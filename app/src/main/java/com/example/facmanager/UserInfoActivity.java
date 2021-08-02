package com.example.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {

    int level;

    RecyclerView recyclerUserInfo;
    UserInfoAdapter userInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //Intetn에서 가져오기
        level = getIntent().getIntExtra("level", 0);

        //뷰에서 불러오기
        recyclerUserInfo = findViewById(R.id.recyclerUserInfo);

        userInfoAdapter = new UserInfoAdapter();

        UserInfoItem userInfoItem = new UserInfoItem();
        userInfoItem.user_name = "김석훈";
        userInfoItem.user_birthday = "661205";
        userInfoItem.user_level = 2;
        userInfoAdapter.addItem(userInfoItem);

        userInfoItem = new UserInfoItem();
        userInfoItem.user_name = "조궁주";
        userInfoItem.user_birthday = "820527";
        userInfoItem.user_level = 2;
        userInfoAdapter.addItem(userInfoItem);

        userInfoItem = new UserInfoItem();
        userInfoItem.user_name = "이성민";
        userInfoItem.user_birthday = "820201";
        userInfoItem.user_level = 2;
        userInfoAdapter.addItem(userInfoItem);

        userInfoItem = new UserInfoItem();
        userInfoItem.user_name = "이재원";
        userInfoItem.user_birthday = "840819";
        userInfoItem.user_level = 2;
        userInfoAdapter.addItem(userInfoItem);

        userInfoItem = new UserInfoItem();
        userInfoItem.user_name = "전철웅";
        userInfoItem.user_birthday = "840822";
        userInfoAdapter.addItem(userInfoItem);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerUserInfo.setLayoutManager(layoutManager);
        recyclerUserInfo.setAdapter(userInfoAdapter);

        userInfoAdapter.setOnUserInfoClicked(new UserInfoAdapter.OnUserInfoClicked() {
            @Override
            public void onClick(View v, UserInfoItem userInfoItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_change_level, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                TextView textLevelUserName = view.findViewById(R.id.textLevelUserName);
                TextView textLevelAdmin = view.findViewById(R.id.textLevelAdmin);
                TextView textLevelManager = view.findViewById(R.id.textLevelManager);
                TextView textLevelTeamLeader = view.findViewById(R.id.textLevelTeamLeader);
                TextView textDialogCancel7 = view.findViewById(R.id.textDialogCancel7);

                if(level < 4) {
                    textLevelAdmin.setVisibility(View.GONE);
                }

                textLevelUserName.setText(userInfoItem.user_name + " 직원등급");

                switch(userInfoItem.user_level) {
                    case 1:
                        textLevelTeamLeader.setVisibility(View.GONE);
                        break;
                    case 2:
                        textLevelManager.setVisibility(View.GONE);
                        break;
                }

                dialog.show();



                //취소 선택시
                textDialogCancel7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });



    }
}