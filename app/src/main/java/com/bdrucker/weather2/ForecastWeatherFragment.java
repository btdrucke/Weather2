package com.bdrucker.weather2;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.bdrucker.weather2.data.FutureForecast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ForecastWeatherFragment extends BaseWeatherFragment<List<FutureForecast>> {
    // Format for displaying forecast dates in the current locale.
    private static DateFormat forecastDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

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
        final TextView date;
        final View day;
        final View night;

        ForecastLayout(View container) {
            this.container = container;
            this.date = (TextView) container.findViewById(R.id.date);
            this.day = container.findViewById(R.id.day_weather_card);
            this.night = container.findViewById(R.id.night_weather_card);
        }
    }

    @Override
    protected void bindData() {
        super.bindData();

        final Resources resources = getResources();
        postalCodeView.setText(resources.getString(R.string.forecast_for_postal_code, postalCode));

        for (int i = 0; i < forecastLayouts.size(); ++i) {
            final FutureForecast datum = data.get(i);
            final ForecastLayout forecastLayout = forecastLayouts.get(i);

            final String dateString = forecastDateFormat.format(datum.getForecastDate());
            forecastLayout.date.setText(dateString);
            forecastLayout.container.setVisibility(View.VISIBLE);
        }

        // Now bind all the units-dependant views.
        onUpdateUseMetric();
    }


    @Override
    protected void onUpdateUseMetric() {
        for (int i = 0; i < forecastLayouts.size(); ++i) {
            final FutureForecast datum = data.get(i);
            final ForecastLayout forecastLayout = forecastLayouts.get(i);

            bindWeatherCard(datum.day, forecastLayout.day, false);
            bindWeatherCard(datum.night, forecastLayout.night, true);
        }
    }
}
