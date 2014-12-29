package com.example.mgriffin.listviewex;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class OpponentsFragment extends Fragment{

    private TextView bracketTitleView;
    private TextView roundTitleView;
    private Button goToNextRound;
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
        modifyActionBar();


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

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "lking.ttf");

        bracketTitleView = (TextView) rootView.findViewById(R.id.tv_bracket_title);
        bracketTitleView.setTypeface(tf);

        roundTitleView = (TextView) rootView.findViewById(R.id.tv_round_title);
        addNewMatchUp = (Button) rootView.findViewById(R.id.btn_add_opponents);
        if (roundNumber != 1 || matchUpDataSource.isRoundTwoStarted(gameId) ) {
            addNewMatchUp.setVisibility(View.INVISIBLE);
        }
        goToNextRound = (Button) rootView.findViewById(R.id.btn_next_round);
        matchUps = (ListView) rootView.findViewById(R.id.lv_matchups);

    }

    private void initializeData() {
        currentGame = gameDataSource.getExistingGameById(gameId);
        matchUpData = new ArrayList<MatchUp>();
        matchUpData = matchUpDataSource.getAllMatchUps(currentGame.getGameId(), roundNumber);

        boolean isData = true;

        if (matchUpData.size() == 0) {

            List<MatchUp> autoMatchUps = matchUpDataSource.getAllMatchUps(currentGame.getGameId(), roundNumber - 1);
            boolean isWinner = true;
            for (MatchUp matchUp : autoMatchUps) {
                if (matchUp.getWinnerId() == 0) {
                    isWinner = false;
                    isData = false;
                }
            }
            if (!isWinner) {
                Toast.makeText(getActivity(), "You must select a winner before going to the next round", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }


            if (isData) {
                if (autoMatchUps.size() == 1) {
                    for (MatchUp winner : autoMatchUps) {
                        Toast.makeText(getActivity(), winner.getWinnerName() + " is the winner!", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(String.valueOf(gameId), Context.MODE_PRIVATE);
                    boolean randomizeMatchUps = sharedPreferences.getBoolean("checkbox_randomize_opponents", false);

                    if (randomizeMatchUps) {
                        long seed = System.nanoTime();
                        Collections.shuffle(autoMatchUps, new Random(seed));
                    }

                    for (int i = 0; i < autoMatchUps.size(); i = i + 2) {
                        MatchUp matchUpWinnerOne = autoMatchUps.get(i);
                        MatchUp matchUpWinnerTwo = new MatchUp();

                        try {
                            matchUpWinnerTwo = autoMatchUps.get(i + 1);
                        } catch (IndexOutOfBoundsException e) {



                            matchUpWinnerTwo.setWinnerId(-1);
                            matchUpWinnerTwo.setWinnerName("BYE");
                        }

                        MatchUp newMatchUp = matchUpDataSource.createMatchUp(matchUpWinnerOne.getWinnerId(), matchUpWinnerTwo.getWinnerId(), currentGame.getGameId(), roundNumber, matchUpWinnerOne.getWinnerName(), matchUpWinnerTwo.getWinnerName());
                        if (newMatchUp.getTeamTwoId()==-1)
                        {
                            matchUpDataSource.assignMatchUpWinner(newMatchUp.getId(), newMatchUp.getTeamOneId(), newMatchUp.getTeamOneName());
                            newMatchUp.setWinnerId(newMatchUp.getTeamOneId());
                            newMatchUp.setWinnerName(newMatchUp.getTeamOneName());
                        }
                        matchUpData.add(newMatchUp);
                    }
                }
            }
        }

        if (isData) {
            bracketTitleView.setText(currentGame.getGameName());
            roundTitleView.setText("Round: " + roundNumber);

            matchUpAdapter = new MatchUpAdapter<MatchUp>(getActivity(), R.layout.matchup_list_view, matchUpData, Typeface.createFromAsset(getActivity().getAssets(), "OptimusPrinceps.ttf"));
            matchUps.setAdapter(matchUpAdapter);

            AddMatchUpListener btnClick = new AddMatchUpListener();

            addNewMatchUp.setOnClickListener(btnClick);
            goToNextRound.setOnClickListener(btnClick);
            if (roundNumber == 1 && !matchUpDataSource.isRoundTwoStarted(gameId))
                matchUps.setOnItemLongClickListener(new DeleteMatchUpListener());
            matchUps.setOnItemClickListener(new ChooseWinnerListener());
        }
    }

    private class AddMatchUpListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_next_round:

                    if (matchUpData.size() > 0) {
                        Fragment nextRoundFragment = new OpponentsFragment();
                        Bundle b = new Bundle();
                        b.putInt("ROUND_NUMBER", roundNumber + 1);
                        nextRoundFragment.setArguments(b);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                      ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_in);

                        ft.addToBackStack(null).replace(android.R.id.content, nextRoundFragment, "round_frag").commit();

                    } else {
                        Toast.makeText(getActivity(), "Please add matchups", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.btn_add_opponents:
                    AssignNewOpponentsDialogFragment opponentDialogFragment = new AssignNewOpponentsDialogFragment();
                    opponentDialogFragment.show(getFragmentManager(), null);
                    opponentDialogFragment.setListener(new AssignNewOpponentsDialogFragment.OpponentListener() {
                        @Override
                        public void returnOpponents(String nameOne, String nameTwo) {

                            Team teamOne;
                            Team teamTwo;

                            if (nameOne.equals(""))
                                teamOne = teamDataSource.getExistingTeam(-1);
                            else
                                teamOne = teamDataSource.createTeam(nameOne);

                            if (nameTwo.equals(""))
                                teamTwo = teamDataSource.getExistingTeam(-1);
                            else
                                teamTwo = teamDataSource.createTeam(nameTwo);

                            MatchUp matchUp = matchUpDataSource.createMatchUp(teamOne.getTeamId(), teamTwo.getTeamId(), currentGame.getGameId(), roundNumber, teamOne.getTeamName(), teamTwo.getTeamName());

                            if ( teamOne.getTeamId() == -1) {
                                matchUpDataSource.assignMatchUpWinner(matchUp.getId(), teamTwo.getTeamId(), teamTwo.getTeamName());
                                matchUp.setWinnerId(teamTwo.getTeamId());
                                matchUp.setWinnerName(teamTwo.getTeamName());
                            } else if (teamTwo.getTeamId() == -1) {
                                matchUpDataSource.assignMatchUpWinner(matchUp.getId(), teamOne.getTeamId(), teamOne.getTeamName());
                                matchUp.setWinnerId(teamOne.getTeamId());
                                matchUp.setWinnerName(teamOne.getTeamName());
                            }


                            matchUpData.add(matchUp);
                            matchUpAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
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
                                matchUpData.remove(longClickPos);
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

                            Team t1;
                            Team t2;

                            if (longClickMatchUp.getTeamOneId() == -1){
                                t1 = teamDataSource.createTeam(oName1);
                            } else {
                                t1 = teamDataSource.editTeamName(longClickMatchUp.getTeamOneId(), oName1);
                            }

                            if (longClickMatchUp.getTeamTwoId() == -1) {
                                t2 = teamDataSource.createTeam(oName2);
                            } else {
                                t2 = teamDataSource.editTeamName(longClickMatchUp.getTeamTwoId(), oName2);
                            }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bracket_detail, menu);

        if (roundNumber > 1) {
            MenuItem menuItem = menu.findItem(R.id.action_settings);
            menuItem.setVisible(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_home:
                getActivity().finish();
                break;

            case R.id.action_settings:

                Fragment f = getFragmentManager().findFragmentByTag("settings_frag");

                if (f == null) {
                    f = new SettingsFragment().newInstance((int)gameId, matchUpDataSource.isRoundTwoStarted(gameId));
                }

                getFragmentManager().beginTransaction().addToBackStack("settings").replace(android.R.id.content, f, "settings_frag").commit();
            return true;
        }

        return false;
    }
    private void modifyActionBar () {
        getActivity().getActionBar().setTitle(currentGame.getGameName());
        setHasOptionsMenu(true);
    }
}
