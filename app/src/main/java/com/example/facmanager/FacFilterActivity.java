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

import java.util.ArrayList;

public class FacFilterActivity extends AppCompatActivity {

    String facSerialNum;
    String facType;
    String facSubCont;
    String facBuilding;
    String facFloor;
    String facSpot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_filter);

        Resources resources = getResources();
        Spinner spinType = findViewById(R.id.spinType);

        //승인번호 에디트텍스트
        EditText eTextSerialNum = findViewById(R.id.eTextSerialNum);

        //공종스피너
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.types));
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spinType.setAdapter(adapterType);
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facType = (String) spinType.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //사용업체 스피너
        Spinner spinSubcont = findViewById(R.id.spinSubcont);
        ArrayAdapter<String> adapterSubCont = new ArrayAdapter<>(this, R.layout.spinner_item, resources.getStringArray(R.array.subcontractors));
        adapterSubCont.setDropDownViewResource(R.layout.spinner_item);
        spinSubcont.setAdapter(adapterSubCont);
        spinSubcont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facSubCont = (String) spinSubcont.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //설치동 스피너
        Spinner spinBuilding = findViewById(R.id.spinBuilding);
        ArrayAdapter<String> adapterBuilding = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.buildings));
        adapterBuilding.setDropDownViewResource(R.layout.spinner_item);
        spinBuilding.setAdapter(adapterBuilding);
        spinBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facBuilding = (String) spinBuilding.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //층 스피너
        Spinner spinFloor = findViewById(R.id.spinFloor);
        ArrayAdapter<String> adapterFloor = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.floors));
        adapterFloor.setDropDownViewResource(R.layout.spinner_item);
        spinFloor.setAdapter(adapterFloor);
        spinFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facFloor = (String) spinFloor.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //설치위치 스피너
        Spinner spinSpot = findViewById(R.id.spinSpot);
        ArrayAdapter adapterSpot = new ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.spots));
        adapterSpot.setDropDownViewResource(R.layout.spinner_item);
        spinSpot.setAdapter(adapterSpot);
        spinSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facSpot = (String) spinSpot.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacFilterActivity.this, FacListActivity.class);
                facSerialNum = eTextSerialNum.getText().toString();
                Toast.makeText(v.getContext(), facSerialNum + ", " + facType + ", " + facSubCont + ", " + facBuilding + " " + facFloor + " " + facSpot, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


    }
}