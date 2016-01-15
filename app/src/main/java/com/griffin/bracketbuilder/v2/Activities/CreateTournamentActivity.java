package com.griffin.bracketbuilder.v2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;

import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.pojos.Game;
import com.griffin.bracketbuilder.v2.BuildTournamentDialogFragment;
import com.griffin.bracketbuilder.v2.CardAdapters.GameCardAdapter;

import java.sql.SQLException;
import java.util.List;

public class CreateTournamentActivity extends AppCompatActivity {

    private static final String TAG_LOGGER = "CreateTournament";
    private FloatingActionButton fabCreateTournament;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GameCardAdapter gameCardAdapter;
    private List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        initializeViews();
    }

    private void initializeViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("brackets.");
        toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
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

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        GameDataSource gameDataSource = new GameDataSource(this);
        try {
            gameDataSource.open();
            gameDataSource.deleteAllGames();
            for (int i =0; i<20; i++) {
                gameDataSource.createGame("my test game"+i);
            }

            gameList = gameDataSource.getAllGames();
        } catch (SQLException sql) {
            Log.d(TAG_LOGGER, "Could not open gamedatasource", sql);
        }
        gameCardAdapter = new GameCardAdapter(gameList, this);
        gameDataSource.close();
        recyclerView.setAdapter(gameCardAdapter);
        gameCardAdapter.SetOnItemClickListener(new GameCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getApplicationContext(), OpponentsMainActivity.class);
//                intent.putExtra(PublicVars.INTENT_EXTRA_GAME_ID, position);
//                startActivityForResult(intent, 007);
                Intent intent = new Intent(getApplicationContext(), TournamentDetailActivity.class);
                intent.putExtra("GAME_ID", gameList.get(position).getGameId());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(CreateTournamentActivity.this, view.findViewById(R.id.tv_game_name), "gameNameTrans");
                startActivity(intent,optionsCompat.toBundle());

                Log.i(TAG_LOGGER, "POS: " + position);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
