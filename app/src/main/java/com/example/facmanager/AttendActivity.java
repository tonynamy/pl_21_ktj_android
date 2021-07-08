package com.example.facmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.facmanager.models.Team;
import com.example.facmanager.models.UserAttendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AttendActivity extends AppCompatActivity {
    AttendAdapter attendAdapter;

    AttendanceRecord attendanceRecord = new AttendanceRecord();

    String team_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        team_id = getIntent().getStringExtra("team_id");


        RecyclerView recyclerAttend = findViewById(R.id.recyclerAttend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAttend.setLayoutManager(layoutManager);
        attendAdapter = new AttendAdapter(new AttendAdapter.OnAttendBtnClickListener() {
            @Override
            public void onClick(View v, AttendItem attendItem, int new_type) {

                API.APICallback apiCallback = new API.APICallback() {

                    @Override
                    public void onSuccess(Object data) {

                        Toast.makeText(AttendActivity.this, "출근기록 업데이트에 성공했습니다", Toast.LENGTH_SHORT).show();

                        loadAttendance();
                    }

                    @Override
                    public void onFailed(String errorMsg) {

                        Toast.makeText(AttendActivity.this, "출근기록 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                };

                API api = new API.Builder(apiCallback).build();

                api.addAttendance(attendItem.getId(), String.valueOf(new_type));

            }
        }, new AttendAdapter.OnAttendanceUpdateListner() {
            @Override
            public void onUpdate() {
                loadAttendance();
            }
        });

        attendAdapter.team_id = team_id;
        recyclerAttend.setAdapter(attendAdapter);

        loadAttendance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {

            if (resultCode == RESULT_OK) {

                String team_id = data.getStringExtra("team_id");
                String user_id = data.getStringExtra("user_id");

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Toast.makeText(AttendActivity.this, "팀을 변경하는데 성공했습니다.", Toast.LENGTH_SHORT).show();
                        loadAttendance();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(AttendActivity.this, "팀을 변경하는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };

                API api = new API.Builder(apiCallback).build();

                api.editTeam(user_id, team_id);

            }
        }
    }

    private void loadAttendance() {

        attendAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                ArrayList<UserAttendance> attendances = (ArrayList<UserAttendance>) data;

                HashMap<String, AttendItem> record = new HashMap<>();

                for(UserAttendance attendance : attendances) {

                    if (!record.containsKey(attendance.id)) {
                        record.put(attendance.id, new AttendItem(attendance.id, attendance.name));
                    }

                    if(attendance.type == 0 ) {
                        record.get(attendance.id).attend_date = attendance.date;
                        record.get(attendance.id).type = record.get(attendance.id).type < 0 ? 0 : record.get(attendance.id).type;
                    } else if (attendance.type == 1) {
                        record.get(attendance.id).leave_date = attendance.date;
                        record.get(attendance.id).type = 1;
                    } else {
                        record.get(attendance.id).type = -1;
                    }

                }

                for(AttendItem attendItem : record.values()) {
                    attendAdapter.addItem( attendItem );
                }

                attendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(AttendActivity.this, "팀목록을 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getAttendances(team_id);
    }
}