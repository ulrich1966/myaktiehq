package com.example.myaktiehq;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class EinstellungenActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Einstellungen gestartet.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Zurück mit Back-Button.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }
}
