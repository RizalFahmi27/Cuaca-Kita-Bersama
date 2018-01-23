package com.android.exercise.cuacakita.Interfaces;

import com.android.exercise.cuacakita.Util.Api;

import com.android.exercise.cuacakita.Models.Response.TimeResponse;
import com.android.exercise.cuacakita.Models.Response.WeatherResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rizal Fahmi on 10-Jan-18.
 */
public interface NetworkService {

  interface TimeService{
    @GET(Api.URL_GET_TIME)
    Single<TimeResponse> queryTime(@Query(Api.PARAM_LATITUDE) double latitude,
        @Query(Api.PARAM_LONGITUDE_ALT) double longitude);
  }

  interface WeatherService {

    @GET(Api.URL_DAILY)
    Single<WeatherResponse> queryWeather(@Query(Api.PARAM_LATITUDE) double latitude,
        @Query(Api.PARAM_LONGITUDE) double longitude);


  }
}
