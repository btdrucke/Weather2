package com.bdrucker.weather2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdrucker.weather2.data.Forecast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentWeatherFragment extends Fragment {
    private static DateFormat dateFormat = DateFormat.getDateTimeInstance();

    private TextView locationView;
    private TextView descriptionView;
    private ImageView descriptionIcon;
    private TextView temperatureView;
    private View windContainer;
    private TextView windValue;
    private View humidityContainer;
    private TextView humidityValue;
    private View pressureContainer;
    private TextView pressureValue;
    private TextView lastUpdatedView;

    private Forecast forecast;
    private String postalCode;
    private Date lastUpdated;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_weather_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        locationView = (TextView) view.findViewById(R.id.location);
        descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionIcon = (ImageView) view.findViewById(R.id.icon);
        temperatureView = (TextView) view.findViewById(R.id.temperature);
        windContainer = view.findViewById(R.id.wind_container);
        windValue = (TextView) view.findViewById(R.id.wind_value);
        humidityContainer = view.findViewById(R.id.humidity_container);
        humidityValue = (TextView) view.findViewById(R.id.humidity_value);
        pressureContainer = view.findViewById(R.id.pressure_container);
        pressureValue = (TextView) view.findViewById(R.id.pressure_value);
        lastUpdatedView = (TextView) view.findViewById(R.id.last_updated);

        bindData();
    }

    public void setData(Forecast data, String postalCode, Date lastUpdated) {
        this.forecast = data;
        this.postalCode = postalCode;
        this.lastUpdated = lastUpdated;
        bindData();
    }

    private void bindData() {
        final View view = getView();
        if ((view == null) || (forecast == null))
            return;

        // TODO: to implement.
        boolean useMetric = false;

        final Resources resources = getResources();
        locationView.setText(resources.getString(R.string.current_weather_location, postalCode));
        lastUpdatedView.setText(resources.getString(R.string.updated_date_value, dateFormat.format(lastUpdated)));
        temperatureView.setText(forecast.getTemperature(resources, useMetric));

        final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final boolean isNight = (hour < 6 || hour > 18);
        if (isNight) {
            descriptionView.setText(forecast.getNightDescription(resources));
            Integer iconId = forecast.getNightIconId();
            if (iconId != null)
                descriptionIcon.setImageResource(iconId);
        } else {
            descriptionView.setText(forecast.getDescription(resources));
            Integer iconId = forecast.getIconId();
            if (iconId != null)
                descriptionIcon.setImageResource(iconId);
        }

        // Show wind info if we have the data.

        final String windString = forecast.getWindValue(resources, useMetric);
        if (TextUtils.isEmpty(windString))
            windContainer.setVisibility(View.GONE);
        else {
            windContainer.setVisibility(View.VISIBLE);
            windValue.setText(windString);
        }

        // Show pressure info if we have the data.

        final String pressureString = forecast.getPressure(resources);
        if (TextUtils.isEmpty(windString))
            pressureContainer.setVisibility(View.GONE);
        else {
            pressureContainer.setVisibility(View.VISIBLE);
            pressureValue.setText(pressureString);
        }

        // Show humidity info if we have the data.

        final String humidityString = forecast.getHumidity(resources);
        if (TextUtils.isEmpty(windString))
            humidityContainer.setVisibility(View.GONE);
        else {
            humidityContainer.setVisibility(View.VISIBLE);
            humidityValue.setText(humidityString);
        }
    }
}
