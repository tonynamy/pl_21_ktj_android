package com.example.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskPlanAdapter extends RecyclerView.Adapter<TaskPlanAdapter.ViewHolder> {

    ArrayList<TaskPlanItem> taskPlanItemList = new ArrayList<>();


    public void addItem (TaskPlanItem taskPlanItem) {
        taskPlanItemList.add(taskPlanItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(taskPlanItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskPlanItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTaskSerialNum;
        TextView txtTaskLocation;
        TextView txtTaskTeamName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTaskSerialNum = itemView.findViewById(R.id.txtTaskSerialNum);
            txtTaskLocation = itemView.findViewById(R.id.txtTaskLocation);
            txtTaskTeamName = itemView.findViewById(R.id.txtTaskTeamName);
        }

        public void setItem(TaskPlanItem taskPlanItem) {
            txtTaskSerialNum.setText(taskPlanItem.serialNum);
            txtTaskLocation.setText(taskPlanItem.location);
            txtTaskTeamName.setText(taskPlanItem.teamName);
        }
    }
}
