package com.poogosoft.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SafePointTeamAdapter extends RecyclerView.Adapter<SafePointTeamAdapter.ViewHolder> {

    ArrayList<SafePointTeamItem> safePointTeamList = new ArrayList<>();

    //add Item
    public void addItem (SafePointTeamItem safePointTeamItem) { safePointTeamList.add(safePointTeamItem); }

    //clear
    public void clear() {
        safePointTeamList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public SafePointTeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_safe_point_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SafePointTeamAdapter.ViewHolder holder, int position) {
        holder.setItem(safePointTeamList.get(position));
    }

    @Override
    public int getItemCount() {
        return safePointTeamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textSafePointTeamDate;
        TextView textSafePointTeamTitle;
        TextView textSafePointTeamPoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSafePointTeamDate = itemView.findViewById(R.id.textSafePointTeamDate);
            textSafePointTeamTitle = itemView.findViewById(R.id.textSafePointTeamTitle);
            textSafePointTeamPoint = itemView.findViewById(R.id.textSafePointTeamPoint);
        }

        public void setItem(SafePointTeamItem safePointTeamItem) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd일 HH시");

            textSafePointTeamDate.setText(simpleDateFormat.format(safePointTeamItem.date));
            textSafePointTeamTitle.setText(safePointTeamItem.title);
            textSafePointTeamPoint.setText(safePointTeamItem.point);

        }
    }
}
