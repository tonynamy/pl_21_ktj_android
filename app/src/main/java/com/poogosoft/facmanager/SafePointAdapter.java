package com.poogosoft.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SafePointAdapter extends RecyclerView.Adapter<SafePointAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onClick(View v, String team_id);
    }
    SafePointAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    ArrayList<SafePointItem> safePointList = new ArrayList<>();

    //add Item
    public void addItem (SafePointItem safePointItem) { safePointList.add(safePointItem); }

    //clear
    public void clear() {
        safePointList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public SafePointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_safe_point, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SafePointAdapter.ViewHolder holder, int position) {
        holder.setItem(safePointList.get(position));
    }

    @Override
    public int getItemCount() {
        return safePointList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textSafePointTeamName;
        TextView textSafePoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSafePointTeamName = itemView.findViewById(R.id.textSafePointTeamName);
            textSafePoint = itemView.findViewById(R.id.textSafePoint);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, safePointList.get(getAdapterPosition()).team_id);
                }
            });

        }

        public void setItem(SafePointItem safePointItem) {

            textSafePointTeamName.setText(safePointItem.team_name);
            textSafePoint.setText(safePointItem.point);

        }
    }
}
