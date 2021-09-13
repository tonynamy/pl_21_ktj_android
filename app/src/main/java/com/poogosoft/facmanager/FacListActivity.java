package com.poogosoft.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Facility;

import java.util.ArrayList;

public class FacListActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    String super_manager_name;
    int button_right;

    String serial;
    int type;
    String subcontractor;
    String building;
    String floor;
    String spot;

    TextView textFacListTitle;
    TextView textSeachItemNum;
    ImageView imageFilter;
    String filter_string;
    RecyclerView recyclerFac;
    Button btnResearch;

    FacAdapter facAdapter = new FacAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_list);

        //Intent Extra 값 받아오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        super_manager_name = getIntent().getStringExtra("super_manager_name");
        button_right = getIntent().getIntExtra("button_right", 0);

        serial = getIntent().getStringExtra("serial");
        type = getIntent().getIntExtra("type", -1);
        subcontractor = getIntent().getStringExtra("subcontractor");
        building = getIntent().getStringExtra("building");
        floor = getIntent().getStringExtra("floor");
        spot = getIntent().getStringExtra("spot");

        //뷰 불러오기
        textFacListTitle = findViewById(R.id.textFacListTitle);
        textSeachItemNum = findViewById(R.id.textSeachItemNum);
        imageFilter = findViewById(R.id.imageFilter);
        recyclerFac = findViewById(R.id.recyclerFac);
        btnResearch = findViewById(R.id.btnResearch);

        //액티비티 타이틀 정하기
        if(button_right == ButtonRight.MANAGER){
            textFacListTitle.setText("관리자 작업조회");
        } else if(button_right == ButtonRight.TEAM_LEADER) {
            textFacListTitle.setText("팀장님 작업조회");
        } else {
            if(super_manager_name.equals("")){
                textFacListTitle.setText("전체 조회");
            }else if(super_manager_name.contains(" ")) {
                textFacListTitle.setText(super_manager_name + " 조회");
            } else {
                textFacListTitle.setText(super_manager_name + " 담당자 조회");
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerFac.setLayoutManager(layoutManager);
        recyclerFac.setAdapter(facAdapter);

        //아이템 클릭시
        facAdapter.setOnFacItemClicked(new FacAdapter.OnFacItemClicked() {
            @Override
            public void onClick(View v, Facility facility) {
                Intent intent = new Intent(FacListActivity.this, FacilityActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("team_id", team_id);
                intent.putExtra("facility_id", facility.id);
                intent.putExtra("super_manager_name", super_manager_name);
                intent.putExtra("button_right", button_right);
                startActivity(intent);
            }
        });

        //필터아이콘 클릭시
        imageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_faclist_filter, null);
                builder.setView(view);

                ArrayList<CheckBox> checkBoxes = new ArrayList<>();
                CheckBox checkPlan = view.findViewById(R.id.checkPlan);
                CheckBox checkStart = view.findViewById(R.id.checkStart);
                CheckBox checkFinish = view.findViewById(R.id.checkFinish);
                CheckBox checkEditStart = view.findViewById(R.id.checkEditStart);
                CheckBox checkEditFinish = view.findViewById(R.id.checkEditFinish);
                CheckBox checkDisStart = view.findViewById(R.id.checkDisStart);
                CheckBox checkDisFinish = view.findViewById(R.id.checkDisFinish);
                checkBoxes.add(checkPlan);
                checkBoxes.add(checkStart);
                checkBoxes.add(checkFinish);
                checkBoxes.add(checkEditStart);
                checkBoxes.add(checkEditFinish);
                checkBoxes.add(checkDisStart);
                checkBoxes.add(checkDisFinish);
                TextView textDialogDelFilter = view.findViewById(R.id.textDialogDelFilter);
                TextView textDialogCancel10 = view.findViewById(R.id.textDialogCancel10);
                TextView textDialogSubmit10 = view.findViewById(R.id.textDialogSubmit10);

                String[] state_array;
                if(filter_string != null) {
                    state_array = filter_string.split("");

                    for(String filter_key : state_array) {
                        switch (filter_key) {
                            case "a": checkPlan.setChecked(true); break;
                            case "b": checkStart.setChecked(true); break;
                            case "c": checkFinish.setChecked(true); break;
                            case "d": checkEditStart.setChecked(true); break;
                            case "e": checkEditFinish.setChecked(true); break;
                            case "f": checkDisStart.setChecked(true); break;
                            case "g": checkDisFinish.setChecked(true); break;
                        }
                    }
                }
                AlertDialog dialog = builder.create();
                dialog.show();

                //전체필터해제 선택시
                textDialogDelFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(CheckBox this_checkbox : checkBoxes) {
                            this_checkbox.setChecked(false);
                        }
                    }
                });

                //취소 선택시
                textDialogCancel10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //확인 선택시
                textDialogSubmit10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filter_string = null;
                        for(int i = 0; i < checkBoxes.size(); i++) {
                            CheckBox this_checkbox = checkBoxes.get(i);
                            if(this_checkbox.isChecked()) {
                                switch (i) {
                                    case 0: filter_string += "a"; break;
                                    case 1: filter_string += "b"; break;
                                    case 2: filter_string += "c"; break;
                                    case 3: filter_string += "d"; break;
                                    case 4: filter_string += "e"; break;
                                    case 5: filter_string += "f"; break;
                                    case 6: filter_string += "g"; break;
                                }
                            }
                        }
                        //설비정보 불러오기
                        searchFacility();

                        dialog.dismiss();
                    }
                });
            }
        });

        //다시검색 버튼 클릭시
        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //설비정보 불러오기
        searchFacility();
    }

    //액티비티 불러올때마다 새로고침
    @Override
    protected void onRestart() {
        super.onRestart();

        searchFacility();
    }

    public void searchFacility() {

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
        api.searchFacility(place_id, serial, type, super_manager_name, subcontractor, building, floor, spot, button_right, filter_string);

    }
}