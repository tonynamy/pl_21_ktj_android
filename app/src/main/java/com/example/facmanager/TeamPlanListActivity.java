package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeamPlanListActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;

    RecyclerView recyclerTeamLeaderTask;
    TeamPlanAdapter teamPlanAdapter;

    Button buttonAllFacility2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_plan_list);

        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");

        recyclerTeamLeaderTask = findViewById(R.id.recyclerTeamLeaderTask);
        buttonAllFacility2 = findViewById(R.id.buttonAllFacility2);

        //팀장님 작업 목록
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTeamLeaderTask.setLayoutManager(layoutManager);
        teamPlanAdapter = new TeamPlanAdapter();
        //예시데이터
        TeamPlanItem teamPlanItem = new TeamPlanItem();
        teamPlanItem.serialNum = "FM-설비-FAB-001";
        teamPlanItem.location = "FAB 3F C-F/13-21열";
        teamPlanItem.state = "설치예정";
        teamPlanAdapter.addItem(teamPlanItem);
        teamPlanItem = new TeamPlanItem();
        teamPlanItem.serialNum = "FM-전기-FAB-007";
        teamPlanItem.location = "동SUP 12F I-J/23-24열";
        teamPlanItem.state = "수정예정";
        teamPlanAdapter.addItem(teamPlanItem);
        teamPlanItem = new TeamPlanItem();
        teamPlanItem.serialNum = "FM-전기-FAB-008";
        teamPlanItem.location = "북SUP 12F J/19-20열";
        teamPlanItem.state = "해체예정";
        teamPlanAdapter.addItem(teamPlanItem);

        recyclerTeamLeaderTask.setAdapter(teamPlanAdapter);

        //전체조회버튼 선택
        buttonAllFacility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                startActivity(intent);
            }
        });

    }
}