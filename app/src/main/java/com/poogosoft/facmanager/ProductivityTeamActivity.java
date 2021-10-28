package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductivityTeamActivity extends AppCompatActivity {

    String team_id;

    TextView textProductivityTeamTitle;
    LinearLayout layoutProductivityTeam;
    RecyclerView recycleProductivityTeamCube;
    ProductivityTeamAdapter teamProductivityCubeAdapter;
    TextView textProductivityTeamCubeTotal;
    RecyclerView recycleProductivityTeamDanger;
    ProductivityTeamAdapter teamProductivityDangerAdapter;
    TextView textProductivityTeamDangerTotal;
    RecyclerView recycleProductivityTeamManday;
    ProductivityTeamAdapter teamProductivityMandayAdapter;
    TextView textProductivityTeamMandayTotal;
    TextView textProductivityTeamResult;
    Button buttonBack2Productivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productivity_team);

        //뷰 가져오기
        textProductivityTeamTitle = findViewById(R.id.textProductivityTeamTitle);
        layoutProductivityTeam = findViewById(R.id.layoutProductivityTeam);
        recycleProductivityTeamCube = findViewById(R.id.recycleProductivityTeamCube);
        textProductivityTeamCubeTotal = findViewById(R.id.textProductivityTeamCubeTotal);
        recycleProductivityTeamDanger = findViewById(R.id.recycleProductivityTeamDanger);
        textProductivityTeamDangerTotal = findViewById(R.id.textProductivityTeamDangerTotal);
        recycleProductivityTeamManday = findViewById(R.id.recycleProductivityTeamManday);
        textProductivityTeamMandayTotal = findViewById(R.id.textProductivityTeamMandayTotal);
        textProductivityTeamResult = findViewById(R.id.textProductivityTeamResult);
        buttonBack2Productivity = findViewById(R.id.buttonBack2Productivity);

        //리사이클러 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleProductivityTeamCube.setLayoutManager(layoutManager);
        teamProductivityCubeAdapter = new ProductivityTeamAdapter();
        recycleProductivityTeamCube.setAdapter(teamProductivityCubeAdapter);

        layoutManager = new LinearLayoutManager(this);
        recycleProductivityTeamDanger.setLayoutManager(layoutManager);
        teamProductivityDangerAdapter = new ProductivityTeamAdapter();
        recycleProductivityTeamDanger.setAdapter(teamProductivityDangerAdapter);

        layoutManager = new LinearLayoutManager(this);
        recycleProductivityTeamManday.setLayoutManager(layoutManager);
        teamProductivityMandayAdapter = new ProductivityTeamAdapter();
        recycleProductivityTeamManday.setAdapter(teamProductivityMandayAdapter);

        //예시
        textProductivityTeamTitle.setText("김두한팀 10월 생산성");

        ProductivityTeamItem productivityTeamItem = new ProductivityTeamItem();
        productivityTeamItem.type = 1;
        productivityTeamItem.name = "SAM-002";
        productivityTeamItem.size ="218.1";
        productivityTeamItem.is_square = false;
        productivityTeamItem.manday = "10";
        teamProductivityCubeAdapter.addItem(productivityTeamItem);

        textProductivityTeamCubeTotal.setText("21.81");

        productivityTeamItem = new ProductivityTeamItem();
        productivityTeamItem.type = 1;
        productivityTeamItem.name = "SAM-004";
        productivityTeamItem.size ="57";
        productivityTeamItem.is_square = true;
        productivityTeamItem.manday = "9";
        teamProductivityDangerAdapter.addItem(productivityTeamItem);

        textProductivityTeamDangerTotal.setText("6.33");

        productivityTeamItem = new ProductivityTeamItem();
        productivityTeamItem.type = 3;
        productivityTeamItem.name = "SAM-001";
        productivityTeamItem.size ="218";
        productivityTeamItem.is_square = false;
        productivityTeamItem.manday = "9";
        teamProductivityMandayAdapter.addItem(productivityTeamItem);

        productivityTeamItem = new ProductivityTeamItem();
        productivityTeamItem.type = 3;
        productivityTeamItem.name = "SAM-001";
        productivityTeamItem.size ="218";
        productivityTeamItem.is_square = false;
        productivityTeamItem.manday = "10";
        teamProductivityMandayAdapter.addItem(productivityTeamItem);

        textProductivityTeamMandayTotal.setText("19");

        teamProductivityCubeAdapter.notifyDataSetChanged();
        teamProductivityDangerAdapter.notifyDataSetChanged();
        teamProductivityMandayAdapter.notifyDataSetChanged();

        textProductivityTeamResult.setVisibility(View.GONE);

    }
}