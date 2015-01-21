package com.bdrucker.weather2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdrucker.weather2.api.ForecastResponseModel;

public class CurrentWeatherFragment extends Fragment {
    private TextView locationView;
    private TextView descriptionView;
    private TextView temperatureView;
    private View windContainer;
    private TextView windValue;
    private View humidityContainer;
    private TextView humidityValue;
    private View pressureContainer;
    private TextView pressureValue;

    private ForecastResponseModel.WeatherModel.CurrentWeatherModel currentWeather;

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
        temperatureView = (TextView) view.findViewById(R.id.temperature);
        windContainer = view.findViewById(R.id.wind_container);
        windValue = (TextView) view.findViewById(R.id.wind_value);
        humidityContainer = view.findViewById(R.id.humidity_container);
        humidityValue = (TextView) view.findViewById(R.id.humidity_value);
        pressureContainer = view.findViewById(R.id.pressure_container);
        pressureValue = (TextView) view.findViewById(R.id.pressure_value);

        bindData();
    }

    public void setData(ForecastResponseModel.WeatherModel.CurrentWeatherModel currentWeather) {
        this.currentWeather = currentWeather;
        bindData();
    }

    private void bindData() {
        View view = getView();
        if (view != null && currentWeather != null) {
            final Resources resources = getResources();
            locationView.setText(resources.getString(R.string.current_weather_location), currentWeather.);
        }
    }
}
