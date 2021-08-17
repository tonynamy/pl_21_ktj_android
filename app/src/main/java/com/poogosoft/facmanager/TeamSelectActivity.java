package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Team;

import java.util.ArrayList;

public class TeamSelectActivity extends AppCompatActivity {

    ArrayList<Team> teams = new ArrayList<>();

    TeamItem teamItem;
    TeamAdapter teamAdapter;
    Boolean changeTeamMode;
    String place_id;

    RecyclerView recyclerTeam;
    TextView textTeamResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_select);

        //Intent에서 가져오기
        changeTeamMode = getIntent().getBooleanExtra("changeTeamMode", false);
        place_id = getIntent().getStringExtra("place_id");

        //뷰에서 가져오기
        recyclerTeam = findViewById(R.id.recyclerTeam);
        textTeamResult = findViewById(R.id.textTeamResult);

        //리사이클러뷰 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTeam.setLayoutManager(layoutManager);
        teamAdapter = new TeamAdapter();
        recyclerTeam.setAdapter(teamAdapter);

        //아이템 눌렀을시
        teamAdapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, String team_id, int position) {

                Intent intent = new Intent(v.getContext(), AttendActivity.class);
                intent.putExtra("team_id", team_id);
                intent.putExtra("place_id", place_id);
                startActivity(intent);

            }
        });

        //팀정보 받아오기
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                teams = (ArrayList<Team>) data;

                for(Team team : teams) {
                    teamItem = new TeamItem();
                    teamItem.teamName = team.name;
                    teamItem.teamId = team.id;
                    teamAdapter.addItem(teamItem);
                }

                textTeamResult.setVisibility(View.GONE);
                teamAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed(String errorMsg) {
                if(errorMsg.equals("Not Found")){
                    textTeamResult.setText("검색된 팀이 없습니다.\n웹서비스에서 팀등록버튼을 눌러 팀을 추가해주세요.");
                } else {
                    textTeamResult.setText("팀정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                }
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getTeams(place_id);

    }
}