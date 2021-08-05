package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class TaskListTeamActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;

    RecyclerView recyclerTeamTask;
    TaskListAdapter taskListAdapter;

    Button buttonAllFacility2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_team);

        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");

        recyclerTeamTask = findViewById(R.id.recyclerTeamLeaderTask);
        buttonAllFacility2 = findViewById(R.id.buttonAllFacility2);

        //팀장님 작업 목록
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTeamTask.setLayoutManager(layoutManager);
        taskListAdapter = new TaskListAdapter();
        recyclerTeamTask.setAdapter(taskListAdapter);

        //Task 불러오기
        loadTaskInfo();

        taskListAdapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, String facility_id) {
                Intent intent = new Intent(v.getContext(), FacilityActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("facility_id", facility_id);
                startActivity(intent);
            }
        });

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

    public void loadTaskInfo() {

        taskListAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                ArrayList<TaskListItem> taskList = (ArrayList<TaskListItem>) data;
                for(TaskListItem taskListItem : taskList) {
                    taskListAdapter.addItem(taskListItem);
                }
                taskListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {

            }
        };
        API api = new API.Builder(apiCallback).build();

        api.getFacilityTeamTaskPlan(team_id);
    }
}