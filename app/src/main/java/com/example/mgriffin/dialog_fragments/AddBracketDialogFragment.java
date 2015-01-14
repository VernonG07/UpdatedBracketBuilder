package com.example.mgriffin.dialog_fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.public_references.PublicVars;

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
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ( actionId == EditorInfo.IME_ACTION_DONE){
                    String bracketName = name.getText().toString();

                    if (!bracketName.equals(PublicVars.STRING_EMPTY))
                        mListener.returnData(name.getText().toString());

                    getDialog().dismiss();
                }
                return false;
            }
        });
        addButton = (Button) rootView.findViewById(R.id.btn_add);
        addButton.setOnClickListener(this);

        if (gameName != null) {
            name.setText(gameName);
            addButton.setText(PublicVars.BUTTON_UPDATE);
        }

        getDialog().setTitle("Bracket Name");
        return rootView;
    }

    @Override
    public void onClick(View view) {
        String bracketName = name.getText().toString();

        if (!bracketName.equals(PublicVars.STRING_EMPTY))
            mListener.returnData(name.getText().toString());

        getDialog().dismiss();
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
