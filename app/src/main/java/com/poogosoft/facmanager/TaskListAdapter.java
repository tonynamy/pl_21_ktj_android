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
        ImageView imgPlanCircle2;
        TextView textTaskPeriod;
        LinearLayout layoutTaskPlan;
        TextView txtTaskLocation;
        TextView txtTaskTeamName;
        TextView txtTaskPlan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTaskSerial = itemView.findViewById(R.id.txtTaskSerial);
            imgPlanCircle2 = itemView.findViewById(R.id.imgPlanCircle2);
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
            //승인번호
            txtTaskSerial.setText(taskListItem.serial);

            //관리자 작업조회시는 팀이름이 나옵니다
            if(taskListItem.teamName != null) {
                txtTaskTeamName.setText(taskListItem.teamName);
                txtTaskTeamName.setVisibility(View.VISIBLE);
                txtTaskPlan.setVisibility(View.GONE);
            } else {
                txtTaskTeamName.setVisibility(View.GONE);
                txtTaskPlan.setVisibility(View.VISIBLE);
            }

            //팀 작업조회시에는 계획내용이 나옵니다
            if(taskListItem.taskplan != 0) {
                imgPlanCircle2.setVisibility(View.VISIBLE);
                String stringTaskPlan = "";
                switch (taskListItem.taskplan) {
                    case 1:
                        stringTaskPlan = "설치예정";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#ff5544"));
                        break;
                    case 2:
                        stringTaskPlan = "수정예정";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#77bb00"));
                        break;
                    case 3:
                        stringTaskPlan = "해체예정";
                        imgPlanCircle2.setColorFilter(Color.parseColor("#888899"));
                        break;
                }
                txtTaskPlan.setText(stringTaskPlan);
            } else {
                layoutTaskPlan.setVisibility(View.GONE);
            }

            //설치위치
            if(taskListItem.location != null){
                txtTaskLocation.setText(taskListItem.location);
            }else{
                txtTaskLocation.setVisibility(View.INVISIBLE);
            }

            //기간만료 임박
            if(taskListItem.expired_date != null){
                imgPlanCircle2.setVisibility(View.GONE);
                Date now = Calendar.getInstance().getTime();
                if(taskListItem.expired_date != null && now.after(taskListItem.expired_date)) {
                    textTaskPeriod.setTextColor(Color.RED);
                    textTaskPeriod.setText("만료");
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("~ yyyy. MM. dd");
                    String dateString = simpleDateFormat.format(taskListItem.expired_date);
                    textTaskPeriod.setTextColor(Color.BLACK);
                    textTaskPeriod.setText(dateString);
                }
            } else {
                textTaskPeriod.setVisibility(View.GONE);
            }

        }
    }
}
