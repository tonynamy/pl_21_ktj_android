package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_id);

        Resources resource = getResources();

        Spinner spinConstruction = findViewById(R.id.spinConstruction);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, resource.getStringArray(R.array.constructions));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinConstruction.setAdapter(adapter);

    }
}