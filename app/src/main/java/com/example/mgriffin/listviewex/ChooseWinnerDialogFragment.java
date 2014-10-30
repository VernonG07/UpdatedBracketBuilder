package com.example.mgriffin.listviewex;



import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ChooseWinnerDialogFragment extends DialogFragment implements View.OnClickListener{

    private GameMatchUp gmu;

    public interface WinnerListener {
        void returnWinner(GameMatchUp gameMatchUp);
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
        View rootView = inflater.inflate(R.layout.fragment_choose_winner_dialog, container, false);

        getDialog().setTitle("Choose Winner");

        gmu = (GameMatchUp) getArguments().getSerializable(PublicVars.SP_GAME_MATCH_UP);

        chooseNameOne = (Button) rootView.findViewById(R.id.btn_choose_name_one);
        chooseNameOne.setOnClickListener(this);
        chooseNameOne.setText(gmu.getTeamOne().getTeamName());
        chooseNameTwo = (Button) rootView.findViewById(R.id.btn_choose_name_two);
        chooseNameTwo.setOnClickListener(this);
        chooseNameTwo.setText(gmu.getTeamTwo().getTeamName());

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_choose_name_one:
                gmu.getTeamOne().setWinner(true);
                gmu.getTeamTwo().setWinner(false);
                getDialog().dismiss();
                break;
            case R.id.btn_choose_name_two:
                gmu.getTeamTwo().setWinner(true);
                gmu.getTeamOne().setWinner(false);
                getDialog().dismiss();
                break;
            default:
                break;
        }

        mListener.returnWinner(gmu);
    }
}
