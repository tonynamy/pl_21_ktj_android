package com.poogosoft.facmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poogosoft.facmanager.models.Place;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    interface OnPlaceClicked {
        public void onClick(View v, Place place);
    }

    OnPlaceClicked onPlaceClicked;

    public void setOnPlaceItemClicked(OnPlaceClicked onPlaceClicked) {
        this.onPlaceClicked = onPlaceClicked;
    }

    ArrayList<Place> placeArrayList = new ArrayList<>();

    //add Item
    public void addItem(Place place) {
        placeArrayList.add(place);
    }

    //clear
    public void clear() { placeArrayList.clear(); }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(placeArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPlaceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlaceName = itemView.findViewById(R.id.txtPlaceName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Place place = placeArrayList.get(getAdapterPosition());
                    onPlaceClicked.PonClick(v, place);
                }
            });
        }

        public void setItem(Place place) {
            txtPlaceName.setText(place.name);
        }
    }
}
