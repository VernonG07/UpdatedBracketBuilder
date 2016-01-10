package com.griffin.bracketbuilder.v2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.example.mgriffin.listviewex.R;

public class CreateTournamentActivity extends ActionBarActivity {

    private static final String TAG_LOGGER = "CreateTournament";
    private FloatingActionButton fabCreateTournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        initializeViews();
    }

    private void initializeViews() {
        fabCreateTournament = (FloatingActionButton) findViewById(R.id.fab_add);
        fabCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_LOGGER, "Opening tournament dialog fragment");

                BuildTournamentDialogFragment buildTournamentDialogFragment = new BuildTournamentDialogFragment();
                buildTournamentDialogFragment.setCancelable(false);
                buildTournamentDialogFragment.show(getFragmentManager(), null);

            }
        });
    }

}
