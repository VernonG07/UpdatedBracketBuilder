package com.example.mgriffin.dialog_fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mgriffin.listviewex.R;

/**
 * Created by mgriffin on 10/23/2014.
 */
public class AddBracketDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button addButton;
    private EditText name;

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void returnData(String d);
    }

    public AddBracketDialogFragment () {

    }

    private String gameName = null;

    public AddBracketDialogFragment (String gameName) {
        this.gameName = gameName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment_add_bracket, container, false);
        name = (EditText) rootView.findViewById(R.id.et_name);
        addButton = (Button) rootView.findViewById(R.id.btn_add);
        addButton.setOnClickListener(this);

        if (gameName != null) {
            name.setText(gameName);
            addButton.setText("Update");
        }

        getDialog().setTitle("Bracket Name");
        return rootView;
    }

    @Override
    public void onClick(View view) {
        String bracketName = name.getText().toString();

        if (!bracketName.equals(""))
            mListener.returnData(name.getText().toString());

        getDialog().dismiss();
    }
}
