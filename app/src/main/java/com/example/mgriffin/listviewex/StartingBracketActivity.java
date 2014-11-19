package com.example.mgriffin.listviewex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mgriffin.adapters.GameAdapter;
import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.dialog_fragments.AddBracketDialogFragment;
import com.example.mgriffin.pojos.Game;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;


public class StartingBracketActivity extends Activity {

    ListView bracketView;
    GameAdapter<Game> bracketAdapter;
    List<Game> bracketList;
    private GameDataSource gameDataSource;
    private Button addBracketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_bracket);

        getActionBar().setTitle("Brackets");

        initializeViews();
        initializeData();
        addListeners();

//        ViewTarget viewTarget = new ViewTarget(addBracketButton);
//
//        ShowcaseView scv = new ShowcaseView.Builder(this, true)
//                .setTarget(viewTarget)
//                .setContentTitle("Welcome")
//                .setContentText("Press this button to get started")
//                .build();
//
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        scv.setButtonPosition(rlp);
//        scv.setButtonText("haha");
    }

    private void handleGameLongClick(long position, Context context) {

        final long clickedGamePos = position;
        final Game clickedGame = bracketList.get((int)clickedGamePos);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Modify Game");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gameDataSource.deleteGame(clickedGame);
                bracketList.remove((int)clickedGamePos);
                bracketAdapter.notifyDataSetChanged();
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
                        gameDataSource.editGameName(clickedGame, d);
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
        addBracketButton = (Button) findViewById(R.id.btn_add);
        bracketView = (ListView) findViewById(R.id.lv_items);
    }
    private void initializeData() {
        bracketList = new ArrayList<Game>();

        gameDataSource = new GameDataSource(this);

        try {
            gameDataSource.open();
        } catch (Exception e) {
            Toast.makeText(this, "could not open", Toast.LENGTH_SHORT).show();
        }

        bracketList = gameDataSource.getAllGames();

        bracketAdapter = new GameAdapter<Game>(this, R.layout.game_list_view, bracketList);
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
                        Game g = gameDataSource.createGame(name);
                        bracketList.add(g);
                        bracketAdapter.notifyDataSetChanged();
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

                startActivity(intent);
            }
        });
    }
}


