package com.bdrucker.weather2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdrucker.weather2.data.BaseForecastInterface;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implement some common functionality for all weather forecast fragments.
 *
 * @param <T> The class of data to set and display.
 */
public abstract class BaseWeatherFragment<T> extends Fragment {
    /**
     * Format for displaying last updated timestamps in the current locale.
     */
    protected static DateFormat timeStampFormat = DateFormat.getDateTimeInstance();

    // Cached data from setData().
    protected T data;
    protected String postalCode;
    protected Date lastUpdated;
    protected boolean useMetric;

    /**
     * Cached view for displaying postal code information.
     */
    protected TextView postalCodeView;

    /**
     * Cached view for displaying last updated timestamp information.
     */
    protected TextView lastUpdatedView;

    /**
     * Cached copy of the attached activity's resources.  Set in {@link #onActivityCreated(android.os.Bundle)}.
     */
    protected Resources resources;

    /**
     * Get the layout resource ID for this fragment's content.
     *
     * @return Resource ID.
     */
    protected abstract int getMainContentLayoutId();

    /**
     * Subclasses can find and cache additional child views of the content layout.
     *
     * @param parent Root view of this fragment's content layout.  Guaranteed not to be null.
     */
    protected abstract void findViews(View parent);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getMainContentLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resources = getResources();

        final View view = getView();
        if (view != null) {
            postalCodeView = (TextView) view.findViewById(R.id.postal_code);
            lastUpdatedView = (TextView) view.findViewById(R.id.last_updated);
            findViews(view);

            if (data != null)
                bindData();
        }
    }

    /**
     * Set the data that will be used to display in this fragment.  Data is saved and displayed when the fragment is in attached to its activity.
     *
     * @param data        Forecast data.
     * @param postalCode  Postal code used to fetch forecast data.
     * @param lastUpdated The date and time of the last successful service response.
     * @param useMetric   True if values should be displayed using metric units, false for imperial units.
     */
    public final void setData(T data, String postalCode, Date lastUpdated, boolean useMetric) {
        this.data = data;
        this.postalCode = postalCode;
        this.lastUpdated = lastUpdated;
        this.useMetric = useMetric;

        if (getView() != null)
            bindData();
    }

    /**
     * Display {@link #data} on the screen.  Subclasses should extend this method.
     * The data, the fragment's main view, and {@link #resources} are all guaranteed to be valid at this point.
     * <p/>
     * <em>NB: The last updated timestamp text is set in this method's base implementation but not the location text.</em>
     */
    protected void bindData() {
        lastUpdatedView.setText(resources.getString(R.string.updated_date_value, timeStampFormat.format(lastUpdated)));
    }

    /**
     * Bind forecast data to a "card" in the layout.  These are the light or dark areas that display weather
     * descriptions and icon and the temperature.
     *
     * @param forecast Data to display.
     * @param view     Parent view of the forecast card layout.
     * @param isNight  True if we should use the "night" values form the forecast, false for "day" values.
     * @param <F>      Class of forecast data.
     */
    protected <F extends BaseForecastInterface> void bindWeatherCard(F forecast, View view, boolean isNight) {
        final TextView descriptionView = (TextView) view.findViewById(R.id.description);
        final String descriptionText = isNight ? forecast.getNightDescription(resources) : forecast.getDescription(resources);
        descriptionView.setText(descriptionText);

        final ImageView descriptionIcon = (ImageView) view.findViewById(R.id.icon);
        final Integer iconId = isNight ? forecast.getNightIconId() : forecast.getIconId();
        if (iconId != null)
            descriptionIcon.setImageResource(iconId);

        final String temperatureText = isNight ? forecast.getNightTemperature(resources, useMetric) : forecast.getTemperature(resources, useMetric);
        final TextView temperatureView = (TextView) view.findViewById(R.id.temperature);
        temperatureView.setText(temperatureText);
    }
}
