package com.glauber.android.popularmovies.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.glauber.android.popularmovies.R;

/**
 * Created by glauberl on 1/9/2017.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure default values are applied.  In a real app, you would
        // want this in a shared function that is used to retrieve the
        // SharedPreferences wherever they are needed.
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Trigger the listener immediately with the preference's
        // current value.
        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(getActivity()), "pref_movies_sort_order");
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        ListPreference listPreference = (ListPreference) findPreference(key);

        String value = listPreference.getValue();

        int prefIndex = listPreference.findIndexOfValue(value);
        if (prefIndex >= 0) {
            listPreference.setSummary(listPreference.getEntries()[prefIndex]);
        }

    }
}