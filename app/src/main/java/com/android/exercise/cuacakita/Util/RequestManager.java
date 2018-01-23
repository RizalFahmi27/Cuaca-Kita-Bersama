package com.android.exercise.cuacakita.Util;


import com.android.exercise.cuacakita.Util.Api;
import com.android.exercise.cuacakita.Interfaces.NetworkService.TimeService;
import com.android.exercise.cuacakita.Interfaces.NetworkService.WeatherService;
import com.android.exercise.cuacakita.Models.Response.TimeResponse;
import com.android.exercise.cuacakita.Models.Response.WeatherResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;

import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rizal Fahmi on 10-Jan-18.
 */

public class RequestManager {

  private double latitude;
  private double longitude;

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  private OkHttpClient createOkHttpClientWeather(){
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();

                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(Api.PARAM_UNITS,Api.UNITS)
                        .addQueryParameter(Api.PARAM_APPID,Api.WEATHER_FORECAST_API_KEY)
                        .build();

                final Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(loggingInterceptor);

        httpClient.readTimeout(70, TimeUnit.SECONDS);
        httpClient.connectTimeout(70,TimeUnit.SECONDS);

        return httpClient.build();
    }

    private OkHttpClient createOkHttpClientTime(){
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();

                final HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter(Api.PARAM_KEY,Api.TIMEZONE_API_KEY)
                    .addQueryParameter(Api.PARAM_BY,Api.BY)
                    .addQueryParameter(Api.PARAM_FORMAT,Api.FORMAT)
                    .build();

                final Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.readTimeout(70, TimeUnit.SECONDS);
        httpClient.connectTimeout(70,TimeUnit.SECONDS);
        return httpClient.build();
    }

    private Retrofit createRetrofitWeather(){
        return new Retrofit.Builder()
                .baseUrl(Api.BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClientWeather())
                .build();
    }

    private Retrofit createRetrofitTime(){
        return new Retrofit.Builder()
            .baseUrl(Api.BASE_URL_TIME)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createOkHttpClientTime())
            .build();
    }

    public Observable<WeatherResponse> createObservableWeather(){
      return createRetrofitWeather().create(WeatherService.class)
          .queryWeather(latitude,longitude)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .toObservable();
    }

    public Observable<TimeResponse> createObservableTime(){
      return createRetrofitTime().create(TimeService.class)
          .queryTime(latitude,longitude)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .toObservable();
    }


}
