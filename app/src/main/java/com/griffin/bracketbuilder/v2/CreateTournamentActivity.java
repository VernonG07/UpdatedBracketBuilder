package com.griffin.bracketbuilder.v2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.mgriffin.listviewex.R;

public class CreateTournamentActivity extends AppCompatActivity {

    private static final String TAG_LOGGER = "CreateTournament";
    private FloatingActionButton fabCreateTournament;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        context = this;
        initializeViews(context);
    }

    private void initializeViews(final Context context) {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("let's add a bracket");
        appBarLayout = (AppBarLayout) findViewById(R.id.MyAppbar);
        fabCreateTournament = (FloatingActionButton) findViewById(R.id.fab_add);
        fabCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_LOGGER, "Opening tournament dialog fragment");
//                appBarLayout.setExpanded(false);

                Intent intent = new Intent(getApplicationContext(), BuildTournamentDialogFragment.class);
                startActivity(intent);

//                BuildTournamentDialogFragment buildTournamentDialogFragment = new BuildTournamentDialogFragment();
//                buildTournamentDialogFragment.setCancelable(false);
//                buildTournamentDialogFragment.show(getFragmentManager(), null);

            }
        });
    }

}
