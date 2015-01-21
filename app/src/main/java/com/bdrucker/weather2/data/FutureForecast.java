package com.bdrucker.weather2.data;

import android.content.res.Resources;

import java.util.Date;

/**
 * Simple structure that holds together a day and night {@link Forecast} and the date for which it is valid.
 */
public class FutureForecast implements BaseForecastInterface {
    public final Forecast day;
    public final Forecast night;

    /**
     * @param day   Daytime weather forecast data.  Must not be null;
     * @param night Nighttime weather forecast data.  Must not be null;
     */
    public FutureForecast(Forecast day, Forecast night) {
        this.day = day;
        this.night = night;
    }

    @Override
    public Date getForecastDate() {
        // Day and night have the same date, so pick one.
        return day.getForecastDate();
    }

    @Override
    public String getDescription(Resources resources) {
        return day.getDescription(resources);
    }

    @Override
    public String getNightDescription(Resources resources) {
        return night.getNightDescription(resources);
    }

    @Override
    public Integer getIconId() {
        return day.getIconId();
    }

    @Override
    public Integer getNightIconId() {
        return getNightIconId();
    }

    @Override
    public String getTemperature(Resources resources, boolean useMetric) {
        return day.getTemperature(resources, useMetric);
    }

    @Override
    public String getNightTemperature(Resources resources, boolean useMetric) {
        return night.getTemperature(resources, useMetric);
    }
}
