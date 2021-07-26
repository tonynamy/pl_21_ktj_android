package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TaskListActivity extends AppCompatActivity {

    String place_id;

    LinearLayout layoutExpiredSoon;
    RecyclerView recyclerExpiredSoon;
    TaskPeriodAdapter adapterExpiredSoon;

    LinearLayout layoutStartPlan;
    RecyclerView recyclerStartPlan;
    TaskPlanAdapter adapterStartPlan;

    LinearLayout layoutEditPlan;
    RecyclerView recyclerEditPlan;
    TaskPlanAdapter adapterEditPlan;

    LinearLayout layoutDisPlan;
    RecyclerView recyclerDisPlan;
    TaskPlanAdapter adapterDisPlan;

    Button buttonAllFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        place_id = getIntent().getStringExtra("place_id");

        layoutExpiredSoon = findViewById(R.id.layoutExpiredSoon);
        recyclerExpiredSoon = findViewById(R.id.recyclerExpiredSoon);
        layoutStartPlan = findViewById(R.id.layoutStartPlan);
        recyclerStartPlan = findViewById(R.id.recyclerStartPlan);
        layoutEditPlan = findViewById(R.id.layoutEditPlan);
        recyclerEditPlan = findViewById(R.id.recyclerEditPlan);
        layoutDisPlan = findViewById(R.id.layoutDisPlan);
        recyclerDisPlan = findViewById(R.id.recyclerDisPlan);
        buttonAllFacility = findViewById(R.id.buttonAllFacility);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerExpiredSoon.setLayoutManager(layoutManager);
        adapterExpiredSoon = new TaskPeriodAdapter();
        //예시데이터
        TaskPeriodItem taskPeriodItem = new TaskPeriodItem();
        taskPeriodItem.serialNum = "FM-설비-FAB-001";
        taskPeriodItem.state = "만료";
        adapterExpiredSoon.addItem(taskPeriodItem);
        taskPeriodItem = new TaskPeriodItem();
        taskPeriodItem.serialNum = "FM-전기-FAB-007";
        taskPeriodItem.state = "~2021. 08. 02";
        adapterExpiredSoon.addItem(taskPeriodItem);

        recyclerExpiredSoon.setAdapter(adapterExpiredSoon);

        layoutManager = new LinearLayoutManager(this);
        recyclerStartPlan.setLayoutManager(layoutManager);
        adapterStartPlan = new TaskPlanAdapter();
        //예시데이터
        TaskPlanItem taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-설비-FAB-001";
        taskPlanItem.location = "FAB 3F C-F/13-21열";
        taskPlanItem.teamName = "김선재팀";
        adapterStartPlan.addItem(taskPlanItem);
        taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-전기-FAB-007";
        taskPlanItem.location = "동SUP 12F I-J/23-24열";
        taskPlanItem.teamName = "김영기팀";
        adapterStartPlan.addItem(taskPlanItem);

        recyclerStartPlan.setAdapter(adapterStartPlan);

        layoutManager = new LinearLayoutManager(this);
        recyclerEditPlan.setLayoutManager(layoutManager);
        adapterEditPlan = new TaskPlanAdapter();
        //예시데이터
        taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-설비-FAB-001";
        taskPlanItem.location = "FAB 3F C-F/13-21열";
        taskPlanItem.teamName = "김선재팀";
        adapterEditPlan.addItem(taskPlanItem);
        taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-전기-FAB-007";
        taskPlanItem.location = "동SUP 12F I-J/23-24열";
        taskPlanItem.teamName = "김영기팀";
        adapterEditPlan.addItem(taskPlanItem);
        taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-전기-FAB-008";
        taskPlanItem.location = "북SUP 12F J/19-20열";
        taskPlanItem.teamName = "김영기팀";
        adapterEditPlan.addItem(taskPlanItem);

        recyclerEditPlan.setAdapter(adapterEditPlan);

        layoutManager = new LinearLayoutManager(this);
        recyclerDisPlan.setLayoutManager(layoutManager);
        adapterDisPlan = new TaskPlanAdapter();
        //예시데이터
        taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-설비-FAB-001";
        taskPlanItem.location = "FAB 3F C-F/13-21열";
        taskPlanItem.teamName = "김선재팀";
        adapterDisPlan.addItem(taskPlanItem);
        taskPlanItem = new TaskPlanItem();
        taskPlanItem.serialNum = "FM-전기-FAB-007";
        taskPlanItem.location = "동SUP 12F I-J/23-24열";
        taskPlanItem.teamName = "김영기팀";
        adapterDisPlan.addItem(taskPlanItem);

        recyclerDisPlan.setAdapter(adapterDisPlan);


        buttonAllFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                intent.putExtra("place_id", place_id);
                startActivity(intent);
            }
        });
    }
}