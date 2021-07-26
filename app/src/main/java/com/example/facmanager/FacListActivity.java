package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facmanager.models.Facility;
import com.example.facmanager.models.Team;

import java.util.ArrayList;

public class FacListActivity extends AppCompatActivity {

    TextView textSeachItemNum;

    FacAdapter adapter = new FacAdapter();
    String place_id;

    String serial;
    int type;
    String subcontractor;
    String building;
    String floor;
    String spot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_list);

        textSeachItemNum = findViewById(R.id.textSeachItemNum);
        RecyclerView recyclerFac = findViewById(R.id.recyclerFac);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerFac.setLayoutManager(layoutManager);

        adapter.setOnFacItemClicked(new FacAdapter.OnFacItemClicked() {
            @Override
            public void onClick(View v, Facility facility) {
                Intent intent = new Intent(FacListActivity.this, FacilityActivity.class);
                intent.putExtra("facility_id", facility.id);
                startActivity(intent);
            }
        });

        recyclerFac.setAdapter(adapter);

        Button btnResearch = findViewById(R.id.btnResearch);
        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        place_id = getIntent().getStringExtra("place_id");

        serial = getIntent().getStringExtra("serial");
        type = getIntent().getIntExtra("type", -1);
        subcontractor = getIntent().getStringExtra("subcontractor");
        building = getIntent().getStringExtra("building");
        floor = getIntent().getStringExtra("floor");
        spot = getIntent().getStringExtra("spot");

        searchFacility(type, serial, subcontractor, building, floor, spot);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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
                textSeachItemNum.setText(facilities.size() + "개 검색결과");

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