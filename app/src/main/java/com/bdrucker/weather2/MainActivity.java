package com.bdrucker.weather2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bdrucker.weather2.api.ForecastClient;
import com.bdrucker.weather2.data.Forecast;
import com.bdrucker.weather2.data.FutureForecast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity
        extends ActionBarActivity
        implements ActionBar.TabListener, ForecastClient.ForecastListener {

    /**
     * Activity result when returning from the SettingsActivity.
     */
    private static final int ACTIVITY_RESULT_SETTINGS = 1;

    /**
     * Activity result when returning from the SettingsActivity after a server error
     */
    private static final int ACTIVITY_RESULT_SETTINGS_RECOVERY = 2;

    private static final int POSITION_CURRENT_WEATHER = 0;
    private static final int POSITION_FORECAST_WEATHER = 1;

    private static final String PREF_DEFAULT_POSTAL_CODE = "97206";
    private static final String PREF_DEFAULT_UNITS = SettingsActivity.PREF_VALUE_UNITS_METRIC;
    private static final String PREF_KEY_LAST_UPDATED = "last_updated";
    private static final String PREF_KEY_CACHED_FORECAST = "cached_forecast";
    private static final String PREF_KEY_CACHED_FUTURE_FORECASTS = "cached_future_forecast";

    private static final long REFRESH_INTERVAL_MINUTES = 30;

    private SharedPreferences prefs;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;

    private View progressView;

    private CurrentWeatherFragment currentWeatherFragment;
    private ForecastWeatherFragment forecastWeatherFragment;

    private ForecastClient apiClient;
    private Gson gson;

    /**
     * Handle to the refresh menu item so we can disable it during API calls.
     */
    private MenuItem refreshMenuOption;

    private String postalCode;
    private boolean useMetric;
    private Date lastUpdated;
    private Forecast forecast;
    private List<FutureForecast> futureForecasts;

    /**
     * Keep track of when we have resumed.  Sometimes fragments attach before onResume(), sometimes after.
     */
    private boolean hasResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();

        initializePreferences();

        progressView = findViewById(R.id.progress);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        final SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        apiClient = new ForecastClient(this, this);
    }

    private void initializePreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getLastUpdateFromPreferences();

        SharedPreferences.Editor editor = prefs.edit();
        if (prefs.contains(SettingsActivity.PREF_KEY_POSTAL_CODE))
            postalCode = getPostalCodeFromPreferences();
        else {
            editor.putString(SettingsActivity.PREF_KEY_POSTAL_CODE, PREF_DEFAULT_POSTAL_CODE);
            postalCode = PREF_DEFAULT_POSTAL_CODE;
        }

        if (!prefs.contains(SettingsActivity.PREF_KEY_UNITS))
            useMetric = getUseMetricPreferences();
        else {
            editor.putString(SettingsActivity.PREF_KEY_UNITS, PREF_DEFAULT_UNITS);
            useMetric = SettingsActivity.PREF_VALUE_UNITS_METRIC.equals(PREF_DEFAULT_UNITS);
        }

        editor.apply();
    }

    private void checkForUpdate() {
        if (hasResumed && areFragmentsAttached()) {
            if (isTimeForRefresh())
                fetchWeather();
            else
                onSuccessInternal();
        }
    }

    private void fetchWeather() {
        if (apiClient != null) {
            enableRefreshMenuOption(false);
            apiClient.get(postalCode);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        refreshMenuOption = menu.findItem(R.id.action_refresh);

        return true;
    }

    private void enableRefreshMenuOption(boolean enable) {
        if (refreshMenuOption != null)
            refreshMenuOption.setEnabled(enable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, SettingsActivity.class), ACTIVITY_RESULT_SETTINGS);
            handled = true;
        } else if (id == R.id.action_refresh) {
            fetchWeather();
            handled = true;
        }

        return handled || super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof CurrentWeatherFragment)
            currentWeatherFragment = (CurrentWeatherFragment) fragment;
        else if (fragment instanceof ForecastWeatherFragment)
            forecastWeatherFragment = (ForecastWeatherFragment) fragment;

        checkForUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        hasResumed = true;
        checkForUpdate();
    }

    private boolean areFragmentsAttached() {
        return (currentWeatherFragment != null) && (forecastWeatherFragment != null);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Ignore.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Ignore.
    }

    @Override
    public void onFetchStart() {
        showProgress(true);
        enableRefreshMenuOption(false);
    }

    @Override
    public void onSuccess(Forecast forecast, List<FutureForecast> futureForecasts) {
        lastUpdated = new Date();
        this.forecast = forecast;
        this.futureForecasts = futureForecasts;
        saveLastUpdateToPreferences();

        onSuccessInternal();
    }

    private void onSuccessInternal() {
        showProgress(false);
        enableRefreshMenuOption(true);

        if (areFragmentsAttached()) {
            currentWeatherFragment.setData(forecast, postalCode, lastUpdated, useMetric);
            forecastWeatherFragment.setData(futureForecasts, postalCode, lastUpdated, useMetric);
        }
    }

    @Override
    public void onFailure(ForecastClient.FailureReasonEnum reason, int statusCode, String response) {
        showProgress(false);
        enableRefreshMenuOption(true);

        if (ForecastClient.FailureReasonEnum.ERROR_SERVICE.equals(reason)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.server_error)
                    .setMessage(R.string.server_error_body)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivityForResult(i, ACTIVITY_RESULT_SETTINGS_RECOVERY);
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.network_error)
                    .setMessage(R.string.network_error_body)
                    .setNeutralButton(android.R.string.ok, null)
                    .setCancelable(true)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void showProgress(boolean show) {
        if (show)
            progressView.setVisibility(View.VISIBLE);
        else
            progressView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Don't do anything if the activity is finishing.  This data will be picked up when this activity resumes.
        if (isFinishing())
            return;

        String newPostalCode = getPostalCodeFromPreferences();
        boolean newUseMetric = getUseMetricPreferences();

        switch (requestCode) {
            case ACTIVITY_RESULT_SETTINGS:
                if (!TextUtils.equals(postalCode, newPostalCode)) {
                    postalCode = newPostalCode;
                    useMetric = newUseMetric;
                    dispatchPostalCodeChanged();
                }
                if (useMetric != newUseMetric) {
                    useMetric = newUseMetric;
                    dispatchUseMetricChanged();
                }
                break;

            case ACTIVITY_RESULT_SETTINGS_RECOVERY:
                postalCode = newPostalCode;
                useMetric = newUseMetric;
                dispatchPostalCodeChanged();
                break;
        }
    }

    /**
     * Called when the user has returned form the settings activity and the postal code has changed.
     * The useMetric flag may or may not have changed as well.
     */
    private void dispatchPostalCodeChanged() {
        if (areFragmentsAttached())
            fetchWeather();
    }

    /**
     * Called when the user has returned form the settings activity and the useMetric flag has changed.
     * If we make it here, the postal code did not change, however.
     */
    private void dispatchUseMetricChanged() {
        if (areFragmentsAttached()) {
            currentWeatherFragment.updateUseMetric(useMetric);
            forecastWeatherFragment.updateUseMetric(useMetric);
        }
    }

    private String getPostalCodeFromPreferences() {
        return prefs.getString(SettingsActivity.PREF_KEY_POSTAL_CODE, null);
    }

    private boolean getUseMetricPreferences() {
        return SettingsActivity.PREF_VALUE_UNITS_METRIC.equals(prefs.getString(SettingsActivity.PREF_KEY_UNITS, null));
    }

    private void getLastUpdateFromPreferences() {
        long time = prefs.getLong(PREF_KEY_LAST_UPDATED, -1);
        lastUpdated = (time == -1) ? null : new Date(time);

        try {
            String forecastString = prefs.getString(PREF_KEY_CACHED_FORECAST, null);
            forecast = gson.fromJson(forecastString, Forecast.class);

            String futureForecastsString = prefs.getString(PREF_KEY_CACHED_FUTURE_FORECASTS, null);
            futureForecasts = gson.fromJson(futureForecastsString, new TypeToken<List<FutureForecast>>() {
            }.getType());
        } catch (JsonSyntaxException | NullPointerException e) {
            lastUpdated = null;
            forecast = null;
            futureForecasts = null;

            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(PREF_KEY_LAST_UPDATED);
            editor.remove(PREF_KEY_CACHED_FORECAST);
            editor.remove(PREF_KEY_CACHED_FUTURE_FORECASTS);
            editor.apply();
        }
    }

    /**
     * Save the last successful API response in shared preferences.
     */
    private void saveLastUpdateToPreferences() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(PREF_KEY_LAST_UPDATED, lastUpdated.getTime());

        String a = gson.toJson(forecast);
        editor.putString(PREF_KEY_CACHED_FORECAST, a);

        String b = gson.toJson(futureForecasts);
        editor.putString(PREF_KEY_CACHED_FUTURE_FORECASTS, b);

        editor.apply();
    }

    private boolean isTimeForRefresh() {
        if (lastUpdated == null)
            return true;

        final long minutesSinceLastUpdate = TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - lastUpdated.getTime());
        return minutesSinceLastUpdate >= REFRESH_INTERVAL_MINUTES;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case POSITION_CURRENT_WEATHER:
                    return new CurrentWeatherFragment();
                case POSITION_FORECAST_WEATHER:
                    return new ForecastWeatherFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case POSITION_CURRENT_WEATHER:
                    return getString(R.string.title_section1).toUpperCase(l);
                case POSITION_FORECAST_WEATHER:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }
}
