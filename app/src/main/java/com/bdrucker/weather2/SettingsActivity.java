package com.bdrucker.weather2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public static final String PREF_KEY_POSTAL_CODE = "pref_postal_code";
    public static final String PREF_KEY_UNITS = "pref_use_metric";
    public static final String PREF_VALUE_UNITS_IMPERIAL = "0";
    public static final String PREF_VALUE_UNITS_METRIC = "1";

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.pref_general);

        final Preference postalCodePref = findPreference(PREF_KEY_POSTAL_CODE);
        postalCodePref.setSummary(prefs.getString(PREF_KEY_POSTAL_CODE, null));
        postalCodePref.setOnPreferenceChangeListener(this);

        final ListPreference useMetricPref = (ListPreference) findPreference(PREF_KEY_UNITS);
        useMetricPref.setSummary(getListPreferenceEntry(useMetricPref, prefs.getString(PREF_KEY_UNITS, null)));
        useMetricPref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence entry = getListPreferenceEntry(listPreference, stringValue);
            preference.setSummary(entry);
        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

    private CharSequence getListPreferenceEntry(ListPreference listPreference, String stringValue) {
        int index = listPreference.findIndexOfValue(stringValue);
        return (index >= 0) ? listPreference.getEntries()[index] : null;
    }
}
