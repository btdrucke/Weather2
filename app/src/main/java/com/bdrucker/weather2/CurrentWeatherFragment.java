package com.bdrucker.weather2;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bdrucker.weather2.data.Forecast;

import java.util.Calendar;
import java.util.TimeZone;

public class CurrentWeatherFragment extends BaseWeatherFragment<Forecast> {

    // Cached views.
    private View dayCard;
    private View nightCard;
    private View windContainer;
    private TextView windValue;
    private View humidityContainer;
    private TextView humidityValue;
    private View pressureContainer;
    private TextView pressureValue;

    @Override
    protected int getMainContentLayoutId() {
        return R.layout.current_weather_fragment;
    }

    @Override
    protected void findViews(View view) {
        dayCard = view.findViewById(R.id.day_weather_card);
        nightCard = view.findViewById(R.id.night_weather_card);
        windContainer = view.findViewById(R.id.wind_container);
        windValue = (TextView) view.findViewById(R.id.wind_value);
        humidityContainer = view.findViewById(R.id.humidity_container);
        humidityValue = (TextView) view.findViewById(R.id.humidity_value);
        pressureContainer = view.findViewById(R.id.pressure_container);
        pressureValue = (TextView) view.findViewById(R.id.pressure_value);

        dayCard.setVisibility(View.GONE);
        nightCard.setVisibility(View.GONE);
        windContainer.setVisibility(View.GONE);
        humidityContainer.setVisibility(View.GONE);
        pressureContainer.setVisibility(View.GONE);
    }

    @Override
    protected void bindData() {
        super.bindData();

        final Resources resources = getResources();
        postalCodeView.setText(resources.getString(R.string.current_weather_for_postal_code, postalCode));

        if (isNight()) {
            dayCard.setVisibility(View.GONE);
            nightCard.setVisibility(View.VISIBLE);
            bindWeatherCard(data, nightCard, true);
        } else {
            nightCard.setVisibility(View.GONE);
            dayCard.setVisibility(View.VISIBLE);
            bindWeatherCard(data, dayCard, false);
        }

        // Show wind info if we have the data.

        final String windString = data.getWindValue(resources, useMetric);
        if (TextUtils.isEmpty(windString))
            windContainer.setVisibility(View.GONE);
        else {
            windContainer.setVisibility(View.VISIBLE);
            windValue.setText(windString);
        }

        // Show pressure info if we have the data.

        final String pressureString = data.getPressure(resources);
        if (TextUtils.isEmpty(windString))
            pressureContainer.setVisibility(View.GONE);
        else {
            pressureContainer.setVisibility(View.VISIBLE);
            pressureValue.setText(pressureString);
        }

        // Show humidity info if we have the data.

        final String humidityString = data.getHumidity(resources);
        if (TextUtils.isEmpty(windString))
            humidityContainer.setVisibility(View.GONE);
        else {
            humidityContainer.setVisibility(View.VISIBLE);
            humidityValue.setText(humidityString);
        }
    }

    private boolean isNight() {
        final int hour = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.HOUR_OF_DAY);
        return (hour < 6 || hour > 18);
    }
}
