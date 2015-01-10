package com.example.mgriffin.dialog_fragments;



import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mgriffin.db.MatchUpDataSource;
import com.example.mgriffin.pojos.MatchUp;
import com.example.mgriffin.public_references.PublicVars;
import com.example.mgriffin.listviewex.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChooseWinnerDialogFragment extends DialogFragment implements View.OnClickListener{

    private MatchUp selectedWinnerMatchUp;
    private MatchUpDataSource matchUpDataSource;
    private long matchUpId;

    public interface WinnerListener {
        void returnWinner(MatchUp matchUp);
    }

    private WinnerListener mListener;

    public void setmListener(WinnerListener winnerListener) {
        this.mListener = winnerListener;
    }

    public ChooseWinnerDialogFragment() {
        // Required empty public constructor
    }

    Button chooseNameOne;
    Button chooseNameTwo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment_choose_winner, container, false);

        matchUpDataSource = new MatchUpDataSource(getActivity());
        try {
            matchUpDataSource.open();
        } catch ( Exception e){

        }
        getDialog().setTitle("Choose Winner");

        matchUpId = getArguments().getLong(PublicVars.SP_GAME_MATCH_UP);
        selectedWinnerMatchUp = matchUpDataSource.getMatchUpById(matchUpId);
        chooseNameOne = (Button) rootView.findViewById(R.id.btn_choose_name_one);
        chooseNameOne.setOnClickListener(this);
        chooseNameOne.setText(selectedWinnerMatchUp.getTeamOneName());
        chooseNameTwo = (Button) rootView.findViewById(R.id.btn_choose_name_two);
        chooseNameTwo.setOnClickListener(this);
        chooseNameTwo.setText(selectedWinnerMatchUp.getTeamTwoName());

        return rootView;
    }

    @Override
    public void onClick(View view) {

        MatchUp winner = null;

        switch (view.getId()) {
            case R.id.btn_choose_name_one:
                winner = matchUpDataSource.assignMatchUpWinner(matchUpId, selectedWinnerMatchUp.getTeamOneId(), selectedWinnerMatchUp.getTeamOneName());
                getDialog().dismiss();
                break;
            case R.id.btn_choose_name_two:
                winner = matchUpDataSource.assignMatchUpWinner(matchUpId, selectedWinnerMatchUp.getTeamTwoId(), selectedWinnerMatchUp.getTeamTwoName());
                getDialog().dismiss();
                break;
            default:
                break;
        }

        selectedWinnerMatchUp.setWinnerId(winner.getWinnerId());
        selectedWinnerMatchUp.setWinnerName(winner.getWinnerName());

        mListener.returnWinner(selectedWinnerMatchUp);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}
