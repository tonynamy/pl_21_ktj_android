package com.poogosoft.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductivityTeamAdapter extends RecyclerView.Adapter<ProductivityTeamAdapter.ViewHolder> {

    ArrayList<ProductivityTeamItem> productivityTeamList = new ArrayList<>();

    //add Item
    public void addItem (ProductivityTeamItem productivityTeamItem) { productivityTeamList.add(productivityTeamItem); }

    //clear
    public void clear() {
        productivityTeamList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ProductivityTeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productivity_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductivityTeamAdapter.ViewHolder holder, int position) {
        holder.setItem(productivityTeamList.get(position));
    }

    @Override
    public int getItemCount() {
        return productivityTeamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTeamProductivityName;
        LinearLayout layoutTeamProductivity;
        TextView textTeamProductivity;
        TextView textTeamProductivityResult;
        LinearLayout layoutTeamProductivityManday;
        TextView textTeamProductivityType;
        TextView textTeamProductivityManday;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTeamProductivityName = itemView.findViewById(R.id.textTeamProductivityName);
            layoutTeamProductivity = itemView.findViewById(R.id.layoutTeamProductivity);
            textTeamProductivity = itemView.findViewById(R.id.textTeamProductivity);
            textTeamProductivityResult = itemView.findViewById(R.id.textProductivityTeamResult);
            layoutTeamProductivityManday = itemView.findViewById(R.id.layoutTeamProductivityManday);
            textTeamProductivityType = itemView.findViewById(R.id.textTeamProductivityType);
            textTeamProductivityManday = itemView.findViewById(R.id.textTeamProductivityManday);
        }

        public void setItem(ProductivityTeamItem productivityTeamItem) {

            textTeamProductivityName.setText(productivityTeamItem.name);
            textTeamProductivity.setText(productivityTeamItem.size + "/" + productivityTeamItem.manday);
            Double productivityResult = Double.valueOf(productivityTeamItem.size)/Double.valueOf(productivityTeamItem.manday);
            textTeamProductivityResult.setText(String.format("%.2f", productivityResult));
            String productivityType = "";
            switch (productivityTeamItem.type) {
                case 1 :
                    productivityType = "설치작업";
                    break;
                case 2:
                    productivityType = "수정작업";
                    break;
                case 3:
                    productivityType = "해체작업";
                    break;
                case 4:
                    productivityType = "기타작업";
                    break;
            }
            textTeamProductivityType.setText(productivityType);
            textTeamProductivityManday.setText(productivityTeamItem.manday);

            if(productivityTeamItem.type == 1) {
                layoutTeamProductivity.setVisibility(View.VISIBLE);
                layoutTeamProductivityManday.setVisibility(View.GONE);
            } else {
                layoutTeamProductivity.setVisibility(View.GONE);
                layoutTeamProductivityManday.setVisibility(View.VISIBLE);
            }
        }
    }
}
