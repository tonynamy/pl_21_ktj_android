package com.example.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {


    TextView txtInfo;
    Button btnAttendMenu;
    Button btnTeamLeaderMenu;
    Button btnManagerMenu;
    Button btnAdminMenu;

    int level;
    String place_id;
    String place_name;
    String user_name;
    String team_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        level = getIntent().getIntExtra("level", 0);
        place_id = getIntent().getStringExtra("place_id");
        place_name = getIntent().getStringExtra("place_name");
        user_name = getIntent().getStringExtra("user_name");
        team_id = getIntent().getStringExtra("team_id");

        txtInfo = findViewById(R.id.txtInfo);
        btnAttendMenu = findViewById(R.id.btnAttendMenu);
        btnTeamLeaderMenu = findViewById(R.id.btnTeamLeaderMenu);
        btnManagerMenu = findViewById(R.id.btnManagerMenu);
        btnAdminMenu = findViewById(R.id.btnAdminMenu);

        btnAttendMenu.setVisibility(View.GONE);
        btnTeamLeaderMenu.setVisibility(View.GONE);
        btnManagerMenu.setVisibility(View.GONE);
        btnAdminMenu.setVisibility(View.GONE);

        String role_name;
        switch (level) {
            case 1:
                role_name = "팀장";
                break;
            case 2:
                role_name = "관리자";
                break;
            case 3:
            case 4:
                role_name = "최고관리자";
                break;
            default:
                role_name = "대기자";
                break;
        }
        txtInfo.setText(place_name + " " + user_name + " " + role_name);

        if (level >= 1) {
            btnAttendMenu.setVisibility(View.VISIBLE);
            btnTeamLeaderMenu.setVisibility(View.VISIBLE);
        }
        if (level >= 2) {
            btnManagerMenu.setVisibility(View.VISIBLE);
        }
        if (level >= 3) {
            btnAdminMenu.setVisibility(View.VISIBLE);
        }

        btnAttendMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(level == 1) {
                    choiceTeamDialog();
                } else if(level > 1) {
                    choiceAllTeam();
                }
            }
        });

        btnManagerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void choiceTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_find_team_option, null);
        builder.setView(view);

        TextView txtFindMyTeam = view.findViewById(R.id.txtFindMyTeam);
        TextView txtFindAllTeam = view.findViewById(R.id.txtFindAllTeam);
        TextView txtDialogCancel2 = view.findViewById(R.id.txtDialogCancel2);

        AlertDialog dialog = builder.create();
        dialog.show();

        txtFindMyTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AttendActivity.class);
                intent.putExtra("team_id", team_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        txtFindAllTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceAllTeam();
                dialog.dismiss();
            }
        });

        txtDialogCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void choiceAllTeam() {
        Intent intent = new Intent(this, AttendFilterActivity.class);
        intent.putExtra("place_id", place_id);
        startActivity(intent);
    }
}