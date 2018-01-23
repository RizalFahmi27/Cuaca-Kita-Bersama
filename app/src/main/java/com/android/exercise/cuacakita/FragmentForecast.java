package com.android.exercise.cuacakita;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.exercise.cuacakita.Models.Response;
import com.android.exercise.cuacakita.Models.Response.WeatherResponse.WeatherList;
import com.android.exercise.cuacakita.Models.Weather;

import com.android.exercise.cuacakita.Util.Util;
import java.util.Calendar;

/**
 * Created by Rizal Fahmi on 11-Oct-16.
 */
public class FragmentForecast extends Fragment implements WeatherFragmentCallbacks {

    private GridView gridView;
    MainActivity mainActivity;
    Weather weather;
    Response response;
    Context context;
    Calendar calendar;
    String[] days = new String[]{"Ming","Sen","Sel","Rabu","Kam","Jum","Sab"};
    int fragmentHeight;


    @Override
    public void onMessageFromActivityToFragment(Weather weather) {
        this.weather = weather;
        calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());

    }

  @Override
  public void onMessageFromActivityToFragment(Response response) {
    this.response = response;
    setUpAdapter();
  }

  private void setUpAdapter() {
    ForecastAdapter adapter = new ForecastAdapter(mainActivity);
    gridView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        float upY, downY = 0;
        switch (event.getAction()){
          case MotionEvent.ACTION_DOWN: {
            downY = event.getY();
            return true;
          }
          case MotionEvent.ACTION_UP: {
            upY = event.getY();

            float deltaY = downY - upY;
            if(Math.abs(deltaY) > 90 ){
              if(deltaY > 0 ){
                onBottomToTop(deltaY);
                return true;
              }
            }
            else {
              Log.d("Swipe","Your distance : "+deltaY);
            }
            return false;
          }
        }

        return false;
      }
    });
    gridView.setAdapter(adapter);
  }


  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            context = getActivity();
            mainActivity = (MainActivity) getActivity();
        }
        catch (IllegalStateException e){
            throw new IllegalStateException("Activity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_5days_forecast,null);

        gridView = (GridView) linearLayout.findViewById(R.id.gridForecast);
        fragmentHeight = mainActivity.findViewById(R.id.fragmentWeather).getMinimumHeight();
        return linearLayout;
    }

    public static FragmentForecast newInstance(String strArgs) {

        Bundle args = new Bundle();

        FragmentForecast fragment = new FragmentForecast();
        args.putString("stringArgs1",strArgs);
        fragment.setArguments(args);
        return fragment;
    }

    private void selectDay(int index, int bmp){
        Bundle bundle = new Bundle();
        bundle.putString("desc",weather.currentCondition[index].getDescr());
        bundle.putString("condition",weather.currentCondition[index].getCondition());
        bundle.putString("temp",weather.temperature[index].getTemp());
        bundle.putInt("icon",bmp);
        bundle.putInt("date",index);
        mainActivity.onMessageFromFragmentToActivity("fragment_forecast",bundle);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0xFFFFFFFF);
        gradientDrawable.setStroke(5,0x98706A6A);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            gridView.getChildAt(index).setBackgroundDrawable(gradientDrawable);
        }
        else {
            gridView.getChildAt(index).setBackground(gradientDrawable);
        }

        for (int i=0;i<5;i++){
            if(i!=index)
                gridView.getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }
    private void onBottomToTop(float delta){
        Toast.makeText(mainActivity,"Distance : "+delta,Toast.LENGTH_SHORT).show();

        LinearLayout linearLayout = (LinearLayout) mainActivity.findViewById(R.id.fragmentWeather);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,5f);
        linearLayout.setLayoutParams(params);

        LinearLayout linearLayout2 = (LinearLayout) mainActivity.findViewById(R.id.containerBigWeather);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,5f);
        linearLayout2.setLayoutParams(params2);

        gridView.setOnTouchListener(null);
        mainActivity.onMessageFromFragmentToChangeFragment(weather, MainActivity.FRAGMENT_HOURLY_FORECAST);


    }


    private class ForecastAdapter extends BaseAdapter{
        Context mContext;
        private LayoutInflater inflater = null;
        public ForecastAdapter(Context c){
            this.mContext = c;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return response.weatherResponse.getWeatherList().size();
        }

        @Override
        public WeatherList getItem(int position) {
            return response.weatherResponse.getWeatherList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder{
            TextView day;
            ImageView weatherIcon;
            TextView currentWeather;
            TextView temperature;
            RippleView rippleView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            WeatherList.Weather weather = response.weatherResponse.getWeatherList().get(position).getWeather().get(0);
            WeatherList weatherList = response.weatherResponse.getWeatherList().get(position);
            Holder holder = new Holder();
            View view = null;
            if(inflater!=null){
                view = inflater.inflate(R.layout.forecast_item,null);
            }
            holder.day = (TextView) view.findViewById(R.id.day);
            holder.weatherIcon = (ImageView) view.findViewById(R.id.weatherIconFragment);
            holder.currentWeather = (TextView) view.findViewById(R.id.weatherFragment);
            holder.temperature = (TextView) view.findViewById(R.id.temperatureFragment);
            holder.rippleView = (RippleView) view.findViewById(R.id.rippleForecast);

            //Update date and days according to updates action
            String day = Util.getDayOfWeek(response.timeResponse.getFormatted(),position);


            holder.day.setText(day);

            //Update weather condition, temperature, and icon in forecast gridview item

            //Check icon id and associate it with matched icon in drawable
            String icon = "i" + weather.getIcon();
            final int bmp = getResources().getIdentifier("drawable/"+icon,"drawable",mContext.getPackageName());
            Log.d("Cuaca","Id gambar : "+bmp);
            Log.d("Cuaca","Icon gambar : "+icon);

            holder.weatherIcon.setImageResource(bmp);
            holder.currentWeather.setText(weather.getMain());
            holder.temperature.setText(String.valueOf(weatherList.getTemperature().getDay()) + (char) 0x00B0);
            //view.setLayoutParams(new GridView.LayoutParams(500,500));
            //linearLayout.setPadding(8,8,8,8);
            view.setMinimumWidth(MainActivity.width/5);
            Log.d("width",""+MainActivity.width/5);

            //Set view onClickListener, those who selected is turned brighter or so....

            holder.rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDay(position,bmp);
                }
            });

            return view;
        }

    }
}
