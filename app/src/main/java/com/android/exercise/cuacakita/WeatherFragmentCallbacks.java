package com.android.exercise.cuacakita;


import com.android.exercise.cuacakita.weather_model.Weather;

/**
 * Created by Rizal Fahmi on 21-Aug-16.
 */
public interface WeatherFragmentCallbacks {
    public void onMessageFromActivityToFragment(Weather weather);
}
