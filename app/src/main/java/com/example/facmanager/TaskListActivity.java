package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.facmanager.models.Facility;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    Boolean isManagerButton;

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

        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        isManagerButton = getIntent().getBooleanExtra("isManagerButton", false);

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
        adapterExpiredSoon = new TaskPeriodAdapter();

        recyclerExpiredSoon.setAdapter(adapterExpiredSoon);

        //설치예정작업
        layoutManager = new LinearLayoutManager(this);
        recyclerStartPlan.setLayoutManager(layoutManager);
        adapterStartPlan = new TaskPlanAdapter();

        recyclerStartPlan.setAdapter(adapterStartPlan);

        //수정예정작업
        layoutManager = new LinearLayoutManager(this);
        recyclerEditPlan.setLayoutManager(layoutManager);
        adapterEditPlan = new TaskPlanAdapter();

        recyclerEditPlan.setAdapter(adapterEditPlan);

        //해체예정작업
        layoutManager = new LinearLayoutManager(this);
        recyclerDisPlan.setLayoutManager(layoutManager);
        adapterDisPlan = new TaskPlanAdapter();

        recyclerDisPlan.setAdapter(adapterDisPlan);

        loadFacilityInfo();

        //전체조회 버튼
        buttonAllFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("is_manager_button", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loadFacilityInfo();
    }

    public void loadFacilityInfo() {

        adapterExpiredSoon.clear();
        adapterStartPlan.clear();
        adapterEditPlan.clear();
        adapterDisPlan.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                List<ArrayList<Facility>> response = (List<ArrayList<Facility>>) data;

                ArrayList<Facility> expire_facilities = response.get(0);
                ArrayList<Facility> construct_planned_facilities = response.get(1);
                ArrayList<Facility> edit_planned_facilities = response.get(2);
                ArrayList<Facility> destruct_planned_facilities = response.get(3);

                for (Facility facility : expire_facilities) {
                    adapterExpiredSoon.addItem(facility);
                }

                for (Facility facility : construct_planned_facilities) {
                    adapterStartPlan.addItem(facility);
                }

                for (Facility facility : edit_planned_facilities) {
                    adapterEditPlan.addItem(facility);
                }

                for (Facility facility : destruct_planned_facilities) {
                    adapterDisPlan.addItem(facility);
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

        api.getFacilityInfo(place_id);

    }
}