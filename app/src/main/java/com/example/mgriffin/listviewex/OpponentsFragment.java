package com.example.mgriffin.listviewex;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mgriffin.adapters.MatchUpAdapter;
import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.db.MatchUpDataSource;
import com.example.mgriffin.db.TeamDataSource;
import com.example.mgriffin.dialog_fragments.AssignNewOpponentsDialogFragment;
import com.example.mgriffin.dialog_fragments.ChooseWinnerDialogFragment;
import com.example.mgriffin.pojos.Game;
import com.example.mgriffin.pojos.MatchUp;
import com.example.mgriffin.pojos.Team;
import com.example.mgriffin.public_references.PublicVars;

import java.util.List;


public class OpponentsFragment extends Fragment{

    private TextView bracketTitleView;
    private TextView roundTitleView;
    private Button addNewMatchUp;
    private ListView matchUps;
    private MatchUpAdapter<MatchUp> matchUpAdapter;
    private List<MatchUp> matchUpData;
    private GameDataSource gameDataSource;
    private TeamDataSource teamDataSource;
    private MatchUpDataSource matchUpDataSource;
    private Game currentGame;
    private MatchUp clickedGameMatchUp;
    private long clickPos;
    private long gameId;
    private int roundNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_opponents_list, container, false);

        getParms();
        initializeDataSources();
        initializeViews(rootView);
        initializeData();

        getActivity().getActionBar().setTitle(currentGame.getGameName());

        return rootView;
    }

    private void getParms() {
        gameId = getActivity().getIntent().getExtras().getLong("GAME_ID");
        roundNumber = this.getArguments().getInt("ROUND_NUMBER");
    }

    private void initializeDataSources() {
        gameDataSource = new GameDataSource(getActivity());
        teamDataSource = new TeamDataSource(getActivity());
        matchUpDataSource = new MatchUpDataSource(getActivity());

        try {
            gameDataSource.open();
            teamDataSource.open();
            matchUpDataSource.open();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "could not open", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeViews(View rootView) {

        if (PublicVars.lionKingTypeFace == null)
            PublicVars.lionKingTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "lking.ttf");

        bracketTitleView = (TextView) rootView.findViewById(R.id.tv_bracket_title);
        bracketTitleView.setTypeface(PublicVars.lionKingTypeFace);

        roundTitleView = (TextView) rootView.findViewById(R.id.tv_round_title);
        addNewMatchUp = (Button) rootView.findViewById(R.id.btn_add_opponents);
        matchUps = (ListView) rootView.findViewById(R.id.lv_matchups);

    }

    private void initializeData() {
        currentGame = gameDataSource.getExistingGameById(gameId);
        matchUpData = matchUpDataSource.getAllMatchUps(currentGame.getGameId(), roundNumber);

        bracketTitleView.setText(currentGame.getGameName());
        roundTitleView.setText("Round: " + roundNumber);

        matchUpAdapter = new MatchUpAdapter<MatchUp>(getActivity(), R.layout.matchup_list_view, matchUpData);
        matchUps.setAdapter(matchUpAdapter);

        addNewMatchUp.setOnClickListener(new AddMatchUpListener());
        matchUps.setOnItemLongClickListener(new DeleteMatchUpListener());
        matchUps.setOnItemClickListener(new ChooseWinnerListener());

    }

    private class AddMatchUpListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AssignNewOpponentsDialogFragment opponentDialogFragment = new AssignNewOpponentsDialogFragment();
            opponentDialogFragment.show(getFragmentManager(), null);
            opponentDialogFragment.setListener(new AssignNewOpponentsDialogFragment.OpponentListener() {
                @Override
                public void returnOpponents(String nameOne, String nameTwo) {

                    Team teamOne = teamDataSource.createTeam(nameOne);
                    Team teamTwo = teamDataSource.createTeam(nameTwo);

                    MatchUp matchUp = matchUpDataSource.createMatchUp(teamOne.getTeamId(), teamTwo.getTeamId(), currentGame.getGameId(), roundNumber, nameOne, nameTwo);

                    matchUpData.add(matchUp);
                    matchUpAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class DeleteMatchUpListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            final MatchUp longClickMatchUp = matchUpData.get((int)l);
            final int longClickPos = (int)l;

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Modify Match Up");
            alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                                matchUpDataSource.deleteMatchUp(longClickMatchUp);
                                matchUpData.remove(longClickMatchUp);
                                matchUpAdapter.notifyDataSetChanged();
                }
            });
            alert.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AssignNewOpponentsDialogFragment newOpponentsDialogFragment = new AssignNewOpponentsDialogFragment(longClickMatchUp.getTeamOneName(), longClickMatchUp.getTeamTwoName());
                    newOpponentsDialogFragment.show(getFragmentManager(), null);
                    newOpponentsDialogFragment.setListener(new AssignNewOpponentsDialogFragment.OpponentListener() {
                        @Override
                        public void returnOpponents(String oName1, String oName2) {

                            Team t1 = teamDataSource.editTeamName(longClickMatchUp.getTeamOneId(), oName1);
                            Team t2 = teamDataSource.editTeamName(longClickMatchUp.getTeamTwoId(), oName2);

                            MatchUp editedMatchUp = matchUpDataSource.editMatchUp(longClickMatchUp, t1, t2);

                            matchUpData.set(longClickPos, editedMatchUp);
                            matchUpAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();


            return true;
        }
    }

    private class ChooseWinnerListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ChooseWinnerDialogFragment chooseWinnerDialog = new ChooseWinnerDialogFragment();
            Bundle bundle = new Bundle();

            clickPos = l;

            clickedGameMatchUp = matchUpData.get((int) l);
            bundle.putLong(PublicVars.SP_GAME_MATCH_UP, clickedGameMatchUp.getId());

            chooseWinnerDialog.setArguments(bundle);
            chooseWinnerDialog.setmListener(new ChooseWinnerDialogFragment.WinnerListener() {
                @Override
                public void returnWinner(MatchUp matchUp) {

                    matchUpData.set((int)clickPos, matchUp);
                    matchUpAdapter.notifyDataSetChanged();
                }
            });
            chooseWinnerDialog.show(getFragmentManager(), null);
        }
    }
}
