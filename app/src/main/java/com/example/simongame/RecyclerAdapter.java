package com.example.simongame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerAdapterViewHolder>{

    public  ArrayList<Player> players = null;
    private Context context;

    public RecyclerAdapter(ArrayList<Player> players, Context context) {
        this.players = players;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rawitem= layoutInflater.inflate(R.layout.rawitem, parent, false);
        RecyclerAdapterViewHolder recyclerAdapterViewHolder = new RecyclerAdapterViewHolder(rawitem);
        return recyclerAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterViewHolder holder, int position) {
        Player player = players.get(position);
        holder.bindData(player,context);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class RecyclerAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView playerName;
        private TextView  score;
        private TextView  date;
        private View view;
        public RecyclerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
        public void bindData(final Player player, Context context) {
            playerName = view.findViewById(R.id.playerName);
            score  = view.findViewById(R.id.score);
            date   = view.findViewById(R.id.date);
            playerName.setText(player.getName());
            score.setText(player.getScore());
            date.setText(player.getDate());
        }
    }

}
