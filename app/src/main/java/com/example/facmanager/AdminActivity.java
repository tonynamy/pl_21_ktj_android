package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    int level;

    Button btnPlaceSet;
    Button btnUserLevelSet;
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Intent에서 Extra 가져오기
        level = getIntent().getIntExtra("level", 0);

        //뷰 가져오기
        btnPlaceSet = findViewById(R.id.btnPlaceSet);
        btnUserLevelSet = findViewById(R.id.btnUserLevelSet);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        //권한에 따른 버튼 활성화
        if(level == 4) {
            btnPlaceSet.setVisibility(View.VISIBLE);
        } else if(level == 3) {
            btnPlaceSet.setVisibility(View.GONE);
        } else {
            btnPlaceSet.setVisibility(View.GONE);
            btnUserLevelSet.setVisibility(View.GONE);
            btnChangePassword.setVisibility(View.GONE);
        }

        //현장관리버튼 눌렀을때
        btnPlaceSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaceActivity.class);
                startActivity(intent);
            }
        });

        //직원등급관리 눌렀을때
        btnUserLevelSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });

        //비밀번호변경 눌렀을때
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}