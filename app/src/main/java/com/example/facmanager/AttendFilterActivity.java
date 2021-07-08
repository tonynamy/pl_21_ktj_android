package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.facmanager.models.Team;

import java.util.ArrayList;

public class AttendFilterActivity extends AppCompatActivity {

    ArrayList<Team> teams = new ArrayList<>();
    //ArrayList<String> teamNames = new ArrayList<>();
    TeamItem teamItem;
    TeamAdapter teamAdapter;
    Boolean changeTeamMode;
    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_filter);

        changeTeamMode = getIntent().getBooleanExtra("changeTeamMode", false);
        place_id = getIntent().getStringExtra("place_id");

        RecyclerView recyclerTeam = findViewById(R.id.recyclerTeam);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTeam.setLayoutManager(layoutManager);
        teamAdapter = new TeamAdapter();

        //팀변경 상태인 경우
        if(changeTeamMode) {
            teamAdapter.changeTeamMode = true;
            teamAdapter.user_id = getIntent().getStringExtra("user_id");
        }

        recyclerTeam.setAdapter(teamAdapter);


        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                ArrayList<Team> _teams = (ArrayList<Team>) data;

                teams.clear();
                teams = _teams;

                for(Team team : _teams) {
                    teamItem = new TeamItem();
                    teamItem.setTeamName(team.name);
                    teamItem.teamId = team.id;
                    teamAdapter.addItem(teamItem);
                }
                teamAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(AttendFilterActivity.this, "팀목록을 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getTeams();
    }
}