package com.example.facmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskPeriodAdapter extends RecyclerView.Adapter<TaskPeriodAdapter.ViewHolder> {

    ArrayList<TaskPeriodItem> taskPeriodItemList = new ArrayList<>();

    public void addItem (TaskPeriodItem taskPeriodItem) {
        taskPeriodItemList.add(taskPeriodItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_period, parent, false);
        return new TaskPeriodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(taskPeriodItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskPeriodItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPeriodSerialNum;
        TextView txtPeriodState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPeriodSerialNum = itemView.findViewById(R.id.txtPeriodSerialNum);
            txtPeriodState = itemView.findViewById(R.id.txtPeriodState);
        }

        public void setItem(TaskPeriodItem taskPeriodItem) {

            txtPeriodSerialNum.setText(taskPeriodItem.serialNum);
            txtPeriodState.setText(taskPeriodItem.state);

            //만료일이 지났을때는 빨간색으로 "만료" 표시
            if(txtPeriodState.getText() == "만료") {
                txtPeriodState.setTextColor(Color.RED);
            }

        }
    }
}
