package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FacListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_list);

        RecyclerView recyclerFac = findViewById(R.id.recyclerFac);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerFac.setLayoutManager(layoutManager);

        FacAdapter adapter = new FacAdapter();

        adapter.addItem(new FacItem("FM-설비-FAB-001", "설치전"));
        adapter.addItem(new FacItem("FM-전기-FAB-007", "설치전"));
        adapter.addItem(new FacItem("FM-전기-FAB-008", "설치전"));
        adapter.addItem(new FacItem("FM-설비-FAB-002", "설치전"));
        adapter.addItem(new FacItem("FM-설비-FAB-003", "설치전"));

        recyclerFac.setAdapter(adapter);

        Button btnResearch = findViewById(R.id.btnResearch);
        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}