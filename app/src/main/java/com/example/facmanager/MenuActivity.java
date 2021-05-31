package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnAttendMenu = findViewById(R.id.btnAttendMenu);
        btnAttendMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AttendFilterActivity.class);
                startActivity(intent);
            }
        });


        Button btnInstallMenu = findViewById(R.id.btnInstallMenu);
        btnInstallMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, FacFilterActivity.class);
                startActivity(intent);
            }
        });
    }
}