package com.griffin.bracketbuilder.v2;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mgriffin.listviewex.R;

/**
 * Created by Matt on 1/10/2016.
 */
public class BuildTournamentDialogFragment extends DialogFragment {
    private static final String TAG_LOGGER = "BuildTournament";

    private Button acceptButton;
    private Button cancelButton;
    private EditText mainEditText;
    private LinearLayout containerLinearLayout;
    private FloatingActionButton fabAddTeams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment_create_tournament, container, false);
        initializeViews(rootView);
        return rootView;
    }

    private void initializeViews(View v) {
        acceptButton = (Button) v.findViewById(R.id.btn_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Save all values to Database
                for (int i=0; i < containerLinearLayout.getChildCount(); i++) {
                    LinearLayout innerLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(i);
                    EditText innerTeamOne = (EditText) innerLinearLayout.getChildAt(0);
                    EditText innerTeamTwo = (EditText) innerLinearLayout.getChildAt(1);

                    Log.d(TAG_LOGGER, "TEAM ONE: " + innerTeamOne.getText().toString());
                    Log.d(TAG_LOGGER, "TEAM TWO: " + innerTeamTwo.getText().toString());
                }
            }
        });

        cancelButton = (Button) v.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        mainEditText = (EditText) v.findViewById(R.id.et_team_name1);
        containerLinearLayout = (LinearLayout) v.findViewById(R.id.ll_container);
        fabAddTeams = (FloatingActionButton) v.findViewById(R.id.fab_addTeam);
        fabAddTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_LOGGER, "Adding team into view");
                LinearLayout dynamicLL = new LinearLayout(getActivity());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                dynamicLL.setLayoutParams(lp);

                LinearLayout mostRecentLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(containerLinearLayout.getChildCount()-1);
                EditText mostRecentEditText = (EditText) mostRecentLinearLayout.getChildAt(mostRecentLinearLayout.getChildCount()-1);

                EditText dynamicTeamOne = new EditText(getActivity());
                Log.d(TAG_LOGGER, (mostRecentEditText.getId() + 1) + "");
                dynamicTeamOne.setId(mostRecentEditText.getId() + 1);
                dynamicTeamOne.setLayoutParams(mainEditText.getLayoutParams());
                dynamicTeamOne.setHint("team name.");
                dynamicTeamOne.setGravity(Gravity.CENTER_HORIZONTAL);
                dynamicLL.addView(dynamicTeamOne);

                EditText dynamicTeamTwo = new EditText(getActivity());
                Log.d(TAG_LOGGER, (mostRecentEditText.getId() + 2)+ "");
                dynamicTeamTwo.setId(mostRecentEditText.getId() + 2);
                dynamicTeamTwo.setLayoutParams(mainEditText.getLayoutParams());
                dynamicTeamTwo.setHint("team name.");
                dynamicTeamTwo.setGravity(Gravity.CENTER_HORIZONTAL);
                dynamicLL.addView(dynamicTeamTwo);

                containerLinearLayout.addView(dynamicLL);
            }
        });
    }
}
