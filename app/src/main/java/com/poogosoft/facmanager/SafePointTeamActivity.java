package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class SafePointTeamActivity extends AppCompatActivity {

    TextView textSafePointTeamTitle;
    LinearLayout layoutSafePointTeam;
    RecyclerView recycleSafePointTeam;
    SafePointTeamAdapter safePointTeamAdapter;
    TextView textSafePointTeamTotal;
    TextView textSafePointTeamResult;
    Button buttonBack2SafePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_point_team);

        //뷰 가져오기
        textSafePointTeamTitle = findViewById(R.id.textSafePointTeamTitle);
        layoutSafePointTeam = findViewById(R.id.layoutSafePointTeam);
        recycleSafePointTeam = findViewById(R.id.recycleSafePointTeam);
        textSafePointTeamTotal = findViewById(R.id.textSafePointTeamTotal);
        textSafePointTeamResult = findViewById(R.id.textSafePointTeamResult);
        buttonBack2SafePoint = findViewById(R.id.buttonBack2SafePoint);

        //리사이클러 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleSafePointTeam.setLayoutManager(layoutManager);
        safePointTeamAdapter = new SafePointTeamAdapter();
        recycleSafePointTeam.setAdapter(safePointTeamAdapter);

        //예시
        textSafePointTeamTitle.setText("김두한팀 10월 안전점수");

        SafePointTeamItem safePointTeamItem = new SafePointTeamItem();
        safePointTeamItem.date = new Date();
        safePointTeamItem.title = "재발방지대책";
        safePointTeamItem.point = "-20";
        safePointTeamAdapter.addItem(safePointTeamItem);

        safePointTeamItem = new SafePointTeamItem();
        safePointTeamItem.date = new Date();
        safePointTeamItem.title = "개선지시서 및 불합리";
        safePointTeamItem.point = "-5";
        safePointTeamAdapter.addItem(safePointTeamItem);

        textSafePointTeamTotal.setText("75");

        textSafePointTeamResult.setVisibility(View.GONE);

    }
}