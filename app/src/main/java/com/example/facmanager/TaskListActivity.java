package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    int button_right;

    LinearLayout layoutExpiredSoon;
    RecyclerView recyclerExpiredSoon;
    TaskListAdapter adapterExpiredSoon;

    LinearLayout layoutStartPlan;
    RecyclerView recyclerStartPlan;
    TaskListAdapter adapterStartPlan;

    LinearLayout layoutEditPlan;
    RecyclerView recyclerEditPlan;
    TaskListAdapter adapterEditPlan;

    LinearLayout layoutDisPlan;
    RecyclerView recyclerDisPlan;
    TaskListAdapter adapterDisPlan;

    Button buttonAllFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        button_right = getIntent().getIntExtra("button_right", 0);

        layoutExpiredSoon = findViewById(R.id.layoutExpiredSoon);
        recyclerExpiredSoon = findViewById(R.id.recyclerExpiredSoon);
        layoutStartPlan = findViewById(R.id.layoutStartPlan);
        recyclerStartPlan = findViewById(R.id.recyclerStartPlan);
        layoutEditPlan = findViewById(R.id.layoutEditPlan);
        recyclerEditPlan = findViewById(R.id.recyclerEditPlan);
        layoutDisPlan = findViewById(R.id.layoutDisPlan);
        recyclerDisPlan = findViewById(R.id.recyclerDisPlan);
        buttonAllFacility = findViewById(R.id.buttonAllFacility);

        //기간만료작업
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerExpiredSoon.setLayoutManager(layoutManager);
        adapterExpiredSoon = new TaskListAdapter();
        recyclerExpiredSoon.setAdapter(adapterExpiredSoon);

        //설치예정작업
        layoutManager = new LinearLayoutManager(this);
        recyclerStartPlan.setLayoutManager(layoutManager);
        adapterStartPlan = new TaskListAdapter();
        recyclerStartPlan.setAdapter(adapterStartPlan);

        //수정예정작업
        layoutManager = new LinearLayoutManager(this);
        recyclerEditPlan.setLayoutManager(layoutManager);
        adapterEditPlan = new TaskListAdapter();
        recyclerEditPlan.setAdapter(adapterEditPlan);

        //해체예정작업
        layoutManager = new LinearLayoutManager(this);
        recyclerDisPlan.setLayoutManager(layoutManager);
        adapterDisPlan = new TaskListAdapter();
        recyclerDisPlan.setAdapter(adapterDisPlan);

        //Task 불러오기
        loadTaskInfo();

        //아이템 눌렀을때
        ArrayList<TaskListAdapter> adapterList = new ArrayList<>();
        adapterList.add(adapterExpiredSoon);
        adapterList.add(adapterStartPlan);
        adapterList.add(adapterEditPlan);
        adapterList.add(adapterDisPlan);
        for(TaskListAdapter adapter : adapterList) {
            adapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, String facility_id) {
                    Intent intent = new Intent(v.getContext(), FacilityActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("facility_id", facility_id);
                    intent.putExtra("button_right", button_right);
                    startActivity(intent);
                }
            });
        }

        //전체조회 버튼
        buttonAllFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("button_right", button_right);
                startActivity(intent);
            }
        });
    }

    public void loadTaskInfo() {

        adapterExpiredSoon.clear();
        adapterStartPlan.clear();
        adapterEditPlan.clear();
        adapterDisPlan.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                List<ArrayList<TaskListItem>> response = (List<ArrayList<TaskListItem>>) data;

                ArrayList<TaskListItem> expire_facilities = response.get(0);
                ArrayList<TaskListItem> construct_planned_facilities = response.get(1);
                ArrayList<TaskListItem> edit_planned_facilities = response.get(2);
                ArrayList<TaskListItem> destruct_planned_facilities = response.get(3);

                for(TaskListItem taskPlanItem : expire_facilities) {
                    adapterExpiredSoon.addItem(taskPlanItem);
                }

                for(TaskListItem taskPlanItem : construct_planned_facilities) {
                    adapterStartPlan.addItem(taskPlanItem);
                }

                for(TaskListItem taskPlanItem : edit_planned_facilities) {
                    adapterEditPlan.addItem(taskPlanItem);
                }

                for(TaskListItem taskPlanItem : destruct_planned_facilities) {
                    adapterDisPlan.addItem(taskPlanItem);
                }

                adapterExpiredSoon.notifyDataSetChanged();
                adapterStartPlan.notifyDataSetChanged();
                adapterEditPlan.notifyDataSetChanged();
                adapterDisPlan.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(TaskListActivity.this, "시설 정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();

        api.getFacilityTaskPlan(place_id);
    }

}