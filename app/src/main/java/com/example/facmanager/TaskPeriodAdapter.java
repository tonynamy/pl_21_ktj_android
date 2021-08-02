package com.example.facmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facmanager.models.Facility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskPeriodAdapter extends RecyclerView.Adapter<TaskPeriodAdapter.ViewHolder> {

    ArrayList<Facility> facilityArrayList = new ArrayList<>();

    public void addItem (Facility facility) {
        facilityArrayList.add(facility);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_period, parent, false);
        return new TaskPeriodAdapter.ViewHolder(view);
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

        TextView txtPeriodSerialNum;
        TextView txtPeriodState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPeriodSerialNum = itemView.findViewById(R.id.txtPeriodSerialNum);
            txtPeriodState = itemView.findViewById(R.id.txtPeriodState);
        }

        public void setItem(Facility facility) {

            txtPeriodSerialNum.setText(facility.serial);

            if(facility.expired_at != null && facility.expired_at.after(Calendar.getInstance().getTime())) {
                txtPeriodState.setText("만료");
                txtPeriodState.setTextColor(Color.RED);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd.");
                String dateString = simpleDateFormat.format(facility.expired_at);
                txtPeriodState.setText(dateString);
            }

        }
    }
}
