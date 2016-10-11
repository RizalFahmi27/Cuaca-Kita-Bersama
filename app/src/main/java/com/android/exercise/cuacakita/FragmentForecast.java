package com.android.exercise.cuacakita;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    @Override
    public void onMessageFromActivityToFragment(Weather weather) {
        this.weather = weather;
        calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());

        ForecaseAdapter adapter = new ForecaseAdapter(context);
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

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static FragmentForecast newInstance(String strArgs) {

        Bundle args = new Bundle();

        FragmentForecast fragment = new FragmentForecast();
        args.putString("stringArgs1",strArgs);
        fragment.setArguments(args);
        return fragment;
    }


    private class ForecaseAdapter extends BaseAdapter{
        Context mContext;
        private LayoutInflater inflater = null;
        public ForecaseAdapter(Context c){
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
        public View getView(int position, View convertView, ViewGroup parent) {

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
            String today = days[(calendar.get(Calendar.DAY_OF_WEEK)-1)+position];
            holder.day.setText(today);

            //Update weather condition, temperature, and icon in forecast gridview item
            holder.currentWeather.setText(weather.currentCondition[position].getCondition()+"\n( " +
            weather.currentCondition[position].getDescr()+ " )");
            holder.temperature.setText(weather.temperature[position].getTemp() + (char) 0x00B0);
            //view.setLayoutParams(new GridView.LayoutParams(85,85));
                //linearLayout.setPadding(8,8,8,8);
            return view;
        }
    }
}
