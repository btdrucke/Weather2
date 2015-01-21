package com.bdrucker.weather2.data;

import java.util.Date;

/**
 * Simple structure that holds together a day and night {@link Forecast} and the date for which it is valid.
 */
public class FutureForecast {
    public final Date forecastDate;
    public final Forecast day;
    public final Forecast night;

    public FutureForecast(Date forecastDate, Forecast day, Forecast night) {
        this.forecastDate = forecastDate;
        this.day = day;
        this.night = night;
    }
}
