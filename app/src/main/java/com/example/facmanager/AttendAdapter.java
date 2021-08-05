package com.example.facmanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.ViewHolder> {

    interface OnItemClickListener {
        public void onClick(View v, AttendItem attendItem);
    }
    OnItemClickListener onItemClickListener;

    interface OnAttendBtnClickListener {
        public void onClick(View v, AttendItem attendItem, int new_type);
    }
    OnAttendBtnClickListener onAttendBtnClickListener;

    interface OnAttendanceUpdateListner {
        public void onUpdate();
    }
    OnAttendanceUpdateListner onAttendanceUpdateListner;


    ArrayList<AttendItem> attendItemArrayList = new ArrayList<>();
    //String team_id;

    public AttendAdapter(OnItemClickListener onItemClickListener, OnAttendBtnClickListener onAttendBtnClickListener, OnAttendanceUpdateListner onAttendanceUpdateListner) {
        this.onItemClickListener = onItemClickListener;
        this.onAttendBtnClickListener = onAttendBtnClickListener;
        this.onAttendanceUpdateListner = onAttendanceUpdateListner;
    }


    //clear
    public void clear() {
        attendItemArrayList.clear();
    }

    //add Item
    public void addItem(AttendItem attendItem) {
        attendItemArrayList.add(attendItem);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attend, parent, false);
        return new ViewHolder(view, onItemClickListener, onAttendBtnClickListener, onAttendanceUpdateListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(attendItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return attendItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OnItemClickListener onItemClickListener;
        OnAttendBtnClickListener onAttendBtnClickListener;
        OnAttendanceUpdateListner onAttendanceUpdateListner;

        AttendItem attendItem;
        TextView txtName;
        TextView txtAttend;
        Button btnAttend;

        //public ViewHolder(@NonNull View itemView) { this(itemView, null, null, null); }

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener, OnAttendBtnClickListener onAttendBtnClickListener, OnAttendanceUpdateListner onAttendanceUpdateListener) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAttend = itemView.findViewById(R.id.txtAttend);
            btnAttend = itemView.findViewById(R.id.btnAttend);

            //Item 클릭
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListener.onClick(v , attendItem);

                }
            });

            btnAttend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int new_type;
                    int type = attendItem.getType();

                    switch (type) {
                        case AttendanceRecord.NOT_ATTEND :
                            new_type = AttendanceRecord.ATTENDED;
                            break;
                        case AttendanceRecord.ATTENDED :
                            new_type = AttendanceRecord.LEAVED_WORK;
                            break;
                        default :
                            new_type = AttendanceRecord.NOT_ATTEND;
                            break;
                    }

                    onAttendBtnClickListener.onClick(v, attendItem, new_type);
                }
            });

            this.onItemClickListener = onItemClickListener;
            this.onAttendBtnClickListener = onAttendBtnClickListener;
            this.onAttendanceUpdateListner = onAttendanceUpdateListener;
        }

        public void setItem(AttendItem attendItem) {
            this.attendItem = attendItem;

            txtName.setText(attendItem.getName());
            btnAttend.setVisibility(View.VISIBLE);

            int type = attendItem.getType();
            String typeText = "";
            String buttonText = "";
            int typeColor;
            switch (type) {
                case  AttendanceRecord.NOT_ATTEND :
                    typeText = "출근전";
                    buttonText = "출근";
                    typeColor = Color.GRAY;
                    break;
                case  AttendanceRecord.ATTENDED :
                    typeText = "출근";
                    buttonText = "퇴근";
                    typeColor = Color.BLUE;
                    break;
                case  AttendanceRecord.LEAVED_WORK :
                    typeText = "퇴근";
                    typeColor = Color.RED;
                    btnAttend.setVisibility(View.INVISIBLE);
                    break;
                default:
                    typeText = "알수없음";
                    typeColor = Color.RED;
                    break;
            }
            txtAttend.setText(typeText);
            txtAttend.setTextColor(typeColor);
            btnAttend.setText(buttonText);
        }
    }

}
