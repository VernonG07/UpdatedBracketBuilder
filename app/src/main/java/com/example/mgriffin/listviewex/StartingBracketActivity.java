package com.example.mgriffin.listviewex;

import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mgriffin.adapters.GameAdapter;
import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.db.MatchUpDataSource;
import com.example.mgriffin.db.TeamDataSource;
import com.example.mgriffin.dialog_fragments.AddBracketDialogFragment;
import com.example.mgriffin.pojos.Game;
import com.example.mgriffin.pojos.MatchUp;
import com.example.mgriffin.pojos.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StartingBracketActivity extends ActionBarActivity {

    ListView bracketView;
    GameAdapter<Game> bracketAdapter;
    List<Game> bracketList;
    private GameDataSource gameDataSource;
    private ImageButton addBracketButton;
    private Toolbar toolbar;
    private CardView cardView;

    private MatchUpDataSource matchUpDataSource;
    private TeamDataSource teamDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_bracket);

//        getActionBar().setTitle("Brackets " + PublicVars.APP_VERSION_NUMBER);
        initializeViews();
        initializeData();
        addListeners();

        if (bracketList.isEmpty()) {
            cardView.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setMultiTaskColor();
            toolbar.setTransitionName("toolbar");

            Transition t = getWindow().getExitTransition();
            t.excludeTarget(android.R.id.statusBarBackground, true);
            t.excludeTarget(android.R.id.navigationBarBackground, true);

            addBracketButton.setBackgroundResource(R.drawable.ripple);
            addBracketButton.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this, R.anim.anim));
        } else {
            addBracketButton.setBackgroundResource(R.drawable.ripple_u21);
        }
    }

    @TargetApi(21)
    private void setMultiTaskColor () {
        setTaskDescription(new ActivityManager.TaskDescription("Simple Bracket Builder", null, Color.parseColor("#FF26729B")));
    }

    private void handleGameLongClick(long position, Context context) {

        final long clickedGamePos = position;
        final Game clickedGame = bracketList.get((int)clickedGamePos);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Modify Game");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAllDataSources();
                //Delete Game
                gameDataSource.deleteGame(clickedGame);
                //Delete associated matchUps and their teams
                List<MatchUp> matchUps = matchUpDataSource.getMatchupsByGameId(clickedGame.getGameId());
                for (MatchUp matchUp : matchUps) {
                    teamDataSource.deleteTeam(matchUp.getTeamOneId());
                    teamDataSource.deleteTeam(matchUp.getTeamTwoId());
                    matchUpDataSource.deleteMatchUp(matchUp);
                }
                gameDataSource.close();
                teamDataSource.close();
                matchUpDataSource.close();
                bracketList.remove((int)clickedGamePos);
                bracketAdapter.notifyDataSetChanged();

                if (bracketList.isEmpty()) {
                    Animation hi = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
                    cardView.setVisibility(View.VISIBLE);
                    cardView.startAnimation(hi);
                }
            }
        });
        alert.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddBracketDialogFragment addBracketDialogFragment= new AddBracketDialogFragment(clickedGame.getGameName());
                addBracketDialogFragment.show(getFragmentManager(), null);
                addBracketDialogFragment.setListener(new AddBracketDialogFragment.Listener() {
                    @Override
                    public void returnData(String d) {
                        openDataSource();
                        gameDataSource.editGameName(clickedGame, d);
                        gameDataSource.close();
                        bracketList.set((int)clickedGamePos, clickedGame);
                        bracketAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        addBracketButton = (ImageButton) findViewById(R.id.btn_add);
        bracketView = (ListView) findViewById(R.id.lv_items);
        cardView = (CardView) findViewById(R.id.main_cv);
    }
    private void initializeData() {
        bracketList = new ArrayList<Game>();

        gameDataSource = new GameDataSource(this);
        matchUpDataSource = new MatchUpDataSource(this);
        teamDataSource = new TeamDataSource(this);
        openDataSource();
        bracketList = gameDataSource.getAllGames();
        gameDataSource.close();
        bracketAdapter = new GameAdapter<Game>(this, R.layout.game_list_view, bracketList, Typeface.createFromAsset(getAssets(), "lking.ttf"));
        bracketView.setAdapter(bracketAdapter);


    }
    private void addListeners () {
        addBracketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBracketDialogFragment df = new AddBracketDialogFragment();
                df.show(getFragmentManager(), null);
                df.setListener(new AddBracketDialogFragment.Listener() {
                    @Override
                    public void returnData(String name) {

                        if (!name.equals("")) {
                            openDataSource();
                            Game g = gameDataSource.createGame(name);
                            gameDataSource.close();

                            if (bracketList.isEmpty()) {
                                Animation hi = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
                                cardView.setVisibility(View.INVISIBLE);
                                cardView.startAnimation(hi);
                            }

                            bracketList.add(g);
                            bracketAdapter.notifyDataSetChanged();
                        }
                    }

                });
            }
        });

        final Context context = this;

        bracketView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {

                handleGameLongClick(l, context);

                return true;
            }
        });

        bracketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), OpponentsMainActivity.class);

                long gameId = bracketList.get((int)l).getGameId();
                intent.putExtra("GAME_ID", gameId);

                if (Build.VERSION.SDK_INT >= 21)
                    view.setTransitionName("newTitle");

                ActivityOptionsCompat o = ActivityOptionsCompat.makeSceneTransitionAnimation(StartingBracketActivity.this,
                        Pair.create((View) toolbar, "toolbar")
                );

                startActivityForResult(intent, 007, o.toBundle());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 007) {
            openDataSource();
            bracketList = gameDataSource.getAllGames();
            gameDataSource.close();
            bracketAdapter = new GameAdapter<Game>(this, R.layout.game_list_view, bracketList, Typeface.createFromAsset(getAssets(), "lking.ttf"));
            bracketView.setAdapter(bracketAdapter);
            bracketAdapter.notifyDataSetChanged();
        }
    }

    private void openDataSource () {
        try {
            gameDataSource.open();
        } catch (SQLException e) {
            Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAllDataSources () {
        try {
            gameDataSource.open();
            teamDataSource.open();
            matchUpDataSource.open();
        } catch (SQLException e) {
            Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bracket_detail, menu);
        menu.findItem(R.id.action_home).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}


