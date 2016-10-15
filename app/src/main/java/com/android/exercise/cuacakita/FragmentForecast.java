package com.android.exercise.cuacakita;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.exercise.cuacakita.weather_model.Weather;

import java.util.Calendar;

/**
 * Created by Rizal Fahmi on 11-Oct-16.
 */
public class FragmentForecast extends Fragment implements WeatherFragmentCallbacks {

    private GridView gridView;
    MainActivity mainActivity;
    Weather weather;
    Context context;
    Calendar calendar;
    String[] days = new String[]{"Ming","Sen","Sel","Rabu","Kam","Jum","Sab"};
    int fragmentHeight;
    @Override
    public void onMessageFromActivityToFragment(Weather weather) {
        this.weather = weather;
        calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        ForecastAdapter adapter = new ForecastAdapter(mainActivity);
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

        gridView.getChildAt(index).setBackgroundColor(Color.parseColor("#056107"));

        for (int i=0;i<5;i++){
            if(i!=index)
                gridView.getChildAt(i).setBackgroundColor(Color.parseColor("#fff"));
        }
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
            return weather.currentCondition.length;
        }

        @Override
        public Object getItem(int position) {
            return weather.currentCondition[position];
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
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            Holder holder = new Holder();
            View view = null;
            if(inflater!=null){
                view = inflater.inflate(R.layout.forecast_item,null);
            }
            holder.day = (TextView) view.findViewById(R.id.day);
            holder.weatherIcon = (ImageView) view.findViewById(R.id.weatherIconFragment);
            holder.currentWeather = (TextView) view.findViewById(R.id.weatherFragment);
            holder.temperature = (TextView) view.findViewById(R.id.temperatureFragment);

            //Update date and days according to updates action
            String day = "";
            try{
                day = days[(calendar.get(Calendar.DAY_OF_WEEK)-1)+position];
            }
            catch (ArrayIndexOutOfBoundsException ex){
                day = days[(calendar.get(Calendar.DAY_OF_WEEK)-1)+position-7];
                ex.printStackTrace();
            }
            holder.day.setText(day);

            //Update weather condition, temperature, and icon in forecast gridview item

            //Check icon id and associate it with matched icon in drawable
            String icon = "i" + weather.currentCondition[position].getIcon();
            final int bmp = getResources().getIdentifier("drawable/"+icon,"drawable",mContext.getPackageName());
            Log.d("Cuaca","Id gambar : "+bmp);
            Log.d("Cuaca","Icon gambar : "+icon);

            holder.weatherIcon.setImageResource(bmp);
            holder.currentWeather.setText(weather.currentCondition[position].getCondition());
            holder.temperature.setText(weather.temperature[position].getTemp() + (char) 0x00B0);
            //view.setLayoutParams(new GridView.LayoutParams(500,500));
                //linearLayout.setPadding(8,8,8,8);
            view.setMinimumWidth(MainActivity.width/5);
            Log.d("width",""+MainActivity.width/5);

            //Set view onClickListener, those who selected is turned brighter or so....

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDay(position,bmp);
                }
            });

            return view;
        }
    }
}
