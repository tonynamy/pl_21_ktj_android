package com.example.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facmanager.models.Facility;

import java.util.ArrayList;

public class TaskPlanAdapter extends RecyclerView.Adapter<TaskPlanAdapter.ViewHolder> {

    ArrayList<Facility> facilityArrayList = new ArrayList<>();

    public void addItem (Facility Facility) {
        facilityArrayList.add(Facility);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        facilityArrayList.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(facilityArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return facilityArrayList.size();
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

        public void setItem(Facility facility) {

            txtTaskSerialNum.setText(facility.serial);
            txtTaskLocation.setText(facility.building + " " + facility.floor  + " " + facility.spot);
            txtTaskTeamName.setText(facility.taskPlan.team.name);

        }
    }
}
