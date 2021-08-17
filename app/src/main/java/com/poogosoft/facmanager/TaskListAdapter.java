package com.poogosoft.facmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onClick(View v, String facility_id);
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    ArrayList<TaskListItem> taskListItemList = new ArrayList<>();

    //add Item
    public void addItem (TaskListItem taskListItem) {
        taskListItemList.add(taskListItem);
    }

    //clear
    public void clear() {
        taskListItemList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(taskListItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskListItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTaskSerial;
        TextView textTaskPeriod;
        LinearLayout layoutTaskPlan;
        TextView txtTaskLocation;
        TextView txtTaskTeamName;
        TextView txtTaskPlan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTaskSerial = itemView.findViewById(R.id.txtTaskSerial);
            textTaskPeriod = itemView.findViewById(R.id.textTaskPeriod);
            layoutTaskPlan = itemView.findViewById(R.id.layoutTaskPlan);
            txtTaskLocation = itemView.findViewById(R.id.txtTaskLocation);
            txtTaskTeamName = itemView.findViewById(R.id.txtTaskTeamName);
            txtTaskPlan = itemView.findViewById(R.id.txtTaskPlan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, taskListItemList.get(getAdapterPosition()).id);
                }
            });
        }

        public void setItem(TaskListItem taskListItem) {

            txtTaskSerial.setText(taskListItem.serial);
            if(taskListItem.teamName != null || taskListItem.taskplan != 0) {
                if(taskListItem.location != null){
                    txtTaskLocation.setText(taskListItem.location);
                }else{
                    txtTaskLocation.setVisibility(View.INVISIBLE);
                }

                if(taskListItem.taskplan != 0) {
                    String stringTaskPlan = "";
                    switch (taskListItem.taskplan) {
                        case 1:
                            stringTaskPlan = "설치예정";
                            break;
                        case 2:
                            stringTaskPlan = "수정예정";
                            break;
                        case 3:
                            stringTaskPlan = "해체예정";
                            break;
                    }
                    txtTaskPlan.setText(stringTaskPlan);
                    txtTaskTeamName.setVisibility(View.GONE);

                } else {
                    txtTaskPlan.setVisibility(View.GONE);
                    txtTaskTeamName.setText(taskListItem.teamName);
                }

            } else {
                layoutTaskPlan.setVisibility(View.GONE);
            }

            if(taskListItem.expired_date != null){
                Date now = Calendar.getInstance().getTime();
                if(taskListItem.expired_date != null && now.after(taskListItem.expired_date)) {
                    textTaskPeriod.setText("만료");
                    textTaskPeriod.setTextColor(Color.RED);
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("~ yyyy. MM. dd");
                    String dateString = simpleDateFormat.format(taskListItem.expired_date);
                    textTaskPeriod.setText(dateString);
                }
            } else {
                textTaskPeriod.setVisibility(View.GONE);
            }

        }
    }
}
