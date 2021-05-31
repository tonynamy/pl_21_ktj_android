package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AttendFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_filter);

        Resources resources = getResources();
        Spinner spinTeam = findViewById(R.id.spinTeam);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.teams));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinTeam.setAdapter(arrayAdapter);

        spinTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AttendFilterActivity.this, AttendActivity.class);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}