package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FacFilterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_filter);

        Resources resources = getResources();
        Spinner spinType = findViewById(R.id.spinType);

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.types));
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spinType.setAdapter(adapterType);
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinBuilding = findViewById(R.id.spinBuilding);
        ArrayAdapter<String> adapterBuilding = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.buildings));
        adapterBuilding.setDropDownViewResource(R.layout.spinner_item);
        spinBuilding.setAdapter(adapterBuilding);

        Spinner spinFloor = findViewById(R.id.spinFloor);
        ArrayAdapter<String> adapterFloor = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.floors));
        adapterFloor.setDropDownViewResource(R.layout.spinner_item);
        spinFloor.setAdapter(adapterFloor);


        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacFilterActivity.this, FacListActivity.class);
                startActivity(intent);
            }
        });


    }
}