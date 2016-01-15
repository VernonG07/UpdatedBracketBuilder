package com.griffin.bracketbuilder.v2.CardAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mgriffin.listviewex.R;
import com.example.mgriffin.pojos.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 1/14/2016.
 */
public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.ViewHolder> {

    List<Game> list = new ArrayList<>();
    private Context context;
    OnItemClickListener mItemClickListener;
    public GameCardAdapter(List<Game> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Game = getItem(position);
        holder.tvGameName.setText(list.get(position).getGameName());
        holder.tvWinnerName.setText(list.get(position).getWinner());
        holder.tvTimeCreated.setText(list.get(position).getDateCreated());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvGameName;
        TextView tvWinnerName;
        TextView tvTimeCreated;
        Game Game;
        public ViewHolder(View itemView) {
            super(itemView);
            tvGameName = (TextView) itemView.findViewById(R.id.tv_game_name);
            tvWinnerName = (TextView) itemView.findViewById(R.id.tv_winner_name);
            tvTimeCreated = (TextView) itemView.findViewById(R.id.tv_timeCreated);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Game getItem(int i) {
        return list.get(i);
    }
}
