package com.example.mgriffin.dialog_fragments;



import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.public_references.PublicVars;


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
        nameTwo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mListener.returnOpponents(nameOne.getText().toString(), nameTwo.getText().toString());
                    getDialog().dismiss();
                }

                return false;
            }
        });
        addOpponentDialogFragment = (Button) rootView.findViewById(R.id.btn_add_opponents);
        addOpponentDialogFragment.setOnClickListener(this);

        if (editNameOne != null && editNameTwo != null) {
            nameOne.setText(editNameOne);
            nameTwo.setText(editNameTwo);
            addOpponentDialogFragment.setText(PublicVars.BUTTON_UPDATE);
        }

        getDialog().setTitle("Add Opponents");

        return rootView;
    }

    @Override
    public void onClick(View view) {
        mListener.returnOpponents(nameOne.getText().toString(), nameTwo.getText().toString());
        getDialog().dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        return super.onCreateDialog(savedInstanceState);
    }

//    @Override
//    public void onDestroyView() {
//        if (getDialog() != null && getRetainInstance())
//            getDialog().setDismissMessage(null);
//        super.onDestroyView();
//    }
}
