package com.example.mgriffin.listviewex;

import android.animation.AnimatorInflater;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mgriffin.adapters.MatchUpAdapter;
import com.example.mgriffin.db.GameDataSource;
import com.example.mgriffin.db.MatchUpDataSource;
import com.example.mgriffin.db.TeamDataSource;
import com.example.mgriffin.dialog_fragments.AddBracketDialogFragment;
import com.example.mgriffin.dialog_fragments.AssignNewOpponentsDialogFragment;
import com.example.mgriffin.dialog_fragments.ChooseWinnerDialogFragment;
import com.example.mgriffin.pojos.Game;
import com.example.mgriffin.pojos.MatchUp;
import com.example.mgriffin.pojos.Team;
import com.example.mgriffin.public_references.PublicVars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class OpponentsFragment extends Fragment{

    private TextView bracketTitleView;
    private TextView roundTitleView;
    private ImageButton goToNextRound;
    private ImageButton addNewMatchUp;
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
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private CheckBox foot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_opponents_list, container, false);

        getParms();
        initializeDataSources();
        initializeViews(rootView);
        initializeData();
        modifyActionBar();
        api21Actions();

        return rootView;
    }

    private void api21Actions() {
        if (Build.VERSION.SDK_INT >= 21) {
            addNewMatchUp.setBackgroundResource(R.drawable.ripple);
            addNewMatchUp.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getActivity(), R.anim.anim));

            goToNextRound.setBackgroundResource(R.drawable.ripple_next);
            goToNextRound.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getActivity(), R.anim.anim));
        } else {
            addNewMatchUp.setBackgroundResource(R.drawable.ripple_u21);
            goToNextRound.setBackgroundResource(R.drawable.ripple_next_u21);
        }
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
            Toast.makeText(getActivity(), "Database was not opened", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    private void initializeViews(View rootView) {

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "lking.ttf");

        bracketTitleView = (TextView) rootView.findViewById(R.id.tv_bracket_title);
        bracketTitleView.setTypeface(tf);
        goToNextRound = (ImageButton) rootView.findViewById(R.id.btn_next_round);

        roundTitleView = (TextView) rootView.findViewById(R.id.tv_round_title);
        addNewMatchUp = (ImageButton) rootView.findViewById(R.id.btn_add_opponents);
        if (roundNumber != 1 || matchUpDataSource.isRoundTwoStarted(gameId) ) {

            ViewGroup.LayoutParams lp = addNewMatchUp.getLayoutParams();
            addNewMatchUp.setVisibility(View.INVISIBLE);
            goToNextRound.setLayoutParams(lp);
        }

        matchUps = (ListView) rootView.findViewById(R.id.lv_matchups);
    }

    private void initializeData() {
        currentGame = gameDataSource.getExistingGameById(gameId);
        matchUpData = new ArrayList<MatchUp>();
        matchUpData = matchUpDataSource.getAllMatchUps(currentGame.getGameId(), roundNumber);

        bracketTitleView.setText(currentGame.getGameName());
        roundTitleView.setText("Round: " + roundNumber);

        if (matchUpData.size() == 0) {
            createMatchUps();
        }

        createFooter();
        matchUpAdapter = new MatchUpAdapter<MatchUp>(getActivity(), R.layout.matchup_list_view, matchUpData, Typeface.createFromAsset(getActivity().getAssets(), "OptimusPrinceps.ttf"));
        matchUps.setAdapter(matchUpAdapter);

        AddMatchUpListener btnClick = new AddMatchUpListener();

        addNewMatchUp.setOnClickListener(btnClick);
        goToNextRound.setOnClickListener(btnClick);
        if (roundNumber == 1 && !matchUpDataSource.isRoundTwoStarted(gameId))
            matchUps.setOnItemLongClickListener(new DeleteMatchUpListener());

        if (!matchUpDataSource.isNextRoundStarted(gameId, roundNumber)) {
            if (gameDataSource.getGameWinner(gameId) == null)
                matchUps.setOnItemClickListener(new ChooseWinnerListener());
        }

        if (matchUpData.isEmpty()) {
            goToNextRound.setVisibility(View.INVISIBLE);
        }
    }

    private void createFooter() {
        int spacerHeight;
        spacerHeight = addNewMatchUp.getVisibility()==View.INVISIBLE ? 250 : 450;

        foot = new CheckBox(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("DEFAULT_RANDO", Context.MODE_PRIVATE);
        boolean test = sharedPreferences.getBoolean("checkbox_randomize_opponents" + gameId, sharedPreferences.getBoolean("checkbox_randomize_opponents_default", false));

        if (roundNumber == 1 && !matchUpDataSource.isRoundTwoStarted(gameId)) {
            sharedPreferences.edit().putBoolean("checkbox_randomize_opponents" + gameId, test).commit();
            foot.setChecked(test);
            if ( matchUpData.size() == 0)
                foot.setVisibility(View.INVISIBLE);
            foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences.edit().putBoolean("checkbox_randomize_opponents" + gameId, foot.isChecked()).commit();
                }
            });
            matchUps.addFooterView(foot);
        } else if ( roundNumber == 1) {
            foot.setChecked(test);
            if ( matchUpData.size() == 0)
                foot.setVisibility(View.INVISIBLE);
            foot.setEnabled(false);
            matchUps.addFooterView(foot);
        }
        else {
            foot.setChecked(test);
            if ( matchUpData.size() == 0)
                foot.setVisibility(View.INVISIBLE);
            sharedPreferences.edit().putBoolean("checkbox_randomize_opponents" + gameId, test).commit();
        }

        foot.setText("Randomize Opponents");
        foot.setLayoutParams( new AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

        View footer = new View(getActivity());
        footer.setLayoutParams( new AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT, spacerHeight ));
        matchUps.addFooterView(footer, null, false);
    }

    private void createMatchUps() {

        List<MatchUp> autoMatchUps = matchUpDataSource.getAllMatchUps(currentGame.getGameId(), roundNumber - 1);

        sharedPreferences = getActivity().getSharedPreferences("DEFAULT_RANDO", Context.MODE_PRIVATE);
        boolean randomizeMatchUps = sharedPreferences.getBoolean("checkbox_randomize_opponents" + gameId, false);

        if (randomizeMatchUps) {
            long seed = System.nanoTime();
            Collections.shuffle(autoMatchUps, new Random(seed));
        }

        //TODO: create mode for double elimination
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

    private class AddMatchUpListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_next_round:

                    List<MatchUp> autoMatchUps = matchUpDataSource.getAllMatchUps(currentGame.getGameId(), roundNumber);

                   if (autoMatchUps.size() == 1 && autoMatchUps.get(0).getWinnerId() != 0) {
                       gameDataSource.setGameWinner(currentGame, autoMatchUps.get(0).getWinnerName());
                       getActivity().finish();
                   } else {

                       boolean isWinner = true;
                       for (MatchUp matchUp : autoMatchUps) {
                           if (matchUp.getWinnerId() == 0) {
                               isWinner = false;
                           }
                       }

                       if (matchUpData.size() > 0 && isWinner) {
                           Fragment nextRoundFragment = new OpponentsFragment();
                           Bundle b = new Bundle();
                           b.putInt("ROUND_NUMBER", roundNumber + 1);
                           nextRoundFragment.setArguments(b);

                           FragmentTransaction ft = getFragmentManager().beginTransaction();
                           ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out).addToBackStack(null).replace(android.R.id.content, nextRoundFragment, "round_frag").commit();

                       } else if (!isWinner) {
                           Toast.makeText(getActivity(), "Please select a winner for all match ups", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(getActivity(), "Please add match ups", Toast.LENGTH_SHORT).show();
                       }
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

                            if (nameOne.equals("") && nameTwo.equals("")) {
                                Toast.makeText(getActivity(), "At least one name is required", Toast.LENGTH_SHORT).show();
                            } else {
                                if (nameOne.equals(""))
                                    teamOne = teamDataSource.getExistingTeam(-1);
                                else
                                    teamOne = teamDataSource.createTeam(nameOne);

                                if (nameTwo.equals(""))
                                    teamTwo = teamDataSource.getExistingTeam(-1);
                                else
                                    teamTwo = teamDataSource.createTeam(nameTwo);

                                MatchUp matchUp = matchUpDataSource.createMatchUp(teamOne.getTeamId(), teamTwo.getTeamId(), currentGame.getGameId(), roundNumber, teamOne.getTeamName(), teamTwo.getTeamName());
                                goToNextRound.setVisibility(View.VISIBLE);
                                foot.setVisibility(View.VISIBLE);

                                if (teamOne.getTeamId() == -1) {
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
                                teamDataSource.deleteTeam(longClickMatchUp.getTeamOneId());
                                teamDataSource.deleteTeam(longClickMatchUp.getTeamTwoId());
                                matchUpData.remove(longClickPos);
                                matchUpAdapter.notifyDataSetChanged();
                                if (matchUpData.size() == 0){
                                    Animation slideOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out);
                                    slideOut.setDuration(150);
                                    goToNextRound.setVisibility(View.INVISIBLE);
                                    goToNextRound.setAnimation(slideOut);
                                }
                                if (matchUpData.size() == 0) {
                                    foot.setVisibility(View.INVISIBLE);
                                }
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
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            return true;
        }

        return false;
    }
    private void modifyActionBar () {
        setHasOptionsMenu(true);
    }
}
