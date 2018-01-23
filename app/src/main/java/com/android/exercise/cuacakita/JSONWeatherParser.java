package com.android.exercise.cuacakita;

import android.util.Log;

import com.android.exercise.cuacakita.Models.Location;
import com.android.exercise.cuacakita.Models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rizal Fahmi on 27-Aug-16.
 */
public class JSONWeatherParser {
    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(data);

            Location loc = new Location();


            JSONObject cityObject = getObject("city", jObj);
            loc.setCountry(getString("country", cityObject));
            loc.setCity(getString("name", cityObject));
            JSONObject coordObj = getObject("coord", cityObject);
            loc.setLatitude(getFloat("lat", coordObj));
            loc.setLongitude(getFloat("lon", coordObj));

            weather.location = loc;

            JSONArray listArray = jObj.getJSONArray("list");

            for (int i = 0; i < 5; i++) {
                JSONObject JSONList = listArray.getJSONObject(i);
                JSONArray jArrWeather = JSONList.getJSONArray("weather");
                JSONObject JSONWeather = jArrWeather.getJSONObject(0);
                //weather.currentCondition[i] = new Weather.CurrentCondition();
                weather.currentCondition[i].setWeatherId(getInt("id", cityObject));
                weather.currentCondition[i].setDescr(getString("description", JSONWeather));
                weather.currentCondition[i].setCondition(getString("main", JSONWeather));
                weather.currentCondition[i].setIcon(getString("icon", JSONWeather));

                JSONObject JSONTemperature = getObject("temp", JSONList);
                weather.currentCondition[i].setPressure(getInt("pressure", JSONList));
                weather.currentCondition[i].setHumidity(getInt("humidity", JSONList));
                weather.temperature[i].setMaxTemp(getFloat("max", JSONTemperature));
                weather.temperature[i].setMinTemp(getFloat("min", JSONTemperature));
                weather.temperature[i].setTemp("" + Math.round(getFloat("day", JSONTemperature)) + "/" + Math.round(getFloat("night", JSONTemperature)));


                weather.wind[i].setSpeed(getFloat("speed", JSONList));
                weather.wind[i].setDeg(getFloat("deg", JSONList));

                weather.clouds[i].setPerc(getInt("clouds", JSONList));
            }

            //JSONArray jArr = jObj.getJSONArray("weather");

//        JSONObject JSONWeather = jArr.getJSONObject(0);
//        weather.currentCondition.setWeatherId(getInt("id",JSONWeather));
//        weather.currentCondition.setDescr(getString("description",JSONWeather));
//        weather.currentCondition.setCondition(getString("main",JSONWeather));
//        weather.currentCondition.setIcon(getString("icon",JSONWeather));
//
//        JSONObject mainObj = getObject("main",jObj);
//        weather.currentCondition.setHumidity(getInt("humidity",mainObj));
//        weather.currentCondition.setPressure(getInt("pressure",mainObj));
//        weather.temperature.setMaxTemp(getFloat("temp_max",mainObj));
//        weather.temperature.setMinTemp(getFloat("temp_min",mainObj));
//        weather.temperature.setTemp(getFloat("temp",mainObj));
//
//        // Wind
//        JSONObject wObj = getObject("wind",jObj);
//        weather.wind.setSpeed(getFloat("speed",wObj));
//        weather.wind.setDeg(getFloat("deg",wObj));
//
//        // Clouds
//        JSONObject cObj = getObject("clouds",jObj);
//        weather.clouds.setPerc(getInt("all",cObj));
//        Log.d("URL","Desc when set : "+weather.currentCondition.getDescr());
            return weather;
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException{
        Log.d("URL",tagName+" : "+jObj.getString(tagName));
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException{
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException{
        return jObj.getInt(tagName);
    }
}
