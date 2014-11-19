package com.example.mgriffin.listviewex;



import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
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

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance(int gameId) {
        SettingsFragment sf = new SettingsFragment();

        Bundle b = new Bundle();
        b.putInt("GAME_ID", gameId);
        sf.setArguments(b);

        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameId = getArguments().getInt("GAME_ID");

        getPreferenceManager().setSharedPreferencesName(String.valueOf(gameId));
        addPreferencesFromResource(R.xml.fragment_settings);
    }
}
