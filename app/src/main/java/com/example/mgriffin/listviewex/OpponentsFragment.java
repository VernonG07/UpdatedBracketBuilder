package com.example.mgriffin.listviewex;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class OpponentsFragment extends Fragment {

    TextView bracketTitleView;
    Button addNewMatchUp;
    ListView matchUps;
    MatchUpAdapter<GameMatchUp> matchUpAdapter;
    List<GameMatchUp> matchUpData;
    Set<String> winnersSet = new HashSet<String>();
    private RoundType roundType;

    public OpponentsFragment () {
        //TODO: If data exists, build out the list in read only mode. Otherwise, continue as normal
        roundType = RoundType.CREATE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_opponents_list, container, false);

        bracketTitleView = (TextView) rootView.findViewById(R.id.tv_bracket_title);
        String bracketTitle = getActivity().getIntent().getExtras().getString("BRACKET_NAME");
        bracketTitleView.setText(bracketTitle);

        addNewMatchUp = (Button) rootView.findViewById(R.id.btn_add_opponents);
        addNewMatchUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssignNewOpponentsDialogFragment opponentDialogFragment = new AssignNewOpponentsDialogFragment();
                opponentDialogFragment.show(getFragmentManager(), null);
                opponentDialogFragment.setListener(new AssignNewOpponentsDialogFragment.OpponentListener() {
                    @Override
                    public void returnOpponents(String nameOne, String nameTwo) {

                        GameMatchUp gameMatchUp = new GameMatchUp();
                        gameMatchUp.setTeamOne(new Team(nameOne));
                        gameMatchUp.setTeamTwo(new Team(nameTwo));

                        matchUpData.add(gameMatchUp);
                        matchUpAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        matchUps = (ListView) rootView.findViewById(R.id.lv_matchups);
        matchUpData = new ArrayList<GameMatchUp>();
        matchUpAdapter = new MatchUpAdapter<GameMatchUp>(getActivity(), R.layout.matchup_list_view, matchUpData);
        matchUps.setAdapter(matchUpAdapter);
        matchUps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChooseWinnerDialogFragment chooseWinnerDialog = new ChooseWinnerDialogFragment();
                Bundle bundle = new Bundle();

                GameMatchUp clickedGameMatchUp = matchUpData.get((int) l);
                clickedGameMatchUp.setId((int)l);

                bundle.putSerializable(PublicVars.SP_GAME_MATCH_UP, clickedGameMatchUp);

                chooseWinnerDialog.setArguments(bundle);
                chooseWinnerDialog.setmListener(new ChooseWinnerDialogFragment.WinnerListener() {
                    @Override
                    public void returnWinner(GameMatchUp gmu) {

                        //Updates the List in the correct position
                        matchUpData.set(gmu.getId(), gmu);

                        //Store the winners in SharedPrefs
                        SharedPreferences sp = getActivity().getSharedPreferences(PublicVars.SP_MAIN_KEY, Context.MODE_PRIVATE);

                        for (GameMatchUp gameMatchUp : matchUpData) {
                            if ( GameMatchUp.hasWinner(gameMatchUp)) {
                                winnersSet.add(GameMatchUp.getWinner(gameMatchUp).getTeamName());
                            }
                        }

                        sp.edit().putStringSet(PublicVars.SP_WINNER_LIST, winnersSet).commit();

                        matchUpAdapter.notifyDataSetChanged();
                    }
                });
                chooseWinnerDialog.show(getFragmentManager(), null);
            }
        });

        return rootView;
    }
}
