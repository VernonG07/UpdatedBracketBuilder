package com.example.mgriffin.listviewex;



import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mgriffin.public_references.PublicVars;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SettingsFragment extends PreferenceFragment {

    private int gameId;
    private boolean isRoundTwoStarted;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(PublicVars.SP_DEFAULT_RANDOM);
        addPreferencesFromResource(R.xml.fragment_settings);

    }
}
