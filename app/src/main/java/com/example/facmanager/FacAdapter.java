package com.example.facmanager;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facmanager.models.Facility;

import java.util.ArrayList;

public class FacAdapter extends RecyclerView.Adapter<FacAdapter.ViewHolder>{
    ArrayList<Facility> facilities = new ArrayList<>();

    public void addItem(Facility facility) {
        facilities.add(facility);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fac, parent, false);
        return new ViewHolder(view);
   }

    public void clear() {
        facilities.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(facilities.get(position));
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSerialNum;
        TextView txtProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSerialNum = itemView.findViewById(R.id.txtSerialNum);
            txtProgress = itemView.findViewById(R.id.txtProgress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), FacilityActivity.class);
                    ((Activity) v.getContext()).startActivity(intent);
                }
            });
        }

        public void setItem(Facility item) {

            //해체완료dis_finished_at부터 설치시작started_at으로 (끝에서 앞으로) 날짜정보가 있는지 확인해나간다
            //해체완료dis_finished_at이 있으면 해체완료 없으면 앞으로
            //해체시작dis_started_at이 있으면 해체시작 없으면 앞으로
            //수정완료edit_finished_at이 있으면 수정완료 없으면 앞으로
            //수정시작edit_started_at이 있으면 수정시작 없으면 앞으로
            //승인완료finished_at이 있으면 승인완료 없으면 앞으로
            //설치중started_at이 있으면 설치중 없으면 설치전


            txtSerialNum.setText(item.serial);
            
            String progress;
            
            if(item.dis_finished_at != null) {
                progress = "해체완료";
            } else if(item.dis_started_at != null) {
                progress = "해체시작";
            }else if(item.edit_finished_at != null) {
                progress = "수정완료";
            }else if(item.edit_started_at != null) {
                progress = "수정시작";
            }else if(item.finished_at != null) {
                progress = "승인완료";
            }else if(item.started_at != null) {
                progress = "설치중";
            } else {
                progress = "설치전";
            }

            txtProgress.setText(progress);
        }
    }

}
