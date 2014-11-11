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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mgriffin.adapters.GameAdapter;
import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.dialog_fragments.AddBracketDialogFragment;
import com.example.mgriffin.pojos.Game;

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



        //TODO: clean up code!

        addBracketButton = (Button) findViewById(R.id.btn_add);
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

        getActionBar().setTitle("Brackets");

        bracketView = (ListView) findViewById(R.id.lv_items);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
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
}


