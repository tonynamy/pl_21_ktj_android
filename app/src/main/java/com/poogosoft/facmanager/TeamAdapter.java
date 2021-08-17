package com.poogosoft.facmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poogosoft.facmanager.models.Facility;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    interface OnItemClickListener {
        public void onClick(View v, String team_id, int position);
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    ArrayList<TeamItem> teamItemArrayList = new ArrayList<>();
    Boolean changeTeamMode = false;
    String user_id = "";

    //add Item
    public void addItem(TeamItem teamItem) {
        teamItemArrayList.add(teamItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public TeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
        holder.onBind(teamItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return teamItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTeamName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTeamName = itemView.findViewById(R.id.txtTeamName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListener.onClick(v, teamItemArrayList.get(getAdapterPosition()).teamId, getAdapterPosition());

                }
            });
        }

        public void onBind(TeamItem teamItem) {
            txtTeamName.setText(teamItem.teamName);
        }
    }
}
