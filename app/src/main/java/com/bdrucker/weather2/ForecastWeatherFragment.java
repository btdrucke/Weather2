package com.bdrucker.weather2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdrucker.weather2.data.FutureForecast;

import java.util.List;

public class ForecastWeatherFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forecast_weather_fragment, container, false);
    }

    public void setData(List<FutureForecast> forecasts) {

    }
}
