package com.example.mgriffin.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mgriffin.pojos.MatchUp;
import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.public_references.PublicVars;

import java.util.List;

/**
 * Created by mgriffin on 10/23/2014.
 */
public class MatchUpAdapter<T> extends ArrayAdapter<MatchUp> {

    private Activity context;
    private int layout;
    private List<MatchUp> data;

    public MatchUpAdapter(Activity context, int layout, List<MatchUp> data) {
        super(context, layout, data);
        this.data = data;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        DataHolder dataHolder = null;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layout, parent, false);

            dataHolder = new DataHolder();
            dataHolder.name1Holder = (TextView) row.findViewById(R.id.tv_name1);
            dataHolder.name1WinHolder = (TextView) row.findViewById(R.id.tv_name1_w);
            dataHolder.name2Holder = (TextView) row.findViewById(R.id.tv_name2);
            dataHolder.name2WinHolder = (TextView) row.findViewById(R.id.tv_name2_w);

            row.setTag(dataHolder);
        }
        else {
            dataHolder = (DataHolder) row.getTag();
        }

        MatchUp matchUp = data.get(position);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "OptimusPrinceps.ttf");

        dataHolder.name1Holder.setTypeface(tf);
        dataHolder.name2Holder.setTypeface(tf);

        if (matchUp.getTeamOneId() == matchUp.getWinnerId()) {
            dataHolder.name1Holder.setText(matchUp.getTeamOneName());
            dataHolder.name1Holder.setTextColor(Color.parseColor("#ff18af20"));
//            dataHolder.name1WinHolder.setText( "(winner)");
            dataHolder.name2Holder.setText(matchUp.getTeamTwoName());
            dataHolder.name2Holder.setTextColor(-1979711488);
            dataHolder.name2Holder.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            dataHolder.name1Holder.setPaintFlags(dataHolder.name1Holder.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dataHolder.name2WinHolder.setText("");
        } else if (matchUp.getTeamTwoId() == matchUp.getWinnerId()) {
            dataHolder.name1Holder.setText(matchUp.getTeamOneName());
            dataHolder.name1Holder.setTextColor(-1979711488);
            dataHolder.name1Holder.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            dataHolder.name2WinHolder.setText( "(winner)");
            dataHolder.name2Holder.setText(matchUp.getTeamTwoName());
            dataHolder.name2Holder.setTextColor(Color.parseColor("#ff18af20"));
            dataHolder.name2Holder.setPaintFlags(dataHolder.name2Holder.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dataHolder.name1WinHolder.setText("");
        } else {
            dataHolder.name1Holder.setText(matchUp.getTeamOneName());
            dataHolder.name2Holder.setText(matchUp.getTeamTwoName());
            dataHolder.name1Holder.setTextColor(-1979711488);
            dataHolder.name2Holder.setTextColor(-1979711488);
            dataHolder.name1Holder.setPaintFlags(dataHolder.name1Holder.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dataHolder.name2Holder.setPaintFlags(dataHolder.name2Holder.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }






        return row;
    }

    static class DataHolder {
        TextView name1Holder;
        TextView name1WinHolder;
        TextView name2Holder;
        TextView name2WinHolder;
    }
}
