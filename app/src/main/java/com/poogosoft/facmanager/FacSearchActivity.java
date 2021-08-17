package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.FacilityInfo;

import java.util.ArrayList;
import java.util.Arrays;

public class FacSearchActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    String super_manager_name;
    int button_right;

    TextView textFacFilterTitle;
    TextView textFacSearch;
    LinearLayout layoutFacSearch;
    EditText eTextSerialNum;
    Button btnSearch;

    ArrayList<String> types = new ArrayList<>();
    ArrayList<String> subcontractors = new ArrayList<>();
    ArrayList<String> buildings = new ArrayList<>();
    ArrayList<String> floors = new ArrayList<>();
    ArrayList<String> spots = new ArrayList<>();

    HintSpinnerAdapter<String> adapterType;
    HintSpinnerAdapter<String> adapterSubCont;
    HintSpinnerAdapter<String> adapterBuilding;
    HintSpinnerAdapter<String> adapterFloor;
    HintSpinnerAdapter<String> adapterSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_search);

        //Intent에서 가져오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        super_manager_name = getIntent().getStringExtra("super_manager_name");
        if(super_manager_name == null){ super_manager_name = ""; }
        button_right = getIntent().getIntExtra("button_right", 0);

        //뷰에서 가져오기
        textFacFilterTitle = findViewById(R.id.textFacFilterTitle);
        textFacSearch = findViewById(R.id.textFacSearch);
        layoutFacSearch = findViewById(R.id.layoutFacSearch);
        eTextSerialNum = findViewById(R.id.eTextSerialNum);
        btnSearch = findViewById(R.id.btnSearch);

        layoutFacSearch.setVisibility(View.GONE);

        if(button_right == ButtonRight.MANAGER){
            textFacFilterTitle.setText("관리자 작업조회");
        } else if(button_right == ButtonRight.TEAM_LEADER) {
            textFacFilterTitle.setText("팀장님 작업조회");
        } else {
            textFacFilterTitle.setText(super_manager_name + " 담당자 조회");
        }

        //공종스피너
        Spinner spinType = findViewById(R.id.spinType);
        adapterType = new HintSpinnerAdapter(this, R.layout.spinner_item, types);
        adapterType.setDropDownViewResource(R.layout.spinner_item_drop);
        spinType.setAdapter(adapterType);

        //사용업체 스피너
        Spinner spinSubcont = findViewById(R.id.spinSubcont);
        adapterSubCont = new HintSpinnerAdapter(this, R.layout.spinner_item, subcontractors);
        adapterSubCont.setDropDownViewResource(R.layout.spinner_item_drop);
        spinSubcont.setAdapter(adapterSubCont);

        //설치동 스피너
        Spinner spinBuilding = findViewById(R.id.spinBuilding);
        adapterBuilding = new HintSpinnerAdapter(this, R.layout.spinner_item, buildings);
        adapterBuilding.setDropDownViewResource(R.layout.spinner_item_drop);
        spinBuilding.setAdapter(adapterBuilding);

        //층 스피너
        Spinner spinFloor = findViewById(R.id.spinFloor);
        adapterFloor = new HintSpinnerAdapter(this, R.layout.spinner_item, floors);
        adapterFloor.setDropDownViewResource(R.layout.spinner_item_drop);
        spinFloor.setAdapter(adapterFloor);

        //설치위치 스피너
        Spinner spinSpot = findViewById(R.id.spinSpot);
        adapterSpot = new HintSpinnerAdapter(this, R.layout.spinner_item, spots);
        adapterSpot.setDropDownViewResource(R.layout.spinner_item_drop);
        spinSpot.setAdapter(adapterSpot);

        //검색버튼 눌렀을시
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacListActivity.class);

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

                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("team_id", team_id);
                intent.putExtra("super_manager_name", super_manager_name);
                intent.putExtra("button_right", button_right);

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

                textFacSearch.setVisibility(View.GONE);

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

                layoutFacSearch.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailed(String errorMsg) {
                if(errorMsg.equals("Not Found")) {
                    textFacSearch.setText("설치계획정보가 없습니다.\n웹서비스에서 도면등록버튼을 눌러주세요.");
                } else {
                    textFacSearch.setText("검색정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                }
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getFacilitySearchInfo(place_id, super_manager_name);

    }
}