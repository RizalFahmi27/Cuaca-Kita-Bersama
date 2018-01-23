package com.android.exercise.cuacakita.Models;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Parcelable.Creator;
import android.util.Log;
import com.android.exercise.cuacakita.Models.Response.WeatherResponse.WeatherList.Temperature;
import com.android.exercise.cuacakita.Util.TemperatureState;
import com.android.exercise.cuacakita.Util.Util;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Rizal Fahmi on 10-Jan-18.
 */
public class Response implements Parcelable {

  public final WeatherResponse weatherResponse;

  public final TimeResponse timeResponse;

  public Response(WeatherResponse weatherResponse,
      TimeResponse timeResponse) {
    this.weatherResponse = weatherResponse;
    this.timeResponse = timeResponse;
  }

  public String getCurrentWeatherMain() {
    return weatherResponse.getWeatherList().get(0).getWeather().get(0).getMain();
  }

  public Response(JSONObject weather, JSONObject time){
    Gson gson = new Gson();
    this.weatherResponse = gson.fromJson(weather.toString(),WeatherResponse.class);
    this.timeResponse = gson.fromJson(time.toString(),TimeResponse.class);
  }

  public String getCurrentWeatherDescription() {
    return weatherResponse.getWeatherList().get(0).getWeather().get(0).getDescription();
  }

  public double getCurrentTemperature(){
    Integer hour = Integer.valueOf(timeResponse.getFormatted().split(" ")[1].substring(0,2));
    TemperatureState state = Util.getCurrentTime(hour);
    Temperature temp = weatherResponse.getWeatherList().get(0).getTemperature();
    switch (state){
      case DAY:return temp.getDay();
      case NIGHT:return temp.getNight();
      case EVENING:return temp.getEvening();
      case MORNING:return temp.getMorning();
      case MAX:return temp.getMax();
      case MIN:return temp.getMin();
      default:return 0;
    }
  }

  public String getCurrentIcon(){
    return weatherResponse.getWeatherList().get(0).getWeather().get(0).getIcon();
  }

  public static class WeatherResponse implements Parcelable {

    @Expose
    private City city;

    @SerializedName("cod")
    @Expose
    private String code;

    @Expose
    private String message;

    @Expose
    private int cnt;

    @SerializedName("list")
    @Expose
    private List<WeatherList> weatherList;

    public WeatherResponse(String code, String message, int cnt, City city,
        List<WeatherList> weatherList) {
      this.code = code;
      this.message = message;
      this.cnt = cnt;
      this.weatherList = weatherList;
      this.city = city;
    }

    public City getCity() {
      return city;
    }

    public void setCity(City city) {
      this.city = city;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public int getCnt() {
      return cnt;
    }

    public void setCnt(int cnt) {
      this.cnt = cnt;
    }

    public List<WeatherList> getWeatherList() {
      return weatherList;
    }

    public void setWeatherList(List<WeatherList> weatherList) {
      this.weatherList = weatherList;
    }

    public static class City implements Parcelable {

      @Expose
      private long id;

      @Expose
      private String name;

      @SerializedName("coord")
      @Expose
      private Coordinate coordinate;

      @Expose
      private String country;

      @Expose
      private String population;

      public City(long id, String name, Coordinate coordinate, String country, String population) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.country = country;
        this.population = population;
      }

      public long getId() {
        return id;
      }

      public void setId(long id) {
        this.id = id;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public Coordinate getCoordinate() {
        return coordinate;
      }

      public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
      }

      public String getCountry() {
        return country;
      }

      public void setCountry(String country) {
        this.country = country;
      }

      public String getPopulation() {
        return population;
      }

      public void setPopulation(String population) {
        this.population = population;
      }

      public static class Coordinate implements Parcelable {

        @SerializedName("lat")
        @Expose
        private double latitude;

        @SerializedName("lon")
        @Expose
        private double longitude;

        public Coordinate(double latitude, double longitude) {
          this.latitude = latitude;
          this.longitude = longitude;
        }

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

        @Override
        public int describeContents() {
          return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
          dest.writeDouble(this.latitude);
          dest.writeDouble(this.longitude);
        }

        protected Coordinate(Parcel in) {
          this.latitude = in.readDouble();
          this.longitude = in.readDouble();
        }

        public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
          @Override
          public Coordinate createFromParcel(Parcel source) {
            return new Coordinate(source);
          }

          @Override
          public Coordinate[] newArray(int size) {
            return new Coordinate[size];
          }
        };
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.coordinate, flags);
        dest.writeString(this.country);
        dest.writeString(this.population);
      }

      protected City(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.coordinate = in.readParcelable(Coordinate.class.getClassLoader());
        this.country = in.readString();
        this.population = in.readString();
      }

      public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
          return new City(source);
        }

        @Override
        public City[] newArray(int size) {
          return new City[size];
        }
      };
    }

    public static class WeatherList implements Parcelable {

      @SerializedName("dt")
      @Expose
      private String dateTime;

      @SerializedName("temp")
      @Expose
      private Temperature temperature;

      @Expose
      private String pressure;

      @Expose
      private String humidity;

      @Expose
      private List<Weather> weather;

      @Expose
      private double speed;

      @SerializedName("deg")
      @Expose
      private double degree;

      @Expose
      private double clouds;

      @Expose
      private double rain;

      @Expose
      private double snow;

      public WeatherList(String dateTime, Temperature temperature, String pressure, String humidity,
          List<Weather> weather, double speed, double degree, double clouds, double rain,
          double snow) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weather = weather;
        this.speed = speed;
        this.degree = degree;
        this.clouds = clouds;
        this.rain = rain;
        this.snow = snow;
      }

      public String getDateTime() {
        return dateTime;
      }

      public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
      }

      public Temperature getTemperature() {
        return temperature;
      }

      public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
      }

      public String getPressure() {
        return pressure;
      }

      public void setPressure(String pressure) {
        this.pressure = pressure;
      }

      public String getHumidity() {
        return humidity;
      }

      public void setHumidity(String humidity) {
        this.humidity = humidity;
      }

      public List<Weather> getWeather() {
        return weather;
      }

      public void setWeather(List<Weather> weather) {
        this.weather = weather;
      }

      public double getSpeed() {
        return speed;
      }

      public void setSpeed(double speed) {
        this.speed = speed;
      }

      public double getDegree() {
        return degree;
      }

      public void setDegree(double degree) {
        this.degree = degree;
      }

      public double getClouds() {
        return clouds;
      }

      public void setClouds(double clouds) {
        this.clouds = clouds;
      }

      public double getRain() {
        return rain;
      }

      public void setRain(double rain) {
        this.rain = rain;
      }

      public double getSnow() {
        return snow;
      }

      public void setSnow(double snow) {
        this.snow = snow;
      }

      public static class Weather implements Parcelable {

        @Expose
        private int id;

        @Expose
        private String main;

        @Expose
        private String description;

        @Expose
        private String icon;

        public Weather(int id, String main, String description, String icon) {
          this.id = id;
          this.main = main;
          this.description = description;
          this.icon = icon;
        }

        public int getId() {
          return id;
        }

        public void setId(int id) {
          this.id = id;
        }

        public String getMain() {
          return main;
        }

        public void setMain(String main) {
          this.main = main;
        }

        public String getDescription() {
          return description;
        }

        public void setDescription(String description) {
          this.description = description;
        }

        public String getIcon() {
          return icon;
        }

        public void setIcon(String icon) {
          this.icon = icon;
        }

        @Override
        public int describeContents() {
          return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(this.id);
          dest.writeString(this.main);
          dest.writeString(this.description);
          dest.writeString(this.icon);
        }

        protected Weather(Parcel in) {
          this.id = in.readInt();
          this.main = in.readString();
          this.description = in.readString();
          this.icon = in.readString();
        }

        public static final Creator<Weather> CREATOR = new Creator<Weather>() {
          @Override
          public Weather createFromParcel(Parcel source) {
            return new Weather(source);
          }

          @Override
          public Weather[] newArray(int size) {
            return new Weather[size];
          }
        };
      }

      public static class Temperature implements Parcelable {

        @Expose
        private double day;

        @Expose
        private double min;

        @Expose
        private double max;

        @Expose
        private double night;

        @SerializedName("eve")
        @Expose
        private double evening;

        @SerializedName("morn")
        @Expose
        private double morning;

        public Temperature(double day, double min, double max, double night, double evening,
            double morning) {
          this.day = day;
          this.min = min;
          this.max = max;
          this.night = night;
          this.evening = evening;
          this.morning = morning;
        }

        public double getDay() {
          return day;
        }

        public void setDay(double day) {
          this.day = day;
        }

        public double getMin() {
          return min;
        }

        public void setMin(double min) {
          this.min = min;
        }

        public double getMax() {
          return max;
        }

        public void setMax(double max) {
          this.max = max;
        }

        public double getNight() {
          return night;
        }

        public void setNight(double night) {
          this.night = night;
        }

        public double getEvening() {
          return evening;
        }

        public void setEvening(double evening) {
          this.evening = evening;
        }

        public double getMorning() {
          return morning;
        }

        public void setMorning(double morning) {
          this.morning = morning;
        }

        @Override
        public int describeContents() {
          return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
          dest.writeDouble(this.day);
          dest.writeDouble(this.min);
          dest.writeDouble(this.max);
          dest.writeDouble(this.night);
          dest.writeDouble(this.evening);
          dest.writeDouble(this.morning);
        }

        protected Temperature(Parcel in) {
          this.day = in.readDouble();
          this.min = in.readDouble();
          this.max = in.readDouble();
          this.night = in.readDouble();
          this.evening = in.readDouble();
          this.morning = in.readDouble();
        }

        public static final Creator<Temperature> CREATOR = new Creator<Temperature>() {
          @Override
          public Temperature createFromParcel(Parcel source) {
            return new Temperature(source);
          }

          @Override
          public Temperature[] newArray(int size) {
            return new Temperature[size];
          }
        };
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dateTime);
        dest.writeParcelable(this.temperature, flags);
        dest.writeString(this.pressure);
        dest.writeString(this.humidity);
        dest.writeList(this.weather);
        dest.writeDouble(this.speed);
        dest.writeDouble(this.degree);
        dest.writeDouble(this.clouds);
        dest.writeDouble(this.rain);
        dest.writeDouble(this.snow);
      }

      protected WeatherList(Parcel in) {
        this.dateTime = in.readString();
        this.temperature = in.readParcelable(Temperature.class.getClassLoader());
        this.pressure = in.readString();
        this.humidity = in.readString();
        this.weather = new ArrayList<Weather>();
        in.readList(this.weather, Weather.class.getClassLoader());
        this.speed = in.readDouble();
        this.degree = in.readDouble();
        this.clouds = in.readDouble();
        this.rain = in.readDouble();
        this.snow = in.readDouble();
      }

      public static final Creator<WeatherList> CREATOR = new Creator<WeatherList>() {
        @Override
        public WeatherList createFromParcel(Parcel source) {
          return new WeatherList(source);
        }

        @Override
        public WeatherList[] newArray(int size) {
          return new WeatherList[size];
        }
      };
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.code);
      dest.writeString(this.message);
      dest.writeInt(this.cnt);
      dest.writeList(this.weatherList);
    }

    protected WeatherResponse(Parcel in) {
      this.code = in.readString();
      this.message = in.readString();
      this.cnt = in.readInt();
      this.weatherList = new ArrayList<WeatherList>();
      in.readList(this.weatherList, WeatherList.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeatherResponse> CREATOR = new Parcelable.Creator<WeatherResponse>() {
      @Override
      public WeatherResponse createFromParcel(Parcel source) {
        return new WeatherResponse(source);
      }

      @Override
      public WeatherResponse[] newArray(int size) {
        return new WeatherResponse[size];
      }
    };
  }

  public static class TimeResponse implements Parcelable {
    @Expose
    private String status;

    @Expose
    private String message;

    @Expose
    private String countryCode;

    @Expose
    private String countryName;

    @Expose
    private String zoneName;

    @Expose
    private String abbreviation;

    @Expose
    private long gmtOffset;

    @Expose
    private String dst;

    @Expose
    private long dstStart;

    @Expose
    private long dstEnd;

    @Expose
    private String nextAbbreviation;

    @Expose
    private long timestamp;

    @Expose
    private String formatted;

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public String getCountryCode() {
      return countryCode;
    }

    public void setCountryCode(String countryCode) {
      this.countryCode = countryCode;
    }

    public String getCountryName() {
      return countryName;
    }

    public void setCountryName(String countryName) {
      this.countryName = countryName;
    }

    public String getZoneName() {
      return zoneName;
    }

    public void setZoneName(String zoneName) {
      this.zoneName = zoneName;
    }

    public String getAbbreviation() {
      return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
      this.abbreviation = abbreviation;
    }

    public long getGmtOffset() {
      return gmtOffset;
    }

    public void setGmtOffset(long gmtOffset) {
      this.gmtOffset = gmtOffset;
    }

    public String getDst() {
      return dst;
    }

    public void setDst(String dst) {
      this.dst = dst;
    }

    public long getDstStart() {
      return dstStart;
    }

    public void setDstStart(long dstStart) {
      this.dstStart = dstStart;
    }

    public long getDstEnd() {
      return dstEnd;
    }

    public void setDstEnd(long dstEnd) {
      this.dstEnd = dstEnd;
    }

    public String getNextAbbreviation() {
      return nextAbbreviation;
    }

    public void setNextAbbreviation(String nextAbbreviation) {
      this.nextAbbreviation = nextAbbreviation;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public String getFormatted() {
      return formatted;
    }

    public void setFormatted(String formatted) {
      this.formatted = formatted;
    }

    public TimeResponse(String status, String message, String countryCode, String countryName,
        String zoneName, String abbreviation, long gmtOffset, String dst, long dstStart,
        long dstEnd,
        String nextAbbreviation, long timestamp, String formatted) {
      this.status = status;
      this.message = message;
      this.countryCode = countryCode;
      this.countryName = countryName;
      this.zoneName = zoneName;

      this.abbreviation = abbreviation;
      this.gmtOffset = gmtOffset;
      this.dst = dst;
      this.dstStart = dstStart;
      this.dstEnd = dstEnd;
      this.nextAbbreviation = nextAbbreviation;
      this.timestamp = timestamp;
      this.formatted = formatted;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.status);
      dest.writeString(this.message);
      dest.writeString(this.countryCode);
      dest.writeString(this.countryName);
      dest.writeString(this.zoneName);
      dest.writeString(this.abbreviation);
      dest.writeLong(this.gmtOffset);
      dest.writeString(this.dst);
      dest.writeLong(this.dstStart);
      dest.writeLong(this.dstEnd);
      dest.writeString(this.nextAbbreviation);
      dest.writeLong(this.timestamp);
      dest.writeString(this.formatted);
    }

    protected TimeResponse(Parcel in) {
      this.status = in.readString();
      this.message = in.readString();
      this.countryCode = in.readString();
      this.countryName = in.readString();
      this.zoneName = in.readString();
      this.abbreviation = in.readString();
      this.gmtOffset = in.readLong();
      this.dst = in.readString();
      this.dstStart = in.readLong();
      this.dstEnd = in.readLong();
      this.nextAbbreviation = in.readString();
      this.timestamp = in.readLong();
      this.formatted = in.readString();
    }

    public static final Creator<TimeResponse> CREATOR = new Creator<TimeResponse>() {
      @Override
      public TimeResponse createFromParcel(Parcel source) {
        return new TimeResponse(source);
      }

      @Override
      public TimeResponse[] newArray(int size) {
        return new TimeResponse[size];
      }
    };
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.weatherResponse, flags);
    dest.writeParcelable(this.timeResponse, flags);
  }

  protected Response(Parcel in) {
    this.weatherResponse = in.readParcelable(WeatherResponse.class.getClassLoader());
    this.timeResponse = in.readParcelable(TimeResponse.class.getClassLoader());
  }

  public static final Creator<Response> CREATOR = new Creator<Response>() {
    @Override
    public Response createFromParcel(Parcel source) {
      return new Response(source);
    }

    @Override
    public Response[] newArray(int size) {
      return new Response[size];
    }
  };
}
