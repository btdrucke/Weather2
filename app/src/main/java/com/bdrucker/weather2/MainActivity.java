package com.bdrucker.weather2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bdrucker.weather2.api.ForecastClient;
import com.bdrucker.weather2.data.Forecast;
import com.bdrucker.weather2.data.FutureForecast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity
        extends ActionBarActivity
        implements ActionBar.TabListener, ForecastClient.ForecastListener {

    private final int POSITION_CURRENT_WEATHER = 0;
    private final int POSITION_FORECAST_WEATHER = 1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;

    private View progressView;

    private CurrentWeatherFragment currentWeatherFragment;
    private ForecastWeatherFragment forecastWeatherFragment;

    private ForecastClient apiClient;
    private MenuItem refreshMenuOption;
    private String postalCode = "97206";  // TODO: implement.
    private boolean useMetric = false; // TODO: implement.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void fetchWeather() {
        apiClient.get(postalCode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        refreshMenuOption = menu.findItem(R.id.action_refresh);
        enableRefreshMenuOption(false);

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

        if ((currentWeatherFragment != null) && (forecastWeatherFragment != null))
            fetchWeather();
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
        showProgress(false);
        enableRefreshMenuOption(true);
        if (currentWeatherFragment != null) {
            final Date now = Calendar.getInstance(TimeZone.getDefault()).getTime();
            currentWeatherFragment.setData(forecast, postalCode, now, useMetric);
        }
    }

    @Override
    public void onFailure(ForecastClient.FailureReasonEnum reason, int statusCode, String response) {
        showProgress(false);
        enableRefreshMenuOption(true);
    }

    private void showProgress(boolean show) {
        if (show)
            progressView.setVisibility(View.VISIBLE);
        else
            progressView.setVisibility(View.GONE);
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

    private boolean isAirplaneModeOn() {
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() == null;
    }
}
