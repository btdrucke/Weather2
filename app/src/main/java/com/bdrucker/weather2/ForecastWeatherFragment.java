package com.bdrucker.weather2;

import android.content.res.Resources;
import android.view.View;

import com.bdrucker.weather2.data.FutureForecast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ForecastWeatherFragment extends BaseWeatherFragment<List<FutureForecast>> {
    // Format for displaying forecast dates in the current locale.
    private static DateFormat forecastDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    // Cached views.
    private List<ForecastLayout> forecastLayouts;

    @Override
    protected int getMainContentLayoutId() {
        return R.layout.forecast_weather_fragment;
    }

    @Override
    protected void findViews(View view) {
        forecastLayouts = new ArrayList<>(2);
        for (int id : new int[]{R.id.forecast0, R.id.forecast1}) {
            final View container = view.findViewById(id);
            container.setVisibility(View.GONE);
            forecastLayouts.add(new ForecastLayout(container));
        }
    }

    private static class ForecastLayout {
        final View container;
        final View day;
        final View night;

        ForecastLayout(View container) {
            this.container = container;
            this.day = container.findViewById(R.id.day_weather_card);
            this.night = container.findViewById(R.id.night_weather_card);
        }
    }

    @Override
    protected void bindData() {
        final Resources resources = getResources();
        postalCodeView.setText(resources.getString(R.string.forecast_for_postal_code, postalCode));

        for (int i = 0; i < forecastLayouts.size(); ++i) {
            final ForecastLayout forecastLayout = forecastLayouts.get(i);
            final FutureForecast datum = data.get(i);

            forecastLayout.container.setVisibility(View.GONE);
            bindWeatherCard(datum.day, forecastLayout.day, false);
            bindWeatherCard(datum.night, forecastLayout.night, false);
        }
    }
}
