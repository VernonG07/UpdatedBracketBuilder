package com.example.mgriffin.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.sax.TextElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mgriffin.pojos.Game;
import com.example.mgriffin.listviewex.R;

import java.util.List;

/**
 * Created by mgriffin on 10/23/2014.
 */
public class GameAdapter<T> extends ArrayAdapter<Game> {

    private Activity context;
    private int layout;
    private List<Game> data;
    private Typeface tf;

    public GameAdapter(Activity context, int layout, List<Game> data, Typeface tf) {
        super(context, layout, data);
        this.data = data;
        this.layout = layout;
        this.context = context;
        this.tf = tf;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        DataHolder dataHolder = null;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layout, parent, false);

            dataHolder = new DataHolder();
            dataHolder.gameNameHolder = (TextView) row.findViewById(R.id.tv_game_name);
            dataHolder.gameWinnerHolder = (TextView) row.findViewById(R.id.tv_winner_name);

            row.setTag(dataHolder);
        }
        else {
            dataHolder = (DataHolder) row.getTag();
        }

        Game game = data.get(position);

        if ( game.getWinner() != null)
            dataHolder.gameWinnerHolder.setText("Winner: " + game.getWinner());
        else
            dataHolder.gameWinnerHolder.setText("");

        dataHolder.gameNameHolder.setText(game.getGameName());

        return row;
    }

    static class DataHolder {
        TextView gameNameHolder;
        TextView gameWinnerHolder;
    }
}

