package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskListTeamActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    int button_right;

    RecyclerView recyclerTeamTask;
    TaskListAdapter taskListAdapter;
    TextView textTeamTaskResult;
    Button buttonAllFacility2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_team);

        //Intent에서 가져오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        button_right = getIntent().getIntExtra("button_right", 0);

        //뷰에서 가져오기
        recyclerTeamTask = findViewById(R.id.recyclerTeamLeaderTask);
        textTeamTaskResult = findViewById(R.id.textTeamTaskResult);
        buttonAllFacility2 = findViewById(R.id.buttonAllFacility2);

        //팀장님 작업 목록
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTeamTask.setLayoutManager(layoutManager);
        taskListAdapter = new TaskListAdapter();
        recyclerTeamTask.setAdapter(taskListAdapter);

        //아이템 눌렀을때
        taskListAdapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, String facility_id) {
                Intent intent = new Intent(v.getContext(), FacilityActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("team_id", team_id);
                intent.putExtra("facility_id", facility_id);
                intent.putExtra("button_right", button_right);
                startActivity(intent);
            }
        });

        //전체조회버튼 선택
        buttonAllFacility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacSearchActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("team_id", team_id);
                intent.putExtra("button_right", button_right);
                startActivity(intent);
            }
        });

        //Task 불러오기
        loadTaskInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loadTaskInfo();
    }

    public void loadTaskInfo() {

        taskListAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                ArrayList<TaskListItem> taskList = (ArrayList<TaskListItem>) data;
                if(taskList.size() > 0) {
                    textTeamTaskResult.setVisibility(View.GONE);
                } else {
                    textTeamTaskResult.setText("작업계획이 없습니다.\n아래의 전체조회버튼을 눌러주세요.");
                    textTeamTaskResult.setVisibility(View.VISIBLE);
                }

                for(TaskListItem taskListItem : taskList) {
                    taskListAdapter.addItem(taskListItem);
                }

                taskListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed(String errorMsg) {
                textTeamTaskResult.setText("작업계획을 불러오는데 실패했습니다.\n사유: " + errorMsg);
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getTeamTaskPlan(team_id);
    }
}