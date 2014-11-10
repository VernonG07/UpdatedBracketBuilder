package com.example.mgriffin.dialog_fragments;



import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mgriffin.listviewex.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AssignNewOpponentsDialogFragment extends DialogFragment implements View.OnClickListener{

    private OpponentListener mListener;
    private EditText nameOne;
    private EditText nameTwo;
    private Button addOpponentDialogFragment;

    public void setListener(OpponentListener ol) {
        this.mListener = ol;
    }
    public interface OpponentListener {
        void returnOpponents(String oName1, String oName2);
    }

    private String editNameOne = null;
    private String editNameTwo = null;

    public AssignNewOpponentsDialogFragment () {}

    public AssignNewOpponentsDialogFragment (String editNameOne, String editNameTwo) {
        this.editNameOne = editNameOne;
        this.editNameTwo = editNameTwo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialogfragment_assign_opponents, container, false);

        nameOne = (EditText) rootView.findViewById(R.id.et_name1);
        nameTwo = (EditText) rootView.findViewById(R.id.et_name2);
        addOpponentDialogFragment = (Button) rootView.findViewById(R.id.btn_add_opponents);
        addOpponentDialogFragment.setOnClickListener(this);

        if (editNameOne != null && editNameTwo != null) {
            nameOne.setText(editNameOne);
            nameTwo.setText(editNameTwo);
            addOpponentDialogFragment.setText("Update");
        }

        getDialog().setTitle("Add Opponents");

        return rootView;
    }

    @Override
    public void onClick(View view) {
        mListener.returnOpponents(nameOne.getText().toString(), nameTwo.getText().toString());
        getDialog().dismiss();
    }
}
