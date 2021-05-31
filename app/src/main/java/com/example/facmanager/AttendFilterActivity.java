package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.facmanager.models.Team;

import java.util.ArrayList;

public class AttendFilterActivity extends AppCompatActivity {

    ArrayList<Team> teams = new ArrayList<>();
    ArrayList<String> teamNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_filter);

        Resources resources = getResources();
        Spinner spinTeam = findViewById(R.id.spinTeam);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, teamNames);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinTeam.setAdapter(arrayAdapter);

        spinTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AttendFilterActivity.this, AttendActivity.class);
                String teamId = teams.get(position).id;
                intent.putExtra("teamId", teamId);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        API.APICallback apiCallback = new API.APICallback() {

            @Override
            public void onSuccess(Object data) {

                ArrayList<Team> _teams = (ArrayList<Team>) data;

                teams.clear();
                teamNames.clear();

                teams = _teams;
                for(Team team : _teams) {
                    teamNames.add(team.name);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(AttendFilterActivity.this, "팀 목록을 불러오는데에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getTeams();

    }
}