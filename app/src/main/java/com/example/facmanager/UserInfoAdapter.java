package com.example.facmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {

    interface OnUserInfoClicked {
        void onClick(View v, UserInfoItem userInfoItem);
    }
    OnUserInfoClicked onUserInfoClicked;

    public void setOnUserInfoClicked(OnUserInfoClicked onUserInfoClicked) {
        this.onUserInfoClicked = onUserInfoClicked;
    }

    ArrayList<UserInfoItem> userInfoList = new ArrayList<>();

    //clear
    public void clear() {
        userInfoList.clear();
    }

    //add Item
    public void addItem(UserInfoItem userInfoItem) {
        userInfoList.add(userInfoItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(userInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textUserName;
        TextView textUserBirthday;
        TextView textUserLevel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            textUserBirthday = itemView.findViewById(R.id.textUserBirthday);
            textUserLevel = itemView.findViewById(R.id.textUserLevel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onUserInfoClicked.onClick(v, userInfoList.get(getAdapterPosition()));
                }
            });

        }

        public void setItem(UserInfoItem userInfoItem) {
            textUserName.setText(userInfoItem.user_name);
            textUserBirthday.setText(userInfoItem.user_birthday);

            String user_level = "";
            switch (userInfoItem.user_level) {
                case 1:
                    user_level = "팀장";
                    break;
                case 2:
                    user_level = "관리자";
                    break;
                case 3:
                case 4:
                    user_level = "최고관리자";
                    break;
                default:
                    user_level = "대기자";
                    break;
            }

           textUserLevel.setText(user_level);

            if(user_level.equals("대기자")) {
                textUserName.setTextColor(Color.BLUE);
                textUserBirthday.setTextColor(Color.BLUE);
                textUserLevel.setTextColor(Color.BLUE);
            }
            else {
                textUserName.setTextColor(Color.BLACK);
                textUserBirthday.setTextColor(Color.BLACK);
                textUserLevel.setTextColor(Color.BLACK);
            }
        }
    }
}
