package com.example.app_10p5;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Jean-loup Beaussart on 05/05/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
