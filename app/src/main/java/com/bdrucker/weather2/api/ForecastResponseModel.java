package com.bdrucker.weather2.api;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A model of the response data coming from the weather2 2-day forecast API.
 */
public class ForecastResponseModel {

    public WeatherModel weather;

    public static class WeatherModel {

        @SerializedName("curren_weather")
        public List<CurrentWeatherModel> currentWeather;
        public List<ForecastModel> forecast;

        public static class ForecastPeriodModel {
            @SerializedName("weather_code")
            public Integer weatherCode;
            public List<WindModel> wind;

            public static class WindModel {
                @SerializedName("dir")
                public String direction;
                public Integer speed;
            }
        }

        public static class CurrentWeatherModel extends ForecastPeriodModel {
            public Integer humidity;
            public Integer pressure;
            @SerializedName("temp")
            public Integer temperature;
        }

        public static class ForecastModel {
            private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            public String date;
            public List<ForecastPeriodModel> day;
            @SerializedName("day_max_temp")
            public Integer dayMaxTemperature;
            public List<ForecastPeriodModel> night;
            @SerializedName("night_min_temp")
            public Integer nightMinTemperature;

            public Date getDate() {
                if (date == null)
                    return null;

                try {
                    return dateFormat.parse(date);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
    }
}
