package com.example.facmanager;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.ViewHolder> {

    interface OnAttendBtnClickListener {

        public void onClick(View v, AttendItem attendItem, int new_type);

    }

    OnAttendBtnClickListener onAttendBtnClickListener;
    ArrayList<AttendItem> attendItemArrayList = new ArrayList<>();

    public AttendAdapter(OnAttendBtnClickListener onAttendBtnClickListener) {
        this.onAttendBtnClickListener = onAttendBtnClickListener;
    }

    public void addItem(AttendItem attendItem) {
        attendItemArrayList.add(attendItem);
    }

    public void clear() {
        attendItemArrayList.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attend, parent, false);
        return new ViewHolder(view, onAttendBtnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.setItem(attendItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return attendItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OnAttendBtnClickListener onAttendBtnClickListener;
        AttendItem attendItem;
        TextView txtName;
        TextView txtAttend;
        Button btnAttend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAttend = itemView.findViewById(R.id.txtAttend);
            btnAttend = itemView.findViewById(R.id.btnAttend);
        }

        public ViewHolder(@NonNull View itemView, OnAttendBtnClickListener onAttendBtnClickListener) {

            this(itemView);


            btnAttend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int new_type;
                    int type = attendItem.getType();

                    switch (type) {
                        case AttendanceRecord.NOT_ATTEND :
                            new_type = AttendanceRecord.ATTENDED;
                            break;
                        case AttendanceRecord.ATTENDED:
                            new_type = AttendanceRecord.LEAVED_WORK;
                            break;
                        default:
                            new_type = AttendanceRecord.NOT_ATTEND;
                            break;
                    }

                    onAttendBtnClickListener.onClick(v, attendItem, new_type);
                }
            });
        }

        public void setItem(AttendItem attendItem) {
            this.attendItem = attendItem;
            txtName.setText(attendItem.getName());
            int type = attendItem.getType();
            String typeText = "";
            String buttonText = "";
            int typeColor;
            btnAttend.setVisibility(View.VISIBLE);
            switch (type) {
                case AttendanceRecord.NOT_ATTEND :
                    typeText = "출근전";
                    buttonText = "출근";
                    typeColor = Color.GRAY;
                    break;
                case AttendanceRecord.ATTENDED:
                    typeText = "출근";
                    buttonText = "퇴근";
                    typeColor = Color.BLUE;
                    break;
                case AttendanceRecord.LEAVED_WORK:
                    typeText = "퇴근";
                    typeColor = Color.GRAY;
                    btnAttend.setVisibility(View.INVISIBLE);
                    break;
                default:
                    typeText = "알 수 없음";
                    typeColor = Color.RED;
                    break;
            }
            btnAttend.setText(buttonText);
            txtAttend.setText(typeText);
            txtAttend.setTextColor(typeColor);
        }
    }

}
