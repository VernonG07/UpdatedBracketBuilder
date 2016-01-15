package com.griffin.bracketbuilder.v2.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.pojos.Game;

import java.sql.SQLException;

public class TournamentDetailActivity extends AppCompatActivity {

    private TextView tvTournamentName;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTournamentName = (TextView) findViewById(R.id.tv_tournament_name);

        String t = String.valueOf(getIntent().getExtras().get("GAME_ID"));

        GameDataSource gameDataSource = new GameDataSource(this);
        try {
            gameDataSource.open();
            game = gameDataSource.getExistingGameById(Long.valueOf(t));
        } catch (SQLException sql) {
            Log.d("TAG_LOGGER", "could not open", sql);
        }
        tvTournamentName.setText(game.getGameName());

        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
