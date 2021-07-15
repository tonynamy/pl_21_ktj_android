package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.facmanager.models.Facility;
import com.example.facmanager.models.Team;

import java.util.ArrayList;

public class FacListActivity extends AppCompatActivity {

    FacAdapter adapter = new FacAdapter();
    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_list);

        RecyclerView recyclerFac = findViewById(R.id.recyclerFac);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerFac.setLayoutManager(layoutManager);

        recyclerFac.setAdapter(adapter);

        Button btnResearch = findViewById(R.id.btnResearch);
        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        place_id = getIntent().getStringExtra("place_id");

        String serial = getIntent().getStringExtra("serial");
        int type = getIntent().getIntExtra("type", -1);
        String subcontractor = getIntent().getStringExtra("subcontractor");
        String building = getIntent().getStringExtra("building");
        String floor = getIntent().getStringExtra("floor");
        String spot = getIntent().getStringExtra("spot");

        searchFacility(type, serial, subcontractor, building, floor, spot);
    }

    public void searchFacility(int type, String serial, String subcontractor, String building, String floor, String spot) {

        adapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                ArrayList<Facility> facilities = (ArrayList<Facility>) data;

                for(Facility facility : facilities) {
                    adapter.addItem(facility);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(FacListActivity.this, "설비 목록을 검색하는데에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.searchFacility(place_id, serial, type, subcontractor, building, floor, spot);



    }
}