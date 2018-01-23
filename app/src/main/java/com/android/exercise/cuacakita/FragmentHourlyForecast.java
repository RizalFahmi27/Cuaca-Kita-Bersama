package com.android.exercise.cuacakita;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.exercise.cuacakita.Models.Response;
import com.android.exercise.cuacakita.Models.Weather;

/**
 * Created by Rizal Fahmi on 21-Oct-16.
 */
public class FragmentHourlyForecast extends Fragment implements WeatherFragmentCallbacks {

    private LayoutInflater inflater;
    MainActivity mainActivity;
    Context context;
    private LinearLayout mainLayout;
    private LinearLayout linearLayoutHourly;

    public static FragmentHourlyForecast newInstance(String strArgs){
        Bundle args = new Bundle();

        FragmentHourlyForecast fragment = new FragmentHourlyForecast();
        args.putString("stringArgs2",strArgs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            mainActivity = (MainActivity) getActivity();
        }
        catch (IllegalStateException e){
            throw new IllegalStateException("Activity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_hourly,null);
        linearLayoutHourly = (LinearLayout) mainLayout.findViewById(R.id.hourlyLinearLayout);
        return mainLayout;
    }

    @Override
    public void onMessageFromActivityToFragment(Weather weather) {
        linearLayoutHourly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float upY, downY = 0;
                Log.d("Swipe","Touched Linear layout");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: {
                        upY = event.getY();
                        return true;
                    }
                case MotionEvent.ACTION_UP: {
                    upY = event.getY();

                    float deltaY = downY - upY;
                    if(Math.abs(deltaY) > 120 ){
                        if(deltaY < 0 ){
                            onTopToBottom(deltaY);
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
    }

    @Override
    public void onMessageFromActivityToFragment(Response response) {

    }

    private void onTopToBottom(float deltaY) {

        Toast.makeText(mainActivity,"Distance : "+deltaY,Toast.LENGTH_SHORT).show();

        LinearLayout linearLayout = (LinearLayout) mainActivity.findViewById(R.id.fragmentWeather);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,3f);
        linearLayout.setLayoutParams(params);

        LinearLayout linearLayout2 = (LinearLayout) mainActivity.findViewById(R.id.containerBigWeather);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,7f);
        linearLayout2.setLayoutParams(params2);

        mainActivity.onMessageFromFragmentToChangeFragment(null, MainActivity.FRAGMENT_FORECAST);
    }
}
