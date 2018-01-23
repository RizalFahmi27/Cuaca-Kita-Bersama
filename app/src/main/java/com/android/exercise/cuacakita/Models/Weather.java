package com.android.exercise.cuacakita.Models;

import android.util.Log;

/**
 * Created by Rizal Fahmi on 21-Aug-16.
 */

public class Weather {
    public Location location;
    public CurrentCondition[] currentCondition = {new CurrentCondition(),new CurrentCondition(),
    new CurrentCondition(), new CurrentCondition(), new CurrentCondition()};
    public Temperature[] temperature = { new Temperature(), new Temperature(), new Temperature(), new Temperature()
    , new Temperature()};
    public Wind[] wind = {new Wind(), new Wind(), new Wind(), new Wind(), new Wind()};
    //public Rain[] rain = new Rain[5];
    //public Snow[] snow = new Snow[5];
    public Clouds[] clouds = { new Clouds(), new Clouds(), new Clouds(), new Clouds(), new Clouds()};

    public byte[][] iconData = new byte[5][1024];




    public class CurrentCondition{
        private int weatherId;
        private String condition;
        private String descr;
        private String icon;

        private float pressure;
        private float humidity;

        public int getWeatherId(){
            return weatherId;
        }

        public void setWeatherId(int weatherId){
            this.weatherId = weatherId;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }

        public String getCondition() {
            return condition;
        }

        public String getDescr() {

            Log.d("URL","return desc : "+descr);
            return descr;
        }

        public String getIcon() {
            return icon;
        }

        public float getPressure() {
            return pressure;
        }

        public float getHumidity() {
            return humidity;
        }
    }

    public class Temperature{
        private String temp;
        private float minTemp;
        private float maxTemp;

        public String getTemp() {
            return temp;
        }

        public float getMinTemp() {
            return minTemp;
        }

        public float getMaxTemp() {
            return maxTemp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }
    }

    public class Wind {
        private float speed;
        private float deg;

        public float getSpeed() {
            return speed;
        }

        public float getDeg() {
            return deg;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public void setDeg(float deg) {
            this.deg = deg;
        }
    }

        public class Rain{
            private String time;
            private float amount;

            public String getTime() {
                return time;
            }

            public float getAmount() {
                return amount;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setAmount(float amount) {
                this.amount = amount;
            }
        }



        public class Snow{
            private String time;
            private float amount;

            public String getTime() {
                return time;
            }

            public float getAmount() {
                return amount;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setAmount(float amount) {
                this.amount = amount;
            }
        }

        public class Clouds{
            private int perc;

            public int getPerc() {
                return perc;
            }

            public void setPerc(int perc) {
                this.perc = perc;
            }
        }
    }

