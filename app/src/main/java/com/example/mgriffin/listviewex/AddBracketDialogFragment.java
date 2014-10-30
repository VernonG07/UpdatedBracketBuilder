package com.example.mgriffin.listviewex;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment_add_bracket, container, false);
        name = (EditText) rootView.findViewById(R.id.et_name);
        addButton = (Button) rootView.findViewById(R.id.btn_add);
        addButton.setOnClickListener(this);
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
