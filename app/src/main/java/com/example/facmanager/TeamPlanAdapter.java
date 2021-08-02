package com.example.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamPlanAdapter extends RecyclerView.Adapter<TeamPlanAdapter.ViewHolder> {

    ArrayList<TeamPlanItem> teamPlanItemList = new ArrayList<>();

    public void addItem (TeamPlanItem teamPlanItem) {
        teamPlanItemList.add(teamPlanItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_plan2, parent, false);
        return new TeamPlanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(teamPlanItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return teamPlanItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTaskSerialNum2;
        TextView txtTaskLocation2;
        TextView txtTaskState2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTaskSerialNum2 = itemView.findViewById(R.id.txtTaskSerialNum2);
            txtTaskLocation2 = itemView.findViewById(R.id.txtTaskLocation2);
            txtTaskState2 = itemView.findViewById(R.id.txtTaskState2);
        }

        public void setItem(TeamPlanItem teamPlanItem) {
            txtTaskSerialNum2.setText(teamPlanItem.serialNum);
            txtTaskLocation2.setText(teamPlanItem.location);
            txtTaskState2.setText(teamPlanItem.state);
        }
    }
}
