package com.android.exercise.cuacakita;

import android.os.Bundle;

import com.android.exercise.cuacakita.Models.Weather;

/**
 * Created by Rizal Fahmi on 21-Aug-16.
 */
public interface WeatherCallbacks {
    public void onMessageFromFragmentToActivity(String sender, Bundle strValue);
    public void onMessageFromFragmentToChangeFragment(Weather weather, int code);

}
