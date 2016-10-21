package com.android.exercise.cuacakita;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exercise.cuacakita.PullRefreshContainer.PullRefreshContainerView;
import com.android.exercise.cuacakita.weather_model.Weather;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements WeatherCallbacks {

    Animation rotationAnimation;
    FragmentTransaction fragmentTransaction;
    FragmentForecast fg;
    LocationListener locationListener;
    LocationManager locationManager;
    long currentTime;
    final Handler timeHandler = new Handler();
    private boolean isReadyToUpdate=false;
    TextView lastUpdateText, temperatureText, currentWeatherText, cityName, dateText;
    ImageView weatherIcon;
    ProgressBar progressBar;
    String[] days = new String[]{"Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
    public static int width;
    public static int height;
    public static Context mainContext;
    PullRefreshContainerView mContainer;
    LinearLayout mPullResfreshHeader;

    Handler AnimationHandler;
    Animation in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fg = FragmentForecast.newInstance("weather");
        fragmentTransaction.replace(R.id.fragmentWeather,fg);
        fragmentTransaction.commit();

        //Initialize Pull to Refresh
       initializePullRefreshFunction();


        //Initialize View
        lastUpdateText = (TextView) findViewById(R.id.lastUpdate);
        lastUpdateText.setText("Ready to update");
        progressBar = (ProgressBar) findViewById(R.id.updateButton);
        temperatureText = (TextView) findViewById(R.id.temperature);
        dateText = (TextView) findViewById(R.id.weatherDate);
        cityName = (TextView) findViewById(R.id.cityName);
        currentWeatherText = (TextView) findViewById(R.id.currentWeather);
        weatherIcon = (ImageView) findViewById(R.id.currentWeatherIcon);

        mainContext = getApplicationContext();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        // Enable fade in and fade out animation on selected or current weather information text
        initiateTextAnimation();

        update();

    }

    private void initializePullRefreshFunction() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mPullResfreshHeader  = (LinearLayout) inflater.inflate(R.layout.pull_refresh_container,null);
        mPullResfreshHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPullResfreshHeader.setGravity(Gravity.CENTER);
        final TextView statusText = (TextView) mPullResfreshHeader.findViewById(R.id.pullText);
        ProgressBar progressBarPull = (ProgressBar) mPullResfreshHeader.findViewById(R.id.pullProgressBar);
        statusText.setText("Pull to Refresh....");
        progressBarPull.setVisibility(View.VISIBLE);
        mContainer = (PullRefreshContainerView) findViewById(R.id.pullRefresh);
        mContainer.setRefreshHeader(mPullResfreshHeader);
        mContainer.bringToFront();
        mContainer.setOnChangeStateListener(new PullRefreshContainerView.OnChangeStateListener() {
            @Override
            public void onChangeState(PullRefreshContainerView container, int state) {
                switch (state){
                    case PullRefreshContainerView.STATE_IDLE:
                    case PullRefreshContainerView.STATE_PULL:
                        statusText.setText("Pull to update");
                        break;
                    case PullRefreshContainerView.STATE_RELEASE:
                        statusText.setText("Release to update");
                        break;
                    case PullRefreshContainerView.STATE_LOADING:
                        statusText.setText("Loading....");
                        update();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void initiateTextAnimation() {
        in = new AlphaAnimation(0.0f,1.0f);
        in.setDuration(1000);
    }

    @Override
    public void onMessageFromFragmentToActivity(String sender, Bundle strValue) {

        String desc = strValue.getString("desc");
        String condition = strValue.getString("condition");
        String temp = strValue.getString("temp");
        int index = strValue.getInt("date");
        int bmp = strValue.getInt("icon");

        currentWeatherText.setText(condition + "\n(" + desc + ")");
        currentWeatherText.startAnimation(in);
        weatherIcon.setImageResource(bmp);
        weatherIcon.startAnimation(in);
        Log.d("URL", "Temp get : " + temp);
        temperatureText.setText("" + temp + (char) 0x00B0);
        temperatureText.startAnimation(in);
        String date = getDate(index);
        dateText.setText(date);
        dateText.startAnimation(in);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureLocation();
                return;
        }
    }

    private void updateWeatherInfo(Weather weather){
        try {
            if (weather.iconData[0] != null && weather.iconData[0].length > 0) {
                Bitmap bmp = BitmapFactory.decodeByteArray(weather.iconData[0], 0, weather.iconData.length);
                weatherIcon.setImageBitmap(bmp);
                Log.d("URL", "This message will appear if icon is loaded successfully");
            }

            cityName.setText(weather.location.getCity() + "," + weather.location.getCountry());
            currentWeatherText.setText(weather.currentCondition[0].getCondition() + "\n(" + weather.currentCondition[0].getDescr() + ")");
            Log.d("URL", "Temp get : " + weather.temperature[0].getTemp());
            temperatureText.setText("" + (weather.temperature[0].getTemp()) + (char) 0x00B0);

            String icon = "i" + weather.currentCondition[0].getIcon();
            int bmp = getResources().getIdentifier("drawable/" + icon, "drawable", getPackageName());

            weatherIcon.setImageResource(bmp);


            String dateNow = getDate(0);
            dateText.setText(dateNow);

            //Send message to Fragment
            fg.onMessageFromActivityToFragment(weather);
        }
        catch (NullPointerException e){
            Toast.makeText(this,"Gagal mengambil data, cek koneksi Anda",Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.GONE);
        lastUpdateText.setText("Tap here to Update");
        lastUpdateText.setAnimation(in);
        lastUpdateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        mContainer.completeRefresh();
    }

    private void update(){

        checkLocationService();
        //ObjectAnimator objAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,500)
        progressBar.setVisibility(View.VISIBLE);
        lastUpdateText.setText("Updating");
        lastUpdateText.setAnimation(in);
        lastUpdateText.setOnClickListener(null);
//        JSONWeatherTask task = new JSONWeatherTask();
//        task.execute(new String[]{String.valueOf(67),String.valueOf(69)});
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MainActivity.this,"Lat : "+location.getLatitude()+" Lang : "+location.getLongitude(),Toast.LENGTH_SHORT).show();
                Log.d("Loc","Lat : "+location.getLatitude()+" Lang : "+location.getLongitude());
                isReadyToUpdate = true;
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude())});
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        updateLocation();
        //startTime = SystemClock.elapsedRealtime();
        //Log.d("Timer","Start : "+SystemClock.elapsedRealtime());
//        final Runnable updateTime = new Runnable() {
//            int time=0;
//            @Override
//            public void run() {
//                time+=300000;
//                lastUpdateText.setText("Terakhir diperbarui " + time + " detik lalu");
//                Log.d("Timer", "" + currentTime);
//                timeHandler.postDelayed(this,300000);
//            }
//        };
//        timeHandler.postDelayed(updateTime,300000);
    }



    private void checkLocationService() {
        boolean network_enabled=false;

        int locationMode = 0;
        String locationProviders;
        try {
//            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                try {
                    locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

                network_enabled = (locationMode != Settings.Secure.LOCATION_MODE_OFF);

            }else{
                locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                network_enabled= (!TextUtils.isEmpty(locationProviders));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(!network_enabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Mohon aktifkan location terlbih dahulu");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Aktifkan Lokasi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
    }

    private void updateLocation(){


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                },10);
                return;
            }
        }
        configureLocation();
    }

    private void configureLocation() {
        Looper looper = Looper.myLooper();
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,looper);
        final Handler myHandler = new Handler(looper);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationManager.removeUpdates(locationListener);
                //Toast.makeText(MainActivity.this,"Gagal Memeperbarui Cuaca",Toast.LENGTH_SHORT).show();
                isReadyToUpdate = false;
            }
        },120000);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        Log.d("Loc","Lat : Got here");


    }

    private String getDate(int index){
        Calendar calendar = Calendar.getInstance();

        DateFormat dateFormatter  = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatter.setLenient(false);
        calendar.add(Calendar.DAY_OF_YEAR,index);
        Date today = calendar.getTime();
        String s = dateFormatter.format(today);
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)-1];


        String finalDateString = (day+", "+s);
        return finalDateString;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = (( new WeatherHttpClient()).getWeatherData(params[0],params[1]));
            try{

                weather = JSONWeatherParser.getWeather(data);
//                for(int i=0;i<5;i++) {
//                    weather.iconData[i] = ((new WeatherHttpClient()).getImage(weather.currentCondition[i].getIcon()));
//                    Log.d("Iconnnn","icon "+i+" : "+weather.iconData[i].length);
//                }
            }
            catch (JSONException e){
        //        finishAndRemoveTask();
                e.printStackTrace();
            }
            return weather;
        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            try {
                String dir = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/TanitionaryData/WeatherData/";
                File file = new File(dir);
                if (!file.exists())
                    file.mkdirs();
                FileOutputStream fos = new FileOutputStream(new File(dir+".weather"), true);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(weather);
                os.close();
            }

            catch (IOException e){
                e.printStackTrace();
            }

             updateWeatherInfo(weather);

        }

    }


}

