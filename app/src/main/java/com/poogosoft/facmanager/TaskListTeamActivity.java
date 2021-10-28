package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskListTeamActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    int button_right;

    LinearLayout layoutPlanTask;
    RecyclerView recyclePlanTask;
    LinearLayout layoutRecentTask;
    RecyclerView recycleRecentTask;
    TextView textTeamTaskResult;
    Button buttonAllFacility2;

    TaskListAdapter planTaskAdapter;
    TaskListAdapter recentTaskAdapter;

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
        layoutPlanTask = findViewById(R.id.layoutPlanTask);
        recyclePlanTask = findViewById(R.id.recyclePlanTask);
        layoutRecentTask = findViewById(R.id.layoutRecentTask);
        recycleRecentTask = findViewById(R.id.recycleRecentTask);
        textTeamTaskResult = findViewById(R.id.textTeamTaskResult);
        buttonAllFacility2 = findViewById(R.id.buttonAllFacility2);

        //계획된 작업 목록
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclePlanTask.setLayoutManager(layoutManager);
        planTaskAdapter = new TaskListAdapter();
        recyclePlanTask.setAdapter(planTaskAdapter);

        //최근3일내 작업 목록
        layoutManager = new LinearLayoutManager(this);
        recycleRecentTask.setLayoutManager(layoutManager);
        recentTaskAdapter = new TaskListAdapter();
        recycleRecentTask.setAdapter(recentTaskAdapter);

        //아이템 눌렀을때
        ArrayList<TaskListAdapter> adapterList = new ArrayList<>();
        adapterList.add(planTaskAdapter);
        adapterList.add(recentTaskAdapter);
        for(TaskListAdapter adapter : adapterList) {
            adapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, String id, int type) {
                    //도면이 있는 작업일때
                    if(type != 4) {
                        Intent intent = new Intent(v.getContext(), FacilityActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("place_id", place_id);
                        intent.putExtra("team_id", team_id);
                        intent.putExtra("facility_id", id);
                        intent.putExtra("button_right", button_right);
                        startActivity(intent);

                     //도면이 없는 작업일때
                    } else {
                        Intent intent = new Intent(v.getContext(), TaskEtcActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("place_id", place_id);
                        intent.putExtra("team_id", team_id);
                        intent.putExtra("task_id", id);
                        intent.putExtra("button_right", button_right);
                        startActivity(intent);
                    }

                }
            });
        }


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

        planTaskAdapter.clear();
        recentTaskAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                List<ArrayList<TaskListItem>> response = (List<ArrayList<TaskListItem>>) data;

                ArrayList<TaskListItem> plan_task = response.get(0);
                ArrayList<TaskListItem> recent_task = response.get(1);

                if(plan_task.size() > 0 || recent_task.size() > 0 ) {
                    textTeamTaskResult.setVisibility(View.GONE);
                } else {
                    textTeamTaskResult.setText("작업계획이 없습니다.\n아래의 전체조회버튼을 눌러주세요.");
                    textTeamTaskResult.setVisibility(View.VISIBLE);
                }

                if(!plan_task.isEmpty()){
                    layoutPlanTask.setVisibility(View.VISIBLE);
                } else {
                    layoutPlanTask.setVisibility(View.GONE);
                }
                if(!recent_task.isEmpty()) {
                    layoutRecentTask.setVisibility(View.VISIBLE);
                } else {
                    layoutRecentTask.setVisibility(View.GONE);
                }

                for(TaskListItem taskPlanItem : plan_task) {
                    planTaskAdapter.addItem(taskPlanItem);
                }
                for(TaskListItem taskPlanItem : recent_task) {
                    recentTaskAdapter.addItem(taskPlanItem);
                }

                planTaskAdapter.notifyDataSetChanged();
                recentTaskAdapter.notifyDataSetChanged();
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