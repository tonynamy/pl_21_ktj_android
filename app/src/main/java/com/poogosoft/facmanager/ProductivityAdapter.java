package com.poogosoft.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductivityAdapter extends RecyclerView.Adapter<ProductivityAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onClick(View v, String team_id);
    }
    ProductivityAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    ArrayList<ProductivityItem> productivityList = new ArrayList<>();

    //add Item
    public void addItem (ProductivityItem productivityItem) { productivityList.add(productivityItem); }

    //clear
    public void clear() {
        productivityList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productivity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(productivityList.get(position));
    }

    @Override
    public int getItemCount() {
        return productivityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textProductivityTeamName;
        TextView textProductivity;
        TextView textProductivityManday;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductivityTeamName = itemView.findViewById(R.id.textProductivityTeamName);
            textProductivity = itemView.findViewById(R.id.textProductivity);
            textProductivityManday = itemView.findViewById(R.id.textProductivityManday);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, productivityList.get(getAdapterPosition()).team_id);
                }
            });

        }

        public void setItem(ProductivityItem productivityItem) {

            textProductivityTeamName.setText(productivityItem.team_name);
            textProductivity.setText(productivityItem.productivity_cube + "+" + productivityItem.productivity_danger);
            textProductivityManday.setText(productivityItem.manday);

        }
    }
}
