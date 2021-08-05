package com.example.facmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.facmanager.models.Team;
import com.example.facmanager.models.UserAttendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AttendActivity extends AppCompatActivity {

    String team_id;

    RecyclerView recyclerAttend;
    AttendAdapter attendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        //Intent에서 가져오기
        team_id = getIntent().getStringExtra("team_id");

        //뷰에서 가져오기
        recyclerAttend = findViewById(R.id.recyclerAttend);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAttend.setLayoutManager(layoutManager);

        attendAdapter = new AttendAdapter(new AttendAdapter.OnItemClickListener() {

            @Override
            public void onClick(View v, AttendItem attendItem) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_edit_attendance, null);
                builder.setView(view);

                TextView txtDialogTitle = view.findViewById(R.id.txtDialogTitle);
                LinearLayout layoutAttend = view.findViewById(R.id.layoutAttend);
                TimePicker timeAttend = view.findViewById(R.id.pickerAttend);
                LinearLayout layoutLeave = view.findViewById(R.id.layoutLeave);
                TimePicker timeLeave = view.findViewById(R.id.pickerLeave);
                Button btnChangeTeam = view.findViewById(R.id.btnChangeTeam);
                TextView txtDialogCancel = view.findViewById(R.id.txtDialogCancel);
                TextView txtDialogSubmit = view.findViewById(R.id.txtDialogSubmit);

                AlertDialog dialog = builder.create();

                txtDialogTitle.setText(attendItem.getName()+ " 출근 상황 수정");

                SimpleDateFormat sdf_hour = new SimpleDateFormat("HH");
                SimpleDateFormat sdf_minute = new SimpleDateFormat("mm");

                switch (attendItem.type) {
                    case AttendanceRecord.NOT_ATTEND :
                        layoutAttend.setVisibility(View.GONE);
                        layoutLeave.setVisibility(View.GONE);
                        break;
                    case AttendanceRecord.ATTENDED :
                        layoutAttend.setVisibility(View.VISIBLE);
                        layoutLeave.setVisibility(View.GONE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            timeAttend.setHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                            timeAttend.setMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                        } else {
                            timeAttend.setCurrentHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                            timeAttend.setCurrentMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                        }

                        break;
                    case AttendanceRecord.LEAVED_WORK :
                        layoutAttend.setVisibility(View.VISIBLE);
                        layoutLeave.setVisibility(View.VISIBLE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            timeAttend.setHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                            timeAttend.setMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                        } else {
                            timeAttend.setCurrentHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                            timeAttend.setCurrentMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            timeLeave.setHour(Integer.parseInt(sdf_hour.format(attendItem.leave_date)));
                            timeLeave.setMinute(Integer.parseInt(sdf_minute.format(attendItem.leave_date)));
                        } else {
                            timeLeave.setCurrentHour(Integer.parseInt(sdf_hour.format(attendItem.leave_date)));
                            timeLeave.setCurrentMinute(Integer.parseInt(sdf_minute.format(attendItem.leave_date)));
                        }
                        break;
                }

                txtDialogSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        API.APICallback apiCallback = new API.APICallback() {

                            @Override
                            public void onSuccess(Object data) {

                                Toast.makeText(v.getContext(), "출근기록 업데이트에 성공했습니다", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(String errorMsg) {

                                Toast.makeText(v.getContext(), "출근기록 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
                            }
                        };

                        API api = new API.Builder(apiCallback).build();

                        if (attendItem.type == 0) {

                            Calendar calendar = Calendar.getInstance();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if(timeAttend.getHour() < 5) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                            } else {
                                if(timeAttend.getCurrentHour() < 5) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getHour());
                                calendar.set(Calendar.MINUTE, timeAttend.getMinute());
                            } else {
                                calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getCurrentHour());
                                calendar.set(Calendar.MINUTE, timeAttend.getCurrentMinute());
                            }

                            api.editAttendance(attendItem.getId(), "0", calendar.getTime());


                        } else if (attendItem.type == 1) {

                            Calendar calendar = Calendar.getInstance();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if(timeAttend.getHour() < 5) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                            } else {
                                if(timeAttend.getCurrentHour() < 5) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getHour());
                                calendar.set(Calendar.MINUTE, timeAttend.getMinute());
                            } else {
                                calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getCurrentHour());
                                calendar.set(Calendar.MINUTE, timeAttend.getCurrentMinute());
                            }

                            api.editAttendance(attendItem.getId(), "0", calendar.getTime());

                            Calendar calendar1 = Calendar.getInstance();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if(timeLeave.getHour() < 5) {
                                    calendar1.add(Calendar.DATE, 1);
                                }
                            } else {
                                if(timeLeave.getCurrentHour() < 5) {
                                    calendar1.add(Calendar.DATE, 1);
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                calendar1.set(Calendar.HOUR_OF_DAY, timeLeave.getHour());
                                calendar1.set(Calendar.MINUTE, timeLeave.getMinute());
                            } else {
                                calendar1.set(Calendar.HOUR_OF_DAY, timeLeave.getCurrentHour());
                                calendar1.set(Calendar.MINUTE, timeLeave.getCurrentMinute());
                            }

                            api.editAttendance(attendItem.getId(), "1", calendar1.getTime());
                        }

                        //onAttendanceUpdateListener.onUpdate();
                        dialog.dismiss();

                    }
                });

                txtDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnChangeTeam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AttendFilterActivity.class);
                        Boolean changeTeamMode = true;
                        intent.putExtra("changeTeamMode", changeTeamMode);
                        intent.putExtra("user_id", attendItem.id);
                        ((Activity)v.getContext()).startActivityForResult(intent, 200);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }


        }, new AttendAdapter.OnAttendBtnClickListener() {
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

       // attendAdapter.team_id = team_id;
        recyclerAttend.setAdapter(attendAdapter);

        loadAttendance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200) {

            if(resultCode == RESULT_OK) {

                String team_id = data.getStringExtra("team_id");
                String user_id = data.getStringExtra("user_id");

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Toast.makeText(AttendActivity.this, "팀을 변경하는데 성공했습니다. ", Toast.LENGTH_SHORT).show();
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