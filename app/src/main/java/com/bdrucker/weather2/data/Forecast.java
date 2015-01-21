package com.bdrucker.weather2.data;

import android.content.res.Resources;

import com.bdrucker.weather2.R;

import java.util.Date;

public class Forecast implements BaseForecastInterface {
    // Data
    private final Date forecastDate;
    private final Integer weatherCode;
    private final Integer degreesCelsius;
    private final String windDirection;
    private final Integer windKilometersPerHour;
    private final Integer humidity;
    private final Integer pressure;

    // Derived/cached data
    private final WeatherResources.WeatherCodeResourceSet weatherCodeResources;
    private final Integer windValueId;

    /**
     * Package-visible constructor.  Create instances of {@link com.bdrucker.weather2.data.Forecast} using
     * {@link com.bdrucker.weather2.data.Forecast.Builder}.
     *
     * @param forecastDate          The day of the weather forecast.
     * @param weatherCode           A code from the API describing the kind of weather (sunny, foggy, etc).
     * @param degreesCelsius        Temperature in Celsius
     * @param windDirection         Wind direction strings from the API (N, SW, NNE, etc).
     * @param windKilometersPerHour Wind speed in kph.
     * @param humidity              Humidity in percentage.
     * @param pressure              Pressure in mb.
     */
    Forecast(Date forecastDate, Integer weatherCode, Integer degreesCelsius, String windDirection,
             Integer windKilometersPerHour, Integer humidity, Integer pressure) {
        this.forecastDate = forecastDate;
        this.weatherCode = weatherCode;
        this.degreesCelsius = degreesCelsius;
        this.windDirection = windDirection;
        this.windKilometersPerHour = windKilometersPerHour;
        this.humidity = humidity;
        this.pressure = pressure;

        this.weatherCodeResources = WeatherResources.getWeatherCodeResources(weatherCode);
        this.windValueId = WeatherResources.getWindResources(windDirection);
    }

    @Override
    public Date getForecastDate() {
        return forecastDate;
    }

    @Override
    public String getDescription(Resources resources) {
        return (weatherCodeResources == null) ? null : resources.getString(weatherCodeResources.dayStringId);
    }

    @Override
    public String getNightDescription(Resources resources) {
        return (weatherCodeResources == null) ? null : resources.getString(weatherCodeResources.nightStringId);
    }

    @Override
    public Integer getIconId() {
        return (weatherCodeResources == null) ? null : weatherCodeResources.dayIconId;
    }

    @Override
    public Integer getNightIconId() {
        return (weatherCodeResources == null) ? null : weatherCodeResources.nightIconId;
    }

    @Override
    public String getTemperature(Resources resources, boolean useMetric) {
        if (degreesCelsius == null)
            return null;

        if (useMetric)
            return resources.getString(R.string.temperature_value_metric, degreesCelsius);
        else
            return resources.getString(R.string.temperature_value_imperial, getDegreesFahrenheit());
    }

    @Override
    public String getNightTemperature(Resources resources, boolean useMetric) {
        return null;
    }

    public String getWindValue(Resources resources, boolean useMetric) {
        if ((windValueId == null) || (windKilometersPerHour == null))
            return null;

        if (useMetric) {
            final String units = resources.getString(R.string.wind_units_metric);
            return resources.getString(windValueId, windKilometersPerHour, units);
        } else {
            final String units = resources.getString(R.string.wind_units_imperial);
            return resources.getString(windValueId, getMilesPerHour(), units);
        }
    }

    public String getHumidity(Resources resources) {
        return (humidity == null) ? null : resources.getString(R.string.humidity_value, humidity);
    }

    public String getPressure(Resources resources) {
        return (pressure == null) ? null : resources.getString(R.string.pressure_value, pressure);
    }

    private int getDegreesFahrenheit() {
        return (int) (degreesCelsius * 9f / 5f + 32);
    }

    private int getMilesPerHour() {
        return (int) (windKilometersPerHour * 0.621371f);
    }

    /**
     * Used to construct {@link com.bdrucker.weather2.data.Forecast} instances using the builder pattern.
     */
    public static class Builder {
        private Date forecastDate;
        private Integer weatherCode;
        private Integer degreesCelsius;
        private String windDirection;
        private Integer windKilometersPerHour;
        private Integer humidity;
        private Integer pressure;

        public Builder() {
        }

        public Builder setForecastDate(Date forecastDate) {
            this.forecastDate = forecastDate;
            return this;
        }

        public Builder setWeatherCode(Integer weatherCode) {
            this.weatherCode = weatherCode;
            return this;
        }

        public Builder setDegreesCelsius(Integer degreesCelsius) {
            this.degreesCelsius = degreesCelsius;
            return this;
        }

        public Builder setWindDirection(String windDirection) {
            this.windDirection = windDirection;
            return this;
        }

        public Builder setWindKilometersPerHour(Integer windKilometersPerHour) {
            this.windKilometersPerHour = windKilometersPerHour;
            return this;
        }

        public Builder setHumidity(Integer humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder setPressure(Integer pressure) {
            this.pressure = pressure;
            return this;
        }

        public Forecast build() {
            return new Forecast(forecastDate, weatherCode, degreesCelsius, windDirection, windKilometersPerHour, humidity, pressure);
        }
    }
}