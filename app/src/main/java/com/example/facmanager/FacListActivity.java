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

    int level;
    String place_id;
    String super_manager_name;
    Boolean is_manager_button;
    String serial;
    int type;
    String subcontractor;
    String building;
    String floor;
    String spot;

    TextView textFacListTitle;
    TextView textSeachItemNum;
    RecyclerView recyclerFac;

    FacAdapter facAdapter = new FacAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_list);

        //Intent Extra 값 받아오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        super_manager_name = getIntent().getStringExtra("super_manager_name");
        is_manager_button = getIntent().getBooleanExtra("is_manager_button", false);
        serial = getIntent().getStringExtra("serial");
        type = getIntent().getIntExtra("type", -1);
        subcontractor = getIntent().getStringExtra("subcontractor");
        building = getIntent().getStringExtra("building");
        floor = getIntent().getStringExtra("floor");
        spot = getIntent().getStringExtra("spot");

        //뷰 불러오기
        textFacListTitle = findViewById(R.id.textFacListTitle);
        textSeachItemNum = findViewById(R.id.textSeachItemNum);
        recyclerFac = findViewById(R.id.recyclerFac);

        if(is_manager_button){
            textFacListTitle.setText("관리자메뉴");
        } else if(!super_manager_name.isEmpty()) {
            textFacListTitle.setText(super_manager_name + " 담당자 조회");
        } else {
            textFacListTitle.setText("팀장님메뉴");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerFac.setLayoutManager(layoutManager);

        facAdapter.setOnFacItemClicked(new FacAdapter.OnFacItemClicked() {
            @Override
            public void onClick(View v, Facility facility) {
                Intent intent = new Intent(FacListActivity.this, FacilityActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("facility_id", facility.id);
                intent.putExtra("super_manager_name", super_manager_name);
                intent.putExtra("is_manager_button", is_manager_button);
                startActivity(intent);
            }
        });

        recyclerFac.setAdapter(facAdapter);

        Button btnResearch = findViewById(R.id.btnResearch);
        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchFacility(type, serial, subcontractor, building, floor, spot);
    }

    //액티비티 불러올때마다 새로고침
    @Override
    protected void onRestart() {
        super.onRestart();

        searchFacility(type, serial, subcontractor, building, floor, spot);
    }

    public void searchFacility(int type, String serial, String subcontractor, String building, String floor, String spot) {

        facAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                ArrayList<Facility> facilities = (ArrayList<Facility>) data;

                for(Facility facility : facilities) {
                    facAdapter.addItem(facility);
                }
                facAdapter.notifyDataSetChanged();
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