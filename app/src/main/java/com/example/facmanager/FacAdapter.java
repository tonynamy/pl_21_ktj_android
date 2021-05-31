package com.example.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FacAdapter extends RecyclerView.Adapter<FacAdapter.ViewHolder>{
    ArrayList<FacItem> facItemArrayList = new ArrayList<>();

    public void addItem(FacItem facItem) {
        facItemArrayList.add(facItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fac, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(facItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return facItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSerialNum;
        TextView txtProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSerialNum = itemView.findViewById(R.id.txtSerialNum);
            txtProgress = itemView.findViewById(R.id.txtProgress);
        }

        public  void setItem(FacItem item) {
            txtSerialNum.setText(item.getSerialNum());
            txtProgress.setText(item.getProgress());
        }
    }

}
