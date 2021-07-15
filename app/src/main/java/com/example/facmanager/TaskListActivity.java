package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TaskListActivity extends AppCompatActivity {

    Button buttonAllFacility;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        place_id = getIntent().getStringExtra("place_id");

        buttonAllFacility = findViewById(R.id.buttonAllFacility);

        buttonAllFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                intent.putExtra("place_id", place_id);
                startActivity(intent);
            }
        });
    }
}