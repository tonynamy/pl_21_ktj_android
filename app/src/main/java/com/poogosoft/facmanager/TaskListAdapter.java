package com.poogosoft.facmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        void onClick(View v, String id, int type);
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    Boolean is_expired_list = false;
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

        LinearLayout layoutSerialDate;
        TextView txtTaskSerial;
        ImageView imgPlanCircle2;
        LinearLayout layoutTaskPlan;
        TextView textTaskPeriod;
        TextView txtTaskLocation;
        TextView txtTaskTeamName;
        TextView txtTaskPlan;
        TextView txtTaskState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutSerialDate = itemView.findViewById(R.id.layoutSerialDate);
            txtTaskSerial = itemView.findViewById(R.id.txtTaskSerial);
            imgPlanCircle2 = itemView.findViewById(R.id.imgPlanCircle2);
            layoutTaskPlan = itemView.findViewById(R.id.layoutTaskPlan);
            textTaskPeriod = itemView.findViewById(R.id.textTaskPeriod);
            txtTaskLocation = itemView.findViewById(R.id.txtTaskLocation);
            txtTaskTeamName = itemView.findViewById(R.id.txtTaskTeamName);
            txtTaskPlan = itemView.findViewById(R.id.txtTaskPlan);
            txtTaskState = itemView.findViewById(R.id.txtTaskState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, taskListItemList.get(getAdapterPosition()).id, taskListItemList.get(getAdapterPosition()).taskplan);
                }
            });
        }

        public void setItem(TaskListItem taskListItem) {
            //????????????
            if(taskListItem.serial != null && !taskListItem.serial.isEmpty()) {
                txtTaskSerial.setText(taskListItem.serial);
            } else {
                layoutSerialDate.setVisibility(View.GONE);
            }

            //???????????? ??????
            if(is_expired_list){
                layoutTaskPlan.setVisibility(View.GONE);
                imgPlanCircle2.setVisibility(View.GONE);
                Date now = Calendar.getInstance().getTime();
                if(taskListItem.expired_date != null && now.after(taskListItem.expired_date)) {
                    textTaskPeriod.setTextColor(Color.RED);
                    textTaskPeriod.setText("??????");
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("~ yyyy. MM. dd");
                    String dateString = simpleDateFormat.format(taskListItem.expired_date);
                    textTaskPeriod.setTextColor(Color.BLACK);
                    textTaskPeriod.setText(dateString);
                }
            } else {
                textTaskPeriod.setVisibility(View.GONE);
            }

            //????????????
            if(taskListItem.location != null && !taskListItem.location.isEmpty()){
                txtTaskLocation.setVisibility(View.VISIBLE);
                txtTaskLocation.setText(taskListItem.location);
            } else {
                txtTaskLocation.setVisibility(View.INVISIBLE);
            }

            //???????????????, ??????????????????, ??????????????????
            if(taskListItem.taskplan > 0) {
                imgPlanCircle2.setVisibility(View.VISIBLE);
                String stringTaskPlan = "";
                switch (taskListItem.taskplan) {
                    case 1:
                        stringTaskPlan = "????????????";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#ff5544"));
                        break;
                    case 2:
                        stringTaskPlan = "????????????";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#77bb00"));
                        break;
                    case 3:
                        stringTaskPlan = "????????????";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#888899"));
                        break;
                    case 4:
                        stringTaskPlan = "????????????";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#888899"));
                        break;
                }

                if(taskListItem.team_name != null) {
                    txtTaskTeamName.setText(taskListItem.team_name);
                    txtTaskTeamName.setVisibility(View.VISIBLE);
                    txtTaskPlan.setVisibility(View.GONE);
                    txtTaskState.setVisibility(View.GONE);
                } else {
                    txtTaskPlan.setText(stringTaskPlan);
                    txtTaskPlan.setVisibility(View.VISIBLE);
                    txtTaskTeamName.setVisibility(View.GONE);
                    txtTaskState.setVisibility(View.GONE);
                }
            } else {
                imgPlanCircle2.setVisibility(View.GONE);
                txtTaskTeamName.setVisibility(View.GONE);
                txtTaskPlan.setVisibility(View.GONE);

                if (taskListItem.progress != null) {
                    txtTaskState.setText(taskListItem.progress);
                    txtTaskState.setVisibility(View.VISIBLE);
                } else {
                    txtTaskState.setVisibility(View.GONE);
                }
            }

        }
    }
}
