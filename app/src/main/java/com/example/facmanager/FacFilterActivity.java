package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.facmanager.models.Facility;
import com.example.facmanager.models.FacilityInfo;
import com.example.facmanager.models.Team;

import java.util.ArrayList;
import java.util.Arrays;

public class FacFilterActivity extends AppCompatActivity {

    String facSerialNum;
    String facType;
    String facSubCont;
    String facBuilding;
    String facFloor;
    String facSpot;


    ArrayList<String> types = new ArrayList<>();
    ArrayList<String> subcontractors = new ArrayList<>();
    ArrayList<String> buildings = new ArrayList<>();
    ArrayList<String> floors = new ArrayList<>();
    ArrayList<String> spots = new ArrayList<>();

    FacFilterAdapter<String> adapterType;
    FacFilterAdapter<String> adapterSubCont;
    FacFilterAdapter<String> adapterBuilding;
    FacFilterAdapter<String> adapterFloor;
    FacFilterAdapter<String> adapterSpot;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_filter);

        place_id = getIntent().getStringExtra("place_id");

        Spinner spinType = findViewById(R.id.spinType);

        //승인번호 에디트텍스트
        EditText eTextSerialNum = findViewById(R.id.eTextSerialNum);

        //공종스피너
        adapterType = new FacFilterAdapter(this, R.layout.spinner_item, types);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spinType.setAdapter(adapterType);

        //사용업체 스피너
        Spinner spinSubcont = findViewById(R.id.spinSubcont);
        adapterSubCont = new FacFilterAdapter(this, R.layout.spinner_item, subcontractors);
        adapterSubCont.setDropDownViewResource(R.layout.spinner_item);
        spinSubcont.setAdapter(adapterSubCont);

        //설치동 스피너
        Spinner spinBuilding = findViewById(R.id.spinBuilding);
        adapterBuilding = new FacFilterAdapter(this, R.layout.spinner_item, buildings);
        adapterBuilding.setDropDownViewResource(R.layout.spinner_item);
        spinBuilding.setAdapter(adapterBuilding);

        //층 스피너
        Spinner spinFloor = findViewById(R.id.spinFloor);
        adapterFloor = new FacFilterAdapter(this, R.layout.spinner_item, floors);
        adapterFloor.setDropDownViewResource(R.layout.spinner_item);
        spinFloor.setAdapter(adapterFloor);

        //설치위치 스피너
        Spinner spinSpot = findViewById(R.id.spinSpot);
        adapterSpot = new FacFilterAdapter(this, R.layout.spinner_item, spots);
        adapterSpot.setDropDownViewResource(R.layout.spinner_item);
        spinSpot.setAdapter(adapterSpot);


        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacFilterActivity.this, FacListActivity.class);

                String serial = eTextSerialNum.getText().toString().equals("") ? "" : eTextSerialNum.getText().toString();

                int type = spinType.getSelectedItemPosition();

                String subcontractor = "";

                if(spinSubcont.getSelectedItemPosition() > 0) {
                    subcontractor = spinSubcont.getSelectedItem().toString();
                }

                String building = "";

                if(spinBuilding.getSelectedItemPosition() > 0) {
                    building = spinBuilding.getSelectedItem().toString();
                }

                String floor = "";

                if(spinFloor.getSelectedItemPosition() > 0) {
                    floor = spinFloor.getSelectedItem().toString();
                }

                String spot = "";

                if(spinSpot.getSelectedItemPosition() > 0) {
                    spot = spinSpot.getSelectedItem().toString();
                }

                intent.putExtra("place_id", place_id);
                intent.putExtra("serial", serial);
                intent.putExtra("type", type);
                intent.putExtra("subcontractor", subcontractor);
                intent.putExtra("building", building);
                intent.putExtra("floor", floor);
                intent.putExtra("spot", spot);


                startActivity(intent);
            }
        });

        getFacilityInfo();


    }

    public void getFacilityInfo() {

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                FacilityInfo facilityInfo = (FacilityInfo) data;


                int[] int_types = facilityInfo.types;
                String[] str_types = new String[int_types.length];

                String[] type_name = {"설비", "전기", "건축", "기타"};

                int i = 0;

                for(int int_type : int_types) {

                    str_types[i++] = type_name[int_type-1];
                }

                types.clear();
                subcontractors.clear();
                buildings.clear();
                floors.clear();
                spots.clear();

                types.addAll(Arrays.asList(str_types));
                subcontractors.addAll(Arrays.asList(facilityInfo.subcontractors));
                buildings.addAll(Arrays.asList(facilityInfo.buildings));
                floors.addAll(Arrays.asList(facilityInfo.floors));
                spots.addAll(Arrays.asList(facilityInfo.spots));

                types.add(0, "공종");
                subcontractors.add(0, "사용업체");
                buildings.add(0, "설치동");
                floors.add(0, "층");
                spots.add(0, "장소");

                adapterType.notifyDataSetChanged();
                adapterSubCont.notifyDataSetChanged();
                adapterBuilding.notifyDataSetChanged();
                adapterFloor.notifyDataSetChanged();
                adapterSpot.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(FacFilterActivity.this, "설비 필터를 로딩하는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getFacilityInfo();

    }
}