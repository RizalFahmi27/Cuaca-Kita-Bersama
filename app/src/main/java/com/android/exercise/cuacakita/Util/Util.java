package com.android.exercise.cuacakita.Util;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rizal Fahmi on 11-Jan-18.
 */

public class Util {
  private static final String DATE_PATTERN_FORMATTED = "yyyy-MM-dd HH:mm:ss";
  private static final String DATE_PATTERN_FIXED = "dd/MM/yyyy";
  private static final String DATE_PATTERN_COMMON = "yyyy-M-d";
  public static TemperatureState getCurrentTime(int hour){
    if(isBetween(hour,4,10)) return TemperatureState.MORNING;
    else if(isBetween(hour,10,14)) return TemperatureState.DAY;
    else if(isBetween(hour,14,20)) return TemperatureState.EVENING;
    else return TemperatureState.NIGHT;
  }

  private static boolean isBetween(int x, int lower, int upper){
    return lower <= x && x <= upper;
  }

  public static String getDate(String formatted){
    try {
      Date date = new SimpleDateFormat(DATE_PATTERN_COMMON).parse(formatted);
      String dayOfWeek = getDayOfWeek(date);
      return dayOfWeek + ", " + new SimpleDateFormat(DATE_PATTERN_FIXED).format(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return "Nan";
    }
  }

  public static final String getDayOfWeek(Date date){
    return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
  }

  public static final String getDayOfWeek(String formatted, int increment){
    int day = Integer.valueOf(formatted.split(" ")[0].split("-")[2]) + increment;
    String[] dateWithoutHour = formatted.split(" ")[0].split("-");
    String fixedDateWithoutHour = dateWithoutHour[0] + "-" + dateWithoutHour[1] + "-" + day;
    formatted = fixedDateWithoutHour + " " + formatted.split(" ")[1];
    Log.d("Util","Fixed formatted : "+formatted);
    try {
      Date date = new SimpleDateFormat(DATE_PATTERN_COMMON).parse(formatted);
      return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
    }
    catch (ParseException e){
      e.printStackTrace();
      return "Unknown";
    }
  }
}
