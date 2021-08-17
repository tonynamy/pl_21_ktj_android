package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Team;
import com.poogosoft.facmanager.models.UserAttendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AttendActivity extends AppCompatActivity {

    String team_id;
    String place_id;

    RecyclerView recyclerAttend;
    TextView textTeammateResult;

    AttendAdapter attendAdapter;
    ArrayList<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        //Intent에서 가져오기
        team_id = getIntent().getStringExtra("team_id");
        place_id = getIntent().getStringExtra("place_id");

        //뷰에서 가져오기
        recyclerAttend = findViewById(R.id.recyclerAttend);
        textTeammateResult = findViewById(R.id.textTeammateResult);

        //리사이클러 설정
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

                AlertDialog time_dialog = builder.create();

                txtDialogTitle.setText(attendItem.name+ " 출근 상황 수정");

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
                time_dialog.show();

                //확인버튼 눌렀을때
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

                        //출근시간
                        Calendar cal_attend = Calendar.getInstance();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(timeAttend.getHour() < 5) {
                                cal_attend.add(Calendar.DATE, 1);
                            }
                        } else {
                            if(timeAttend.getCurrentHour() < 5) {
                                cal_attend.add(Calendar.DATE, 1);
                            }
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cal_attend.set(Calendar.HOUR_OF_DAY, timeAttend.getHour());
                            cal_attend.set(Calendar.MINUTE, timeAttend.getMinute());
                        } else {
                            cal_attend.set(Calendar.HOUR_OF_DAY, timeAttend.getCurrentHour());
                            cal_attend.set(Calendar.MINUTE, timeAttend.getCurrentMinute());
                        }

                        api.editAttendance(attendItem.id, "0", cal_attend.getTime());

                        //퇴근시간
                        if (attendItem.type == 1) {
                            Calendar calLeave = Calendar.getInstance();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if(timeLeave.getHour() < 5) {
                                    calLeave.add(Calendar.DATE, 1);
                                }
                            } else {
                                if(timeLeave.getCurrentHour() < 5) {
                                    calLeave.add(Calendar.DATE, 1);
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                calLeave.set(Calendar.HOUR_OF_DAY, timeLeave.getHour());
                                calLeave.set(Calendar.MINUTE, timeLeave.getMinute());
                            } else {
                                calLeave.set(Calendar.HOUR_OF_DAY, timeLeave.getCurrentHour());
                                calLeave.set(Calendar.MINUTE, timeLeave.getCurrentMinute());
                            }

                            api.editAttendance(attendItem.id, "1", calLeave.getTime());
                        }

                        time_dialog.dismiss();
                        loadAttendance();
                    }
                });

                txtDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time_dialog.dismiss();
                    }
                });

                btnChangeTeam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time_dialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_spinner, null);
                        builder.setView(view);

                        TextView textDialogTitle = view.findViewById(R.id.textDialogTitle);
                        Spinner spinnerSelectDialog = view.findViewById(R.id.spinnerSelectDialog);
                        TextView textDialogCancel8 = view.findViewById(R.id.textDialogCancel8);
                        TextView textDialogSubmit8 = view.findViewById(R.id.textDialogSubmit8);

                        AlertDialog team_dialog = builder.create();

                        API.APICallback apiCallback = new API.APICallback() {
                            @Override
                            public void onSuccess(Object data) {
                                textDialogTitle.setText(attendItem.name+ " 팀변경");
                                textDialogTitle.setVisibility(View.VISIBLE);

                                teams = (ArrayList<Team>) data;

                                HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<String>(v.getContext(), R.layout.spinner_item, new ArrayList<>());
                                adapter.setDropDownViewResource(R.layout.spinner_item_drop);
                                adapter.isBlack = true;
                                spinnerSelectDialog.setAdapter(adapter);

                                ArrayList<String> team_name = new ArrayList<>();

                                for(Team team : teams) {
                                    team_name.add(team.name);
                                }

                                adapter.addAll(team_name);
                                adapter.notifyDataSetChanged();

                                team_dialog.show();
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                Toast.makeText(AttendActivity.this, "팀정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        };
                        API api = new API.Builder(apiCallback).build();
                        api.getTeams(place_id, team_id);


                        //취소 선택시
                        textDialogCancel8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                team_dialog.dismiss();
                            }
                        });

                        //확인 선택시
                        textDialogSubmit8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                team_dialog.dismiss();

                                API.APICallback apiCallback = new API.APICallback() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        Toast.makeText(AttendActivity.this, "팀을 변경하는데 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        loadAttendance();
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        Toast.makeText(AttendActivity.this, "팀을 변경하는데 실패했습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                                    }
                                };

                                API api = new API.Builder(apiCallback).build();
                                api.editAttendanceTeam(attendItem.id, teams.get(spinnerSelectDialog.getSelectedItemPosition()).id);
                            }
                        });
                    }
                });
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
                api.addAttendance(attendItem.id, String.valueOf(new_type));

            }
        });
        recyclerAttend.setAdapter(attendAdapter);

        //출퇴근정보 가져오기
        loadAttendance();
    }

    private void loadAttendance() {

        attendAdapter.clear();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                ArrayList<UserAttendance> attendances = (ArrayList<UserAttendance>) data;

                if(attendances.size() > 0) {
                    textTeammateResult.setVisibility(View.GONE);
                } else {
                    textTeammateResult.setText("팀원정보가 없습니다.\n웹서비스에서 팀등록버튼을 눌러 팀원을 추가해주세요.");
                }

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
                textTeammateResult.setText("출퇴근정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
            }
        };

        API api = new API.Builder(apiCallback).build();
        api.getAttendances(team_id);
    }
}