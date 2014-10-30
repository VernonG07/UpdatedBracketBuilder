package com.example.mgriffin.listviewex;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mgriffin on 10/23/2014.
 */
public class MatchUpAdapter<T> extends ArrayAdapter<GameMatchUp> {

    private Activity context;
    private int layout;
    private List<GameMatchUp> data;
    private TextView tv_name1;
    private TextView tv_name2;

    public MatchUpAdapter(Activity context, int layout, List<GameMatchUp> data) {
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
            dataHolder.name2Holder = (TextView) row.findViewById(R.id.tv_name2);

            row.setTag(dataHolder);
        }
        else {
            dataHolder = (DataHolder) row.getTag();
        }

        GameMatchUp gmu = data.get(position);

        //TODO: This is bad code, clean up.
        if (gmu.getTeamOne().getWinner()) {
            dataHolder.name1Holder.setText(gmu.getTeamOne().getTeamName() + " W");
            dataHolder.name2Holder.setText(gmu.getTeamTwo().getTeamName());
        } else if (gmu.getTeamTwo().getWinner()) {
            dataHolder.name2Holder.setText(gmu.getTeamTwo().getTeamName() + " W");
            dataHolder.name1Holder.setText(gmu.getTeamOne().getTeamName());
        } else {
            dataHolder.name1Holder.setText(gmu.getTeamOne().getTeamName());
            dataHolder.name2Holder.setText(gmu.getTeamTwo().getTeamName());
        }

        return row;
    }

    static class DataHolder {
        TextView name1Holder;
        TextView name2Holder;
    }
}
