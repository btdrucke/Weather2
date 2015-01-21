package com.bdrucker.weather2.data;

import android.content.res.Resources;

import java.util.Date;

public interface BaseForecastInterface {
    public Date getForecastDate();

    public String getDescription(Resources resources);

    public String getNightDescription(Resources resources);

    public Integer getIconId();

    public Integer getNightIconId();

    public String getTemperature(Resources resources, boolean useMetric);

    public String getNightTemperature(Resources resources, boolean useMetric);
}
