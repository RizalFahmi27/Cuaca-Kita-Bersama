package com.android.exercise.cuacakita;

/**
 * Created by Rizal Fahmi on 27-Aug-16.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class WeatherHttpClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static String IMG_URL = "https://openweathermap.org/img/w/";

    public String getWeatherData(String latitute, String longitude){
        HttpURLConnection con = null;
        InputStream is = null;

        try{
            con = (HttpURLConnection) (new URL(BASE_URL+"lat="+latitute+"&lon="+longitude+"&units=metric&appid=c66ec1cf21401e09fa94c6fa4e7c32fe")).openConnection();
            Log.d("URL",""+BASE_URL+"lat="+latitute+"&lon="+longitude+"&units=metric&appid=c66ec1cf21401e09fa94c6fa4e7c32fe");
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setConnectTimeout(120000);
            con.connect();

            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line+"\r\n");
            is.close();
            con.disconnect();
            return buffer.toString();
        }

        catch (SocketTimeoutException ex){
            ex.printStackTrace();
        }

        catch (Throwable t){
            t.printStackTrace();
        }
        finally {
            try{is.close();} catch (Throwable t){}
            try{con.disconnect();} catch (Throwable t){}
        }
        return null;
    }


    public byte[] getImage(String code){
        HttpURLConnection con = null;
        InputStream is = null;

        try{
            con = (HttpURLConnection)(new URL(IMG_URL+code+".png")).openConnection();
            Log.d("URL","Image URL : "+IMG_URL+code+".png");
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
//            con.setRequestProperty("Connection","close");
            con.connect();

            Log.d("URL","byte length : ");
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (is.read(buffer)!=-1)
                baos.write(buffer);
            Log.d("URL","byte length : "+buffer.length);
            return baos.toByteArray();
        }

        catch (Throwable t){
            t.printStackTrace();
            is = con.getErrorStream();

        }
        finally {
            try{ is.close(); } catch (Throwable t){}
            try{ con.disconnect(); } catch (Throwable t){}
        }

        return null;
    }
}
