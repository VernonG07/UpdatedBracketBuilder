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


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SettingsFragment extends PreferenceFragment {

    private int gameId;
    private boolean isRoundTwoStarted;

    public SettingsFragment() {

    }

//    public static SettingsFragment newInstance(int gameId, boolean isRoundTwoStarted) {
//        SettingsFragment sf = new SettingsFragment();
//
//        Bundle b = new Bundle();
//        b.putInt("GAME_ID", gameId);
//        b.putBoolean("ROUND_TWO_STARTED", isRoundTwoStarted);
//        sf.setArguments(b);
//
//        return sf;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("DEFAULT_RANDO");
        addPreferencesFromResource(R.xml.fragment_settings);

    }
}
