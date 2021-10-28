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

    LinearLayout layoutEtcPlan;
    RecyclerView recyclerEtcPlan;
    TaskListAdapter adapterEtcPlan;

    TextView textTaskResult;
    Button buttonAllFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //Intent에서 가져오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        button_right = getIntent().getIntExtra("button_right", 0);

        //뷰에서 가져오기
        layoutExpiredSoon = findViewById(R.id.layoutExpiredSoon);
        recyclerExpiredSoon = findViewById(R.id.recyclerExpiredSoon);
        layoutStartPlan = findViewById(R.id.layoutStartPlan);
        recyclerStartPlan = findViewById(R.id.recyclerStartPlan);
        layoutEditPlan = findViewById(R.id.layoutEditPlan);
        recyclerEditPlan = findViewById(R.id.recyclerEditPlan);
        layoutDisPlan = findViewById(R.id.layoutDisPlan);
        recyclerDisPlan = findViewById(R.id.recyclerDisPlan);
        layoutEtcPlan = findViewById(R.id.layoutEtcPlan);
        recyclerEtcPlan = findViewById(R.id.recyclerEtcPlan);
        textTaskResult = findViewById(R.id.textTaskResult);
        buttonAllFacility = findViewById(R.id.buttonAllFacility);

        //기간만료작업
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerExpiredSoon.setLayoutManager(layoutManager);
        adapterExpiredSoon = new TaskListAdapter();
        adapterExpiredSoon.is_expired_list = true;
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

        //기타작업
        layoutManager = new LinearLayoutManager(this);
        recyclerEtcPlan.setLayoutManager(layoutManager);
        adapterEtcPlan = new TaskListAdapter();
        recyclerEtcPlan.setAdapter(adapterEtcPlan);

        //아이템 눌렀을때
        ArrayList<TaskListAdapter> adapterList = new ArrayList<>();
        adapterList.add(adapterExpiredSoon);
        adapterList.add(adapterStartPlan);
        adapterList.add(adapterEditPlan);
        adapterList.add(adapterDisPlan);
        adapterList.add(adapterEtcPlan);
        for(TaskListAdapter adapter : adapterList) {
            adapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, String id, int type) {
                    //도면이 있는 작업일때
                    if(type != 4) {
                        Intent intent = new Intent(v.getContext(), FacilityActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("place_id", place_id);
                        intent.putExtra("facility_id", id);
                        intent.putExtra("button_right", button_right);
                        startActivity(intent);

                     //도면이 없는 작업일떄
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

        //기타작업의 아이템을 눌렀을때는 따로 만들어야할듯

        //전체조회 버튼
        buttonAllFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacSearchActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
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

        adapterExpiredSoon.clear();
        adapterStartPlan.clear();
        adapterEditPlan.clear();
        adapterDisPlan.clear();
        adapterEtcPlan.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                List<ArrayList<TaskListItem>> response = (List<ArrayList<TaskListItem>>) data;

                ArrayList<TaskListItem> expired_facilities = response.get(0);
                ArrayList<TaskListItem> start_plan_facilities = response.get(1);
                ArrayList<TaskListItem> edit_plan_facilities = response.get(2);
                ArrayList<TaskListItem> dis_plan_facilities = response.get(3);
                ArrayList<TaskListItem> etc_taskplans = response.get(4);

                if(expired_facilities.size() > 0 || start_plan_facilities.size() > 0 || edit_plan_facilities.size() > 0 || dis_plan_facilities.size() > 0 || etc_taskplans.size() > 0) {
                    textTaskResult.setVisibility(View.GONE);
                } else {
                    textTaskResult.setText("작업계획이 없습니다.\n아래의 전체조회버튼을 눌러 조회후 작업계획을 추가해주세요.");
                    textTaskResult.setVisibility(View.VISIBLE);
                }

                if(!expired_facilities.isEmpty()){
                    layoutExpiredSoon.setVisibility(View.VISIBLE);
                } else {
                    layoutExpiredSoon.setVisibility(View.GONE);
                }
                if(!start_plan_facilities.isEmpty()) {
                    layoutStartPlan.setVisibility(View.VISIBLE);
                } else {
                    layoutStartPlan.setVisibility(View.GONE);
                }
                if(!edit_plan_facilities.isEmpty()) {
                    layoutEditPlan.setVisibility(View.VISIBLE);
                } else {
                    layoutEditPlan.setVisibility(View.GONE);
                }
                if(!dis_plan_facilities.isEmpty()) {
                    layoutDisPlan.setVisibility(View.VISIBLE);
                } else {
                    layoutDisPlan.setVisibility(View.GONE);
                }
                if(!etc_taskplans.isEmpty()) {
                    layoutEtcPlan.setVisibility(View.VISIBLE);
                } else {
                    layoutEtcPlan.setVisibility(View.GONE);
                }

                for(TaskListItem taskPlanItem : expired_facilities) {
                    adapterExpiredSoon.addItem(taskPlanItem);
                }
                for(TaskListItem taskPlanItem : start_plan_facilities) {
                    adapterStartPlan.addItem(taskPlanItem);
                }
                for(TaskListItem taskPlanItem : edit_plan_facilities) {
                    adapterEditPlan.addItem(taskPlanItem);
                }
                for(TaskListItem taskPlanItem : dis_plan_facilities) {
                    adapterDisPlan.addItem(taskPlanItem);
                }
                for(TaskListItem taskPlanItem : etc_taskplans) {
                    adapterEtcPlan.addItem(taskPlanItem);
                }

                adapterExpiredSoon.notifyDataSetChanged();
                adapterStartPlan.notifyDataSetChanged();
                adapterEditPlan.notifyDataSetChanged();
                adapterDisPlan.notifyDataSetChanged();
                adapterEtcPlan.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                textTaskResult.setText("작업계획을 불러오는데 실패했습니다.\n사유: " + errorMsg);
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getTaskPlan(place_id);

    }
}