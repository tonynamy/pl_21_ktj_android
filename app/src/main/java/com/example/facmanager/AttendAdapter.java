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
    ArrayList<AttendItem> attendItemArrayList = new ArrayList<>();

    public void addItem(AttendItem attendItem) {
        attendItemArrayList.add(attendItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attend, parent, false);
        return new ViewHolder(view);
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
        TextView txtName;
        TextView txtAttend;
        Button btnAttend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAttend = itemView.findViewById(R.id.txtAttend);
            btnAttend = itemView.findViewById(R.id.btnAttend);

            btnAttend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtAttend.setText("출근");
                    txtAttend.setTextColor(Color.BLUE);
                }
            });
        }

        public void setItem(AttendItem attendItem) {
            txtName.setText(attendItem.getTxtName());
            txtAttend.setText(attendItem.getTxtAttend());
        }
    }

}
