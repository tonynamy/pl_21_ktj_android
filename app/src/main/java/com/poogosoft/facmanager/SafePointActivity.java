package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SafePointActivity extends AppCompatActivity {

    TextView textSafePointMonth;
    RecyclerView recyclerSafePoint;
    SafePointAdapter safePointAdapter;
    Button buttonSafePointMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_point);

        //뷰 가져오기
        textSafePointMonth = findViewById(R.id.textSafePointMonth);
        recyclerSafePoint = findViewById(R.id.recyclerSafePoint);


        //리사이클러 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerSafePoint.setLayoutManager(layoutManager);
        safePointAdapter = new SafePointAdapter();
        recyclerSafePoint.setAdapter(safePointAdapter);

        //예시
        SafePointItem safePointItem = new SafePointItem();
        safePointItem.team_id = "1";
        safePointItem.team_name = "김두한팀";
        safePointItem.point = "75점";
        safePointAdapter.addItem(safePointItem);

        safePointItem = new SafePointItem();
        safePointItem.team_id = "2";
        safePointItem.team_name = "김선재팀";
        safePointItem.point = "70점";
        safePointAdapter.addItem(safePointItem);

        safePointItem = new SafePointItem();
        safePointItem.team_id = "3";
        safePointItem.team_name = "심청이팀";
        safePointItem.point = "100점";
        safePointAdapter.addItem(safePointItem);

        safePointItem = new SafePointItem();
        safePointItem.team_id = "4";
        safePointItem.team_name = "홍길동팀";
        safePointItem.point = "100점";
        safePointAdapter.addItem(safePointItem);

        safePointAdapter.notifyDataSetChanged();

        safePointAdapter.setOnItemClickListener(new SafePointAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, String team_id) {
                Intent intent = new Intent(v.getContext(), SafePointTeamActivity.class);
                //Toast.makeText(v.getContext(), team_id, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}