package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.facmanager.models.Team;
import com.example.facmanager.models.UserAttendance;

import java.util.ArrayList;
import java.util.Map;


public class AttendActivity extends AppCompatActivity {
    AttendAdapter attendAdapter;

    AttendanceRecord attendanceRecord = new AttendanceRecord();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        String teamId = getIntent().getStringExtra("teamId");

        RecyclerView recyclerAttend = findViewById(R.id.recyclerAttend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAttend.setLayoutManager(layoutManager);
        attendAdapter = new AttendAdapter();
        recyclerAttend.setAdapter(attendAdapter);


        API.APICallback apiCallback = new API.APICallback() {

            @Override
            public void onSuccess(Object data) {

                ArrayList<UserAttendance> attendances = (ArrayList<UserAttendance>) data;

                for(UserAttendance attendance : attendances) {
                    attendanceRecord.addRecord(attendance.user_name, attendance.type);
                }

                for(Map.Entry<String, Integer> record : attendanceRecord.getRecord().entrySet()) {
                    attendAdapter.addItem(
                            new AttendItem(record.getKey(),
                                    (
                                            record.getValue() == AttendanceRecord.NOT_ATTEND ? "출근전" : (record.getValue() == AttendanceRecord.ATTENDED ? "출근" : "퇴근")
                                    )
                            )
                    );
                }

                attendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(AttendActivity.this, "팀 목록을 불러오는데에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getAttendances();

    }
}