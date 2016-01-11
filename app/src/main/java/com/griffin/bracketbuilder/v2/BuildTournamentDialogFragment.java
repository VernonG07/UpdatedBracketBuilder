package com.griffin.bracketbuilder.v2;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.listviewex.OpponentsMainActivity;
import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.pojos.Game;
import com.example.mgriffin.public_references.PublicVars;

import java.sql.SQLException;

/**
 * Created by Matt on 1/10/2016.
 */
public class BuildTournamentDialogFragment extends AppCompatActivity {

    private static final String TAG_LOGGER = "BuildTournament";
    private Context context;
    private Button acceptButton;
    private Button cancelButton;
    private EditText tournamentEditText;
    private EditText mainTeamNameEditText;
    private LinearLayout containerLinearLayout;
    private FloatingActionButton fabAddTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogfragment_create_tournament);
        context = this;
        initializeViews();

    }

    private void initializeViews() {
        acceptButton = (Button) findViewById(R.id.btn_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDataSource gds = new GameDataSource(context);
                try {
                    gds.open();
                } catch (SQLException e) {
                    Log.e(TAG_LOGGER, "Could not open data source");
                }

                Game game = gds.createGame(tournamentEditText.getText().toString());

                gds.close();

                //TODO: Save all values to Database and go to Opponents Screen
                for (int i=0; i < containerLinearLayout.getChildCount(); i++) {
                    LinearLayout innerLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(i);
                    EditText innerTeamOne = (EditText) innerLinearLayout.getChildAt(0);
                    EditText innerTeamTwo = (EditText) innerLinearLayout.getChildAt(1);

                    Log.d(TAG_LOGGER, "TEAM ONE: " + innerTeamOne.getText().toString());
                    Log.d(TAG_LOGGER, "TEAM TWO: " + innerTeamTwo.getText().toString());
                }

                Intent intent = new Intent(context, OpponentsMainActivity.class);
                long gameId = game.getGameId();
                intent.putExtra(PublicVars.INTENT_EXTRA_GAME_ID, gameId);
                startActivity(intent);
            }
        });

        cancelButton = (Button) findViewById(R.id.btn_cancel);
        tournamentEditText = (EditText) findViewById(R.id.et_tournament_name);
        mainTeamNameEditText = (EditText) findViewById(R.id.et_team_name1);
        containerLinearLayout = (LinearLayout) findViewById(R.id.ll_container);
        fabAddTeams = (FloatingActionButton) findViewById(R.id.fab_addTeam);
        fabAddTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_LOGGER, "Adding team into view");
                LinearLayout dynamicLL = new LinearLayout(context);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                dynamicLL.setLayoutParams(lp);

                LinearLayout mostRecentLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(containerLinearLayout.getChildCount()-1);
                EditText mostRecentEditText = (EditText) mostRecentLinearLayout.getChildAt(mostRecentLinearLayout.getChildCount()-1);

                EditText dynamicTeamOne = new EditText(context);
                Log.d(TAG_LOGGER, (mostRecentEditText.getId() + 1) + "");
                dynamicTeamOne.setId(mostRecentEditText.getId() + 1);
                dynamicTeamOne.setLayoutParams(mainTeamNameEditText.getLayoutParams());
                dynamicTeamOne.setHint("team name.");
                dynamicTeamOne.setGravity(Gravity.CENTER_HORIZONTAL);
                dynamicLL.addView(dynamicTeamOne);

                EditText dynamicTeamTwo = new EditText(context);
                Log.d(TAG_LOGGER, (mostRecentEditText.getId() + 2)+ "");
                dynamicTeamTwo.setId(mostRecentEditText.getId() + 2);
                dynamicTeamTwo.setLayoutParams(mainTeamNameEditText.getLayoutParams());
                dynamicTeamTwo.setHint("team name.");
                dynamicTeamTwo.setGravity(Gravity.CENTER_HORIZONTAL);
                dynamicLL.addView(dynamicTeamTwo);

                containerLinearLayout.addView(dynamicLL);
            }
        });
    }
}
