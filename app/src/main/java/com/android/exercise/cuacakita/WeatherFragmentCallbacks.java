package com.android.exercise.cuacakita;


import com.android.exercise.cuacakita.Models.Response;
import com.android.exercise.cuacakita.Models.Weather;

/**
 * Created by Rizal Fahmi on 21-Aug-16.
 */
public interface WeatherFragmentCallbacks {
    void onMessageFromActivityToFragment(Weather weather);
    void onMessageFromActivityToFragment(Response response);
}
