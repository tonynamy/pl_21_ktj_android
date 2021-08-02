package com.example.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facmanager.models.Facility;
import com.example.facmanager.models.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FacilityActivity extends AppCompatActivity {

    int level;
    String facility_id;
    String super_manager_name;
    Boolean is_manager_button;

    Facility facility = new Facility();

    TextView textFacSerial;
    SeekBar seekBarTask;
    TextView textTaskState;
    Button buttonFacManger;

    TextView textFacType;
    TextView textSuperManager;
    LinearLayout layoutPurpose;
    TextView textPurpose;
    TextView textFacSubCon;
    TextView textFacSpot;
    TextView textExpiredDate;

    LinearLayout layoutTeamLeader;
    Button buttonCreate;
    Button buttonEdit;
    Button buttonDisassem;
    Button buttonTaskPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        //Intent Extra 값 받아오기
        level = getIntent().getIntExtra("level", -2);
        facility_id = getIntent().getStringExtra("facility_id");
        super_manager_name = getIntent().getStringExtra("super_manager_name");
        is_manager_button = getIntent().getBooleanExtra("is_manager_button", false);

        //뷰 불러오기
        textFacSerial = findViewById(R.id.textFacSerial);
        seekBarTask = findViewById(R.id.seekBarTask);
        textTaskState = findViewById(R.id.textTaskState);
        buttonFacManger = findViewById(R.id.buttonFacManger);

        textFacType = findViewById(R.id.textFacType);
        textSuperManager = findViewById(R.id.textSuperManager);
        layoutPurpose = findViewById(R.id.layoutPurpose);
        textPurpose = findViewById(R.id.textPurpose);
        textFacSubCon = findViewById(R.id.textFacSubCon);
        textFacSpot = findViewById(R.id.textFacSpot);
        textExpiredDate = findViewById(R.id.textExpiredDate);

        layoutTeamLeader = findViewById(R.id.layoutTeamLeader);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDisassem = findViewById(R.id.buttonDisassem);
        buttonTaskPlan = findViewById(R.id.buttonTaskPlan);


        //SeekBar에 터치가 안되게
        seekBarTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //Facility 정보 가져오기
        getFacility();

        /*
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                Facility facility = (Facility) data;

                setFacility(facility);
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(FacilityActivity.this, "설비목록을 검색하는데에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getFacility(facility_id);
        */

        //권한이 필요한 모든 뷰 비활성화
        buttonFacManger.setVisibility(View.GONE);
        layoutTeamLeader.setVisibility(View.GONE);
        buttonTaskPlan.setVisibility(View.GONE);

        //관리자 버튼으로 왔을시
        if(is_manager_button) {
            buttonFacManger.setVisibility(View.VISIBLE);
            buttonTaskPlan.setVisibility(View.VISIBLE);
        } else if(super_manager_name.isEmpty()) {
            layoutTeamLeader.setVisibility(View.VISIBLE);
        } else {
            if(level == 2){
                buttonFacManger.setVisibility(View.VISIBLE);
                buttonTaskPlan.setVisibility(View.VISIBLE);
            } else if(level == 1) {
                layoutTeamLeader.setVisibility(View.VISIBLE);
            }
        }

        //상태변경 버튼 눌렀을때
        buttonFacManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_task_manage, null);
                builder.setView(view);

                TextView textFacReady = view.findViewById(R.id.textFacReady);
                TextView textFacCreate = view.findViewById(R.id.textFacCreate);
                TextView textFacFinish = view.findViewById(R.id.textFacFinish);
                TextView textFacEdit = view.findViewById(R.id.textFacEdit);
                TextView textFacEditFin = view.findViewById(R.id.textFacEditFin);
                TextView textFacDisassem = view.findViewById(R.id.textFacDisassem);
                TextView textFacDisFin = view.findViewById(R.id.textFacDisFin);
                TextView textFacDialogCancel = view.findViewById(R.id.textFacDialogCancel);

                textFacFinish.setVisibility(View.GONE);
                textFacEdit.setVisibility(View.GONE);
                textFacEditFin.setVisibility(View.GONE);
                textFacDisassem.setVisibility(View.GONE);
                textFacDisFin.setVisibility(View.GONE);

                if(facility.started_at != null) {
                    textFacFinish.setVisibility(View.VISIBLE);
                }
                if(facility.finished_at != null) {
                    textFacEdit.setVisibility(View.VISIBLE);
                    textFacDisassem.setVisibility(View.VISIBLE);
                }
                if(facility.edit_started_at != null) {
                    textFacEditFin.setVisibility(View.VISIBLE);
                }
                if(facility.dis_started_at != null) {
                    textFacDisFin.setVisibility(View.VISIBLE);
                }

                AlertDialog dialog = builder.create();
                dialog.show();

                ArrayList<TextView> FacStateTextViews = new ArrayList<>();

                FacStateTextViews.add(textFacReady);
                FacStateTextViews.add(textFacCreate);
                FacStateTextViews.add(textFacFinish);
                FacStateTextViews.add(textFacEdit);
                FacStateTextViews.add(textFacEditFin);
                FacStateTextViews.add(textFacDisassem);
                FacStateTextViews.add(textFacDisFin);

                for(int i = 0; i < FacStateTextViews.size(); i++) {

                    final int state_type = i;

                    FacStateTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            API.APICallback apiCallback = new API.APICallback() {
                                @Override
                                public void onSuccess(Object data) {
                                    getFacility();
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "진행상황 변경에 성공했습니다", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "진행상황 변경에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            };

                            API api = new API.Builder(apiCallback).build();

                            api.editFacilityState(facility_id, state_type);
                        }
                    });

                }

                textFacDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //담당자 등록하기
        textSuperManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //직원등급이 관리자(2), 최고관리자(3), 어드민(4)일때
                if(level == 2 || level == 3 || level == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_text_input, null);
                    builder.setView(view);

                    EditText eTextDialogInput = view.findViewById(R.id.eTextDialogInput);
                    TextView textDialogCancel5 = view.findViewById(R.id.textDialogCancel5);
                    TextView textDialogSubmit5 = view.findViewById(R.id.textDialogSubmit5);
                    eTextDialogInput.setHint("담당자");

                    if(!facility.super_manager.isEmpty()){
                        eTextDialogInput.setText(facility.super_manager);
                    }

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    textDialogCancel5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    textDialogSubmit5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    textDialogSubmit5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            API.APICallback apiCallback = new API.APICallback() {
                                @Override
                                public void onSuccess(Object data) {
                                    getFacility();
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "담당자 변경에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "담당자 변경에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            };

                            API api = new API.Builder(apiCallback).build();

                            api.editFacilitySuperManager(facility_id, eTextDialogInput.getText().toString());
                        }
                    });
                }
            }
        });

        //설치목적 작성하기
        textPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //직원등급이 관리자(2), 최고관리자(3), 어드민(4)일때
                if(level == 2 || level == 3 || level == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_text_input, null);
                    builder.setView(view);

                    EditText eTextDialogInput = view.findViewById(R.id.eTextDialogInput);
                    TextView textDialogCancel5 = view.findViewById(R.id.textDialogCancel5);
                    TextView textDialogSubmit5 = view.findViewById(R.id.textDialogSubmit5);
                    eTextDialogInput.setHint("설치목적");

                    if(!facility.purpose.isEmpty()){
                        eTextDialogInput.setText(facility.purpose);
                    }

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    textDialogCancel5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    textDialogSubmit5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            API.APICallback apiCallback = new API.APICallback() {
                                @Override
                                public void onSuccess(Object data) {
                                    getFacility();
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "설치목적 변경에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "설치목적 변경에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            };

                            API api = new API.Builder(apiCallback).build();

                            api.editFacilityPurpose(facility_id, eTextDialogInput.getText().toString());
                        }
                    });
                }
            }
        });

        //만료일등록을 눌렀을때
        textExpiredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facility.finished_at != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_expired_date, null);
                    builder.setView(view);

                    TextView textAfter3month = view.findViewById(R.id.textAfter3month);
                    TextView textAfter6month = view.findViewById(R.id.textAfter6month);
                    TextView textDirectInput = view.findViewById(R.id.textDirectInput);
                    DatePicker pickerExpiredDate = view.findViewById(R.id.pickerExpiredDate);
                    TextView textDialogCancel3 = view.findViewById(R.id.textDialogCancel3);
                    TextView textDialogSubmit3 = view.findViewById(R.id.textDialogSubmit3);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    textAfter3month.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PickerExpiredDate(pickerExpiredDate,3);
                            pickerExpiredDate.setVisibility(View.VISIBLE);
                        }
                    });

                    textAfter6month.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PickerExpiredDate(pickerExpiredDate,6);
                            pickerExpiredDate.setVisibility(View.VISIBLE);
                        }
                    });

                    textDirectInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(facility.expired_at != null)
                                PickerExpiredDate(pickerExpiredDate,0);
                            pickerExpiredDate.setVisibility(View.VISIBLE);
                        }
                    });

                    textDialogCancel3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    textDialogSubmit3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(pickerExpiredDate.getVisibility() == View.VISIBLE){

                                Calendar calExpiredDate = Calendar.getInstance();
                                calExpiredDate.set(pickerExpiredDate.getYear(), pickerExpiredDate.getMonth(), pickerExpiredDate.getDayOfMonth());

                                Date expired_at = calExpiredDate.getTime();

                                API.APICallback apiCallback = new API.APICallback() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        getFacility();
                                        dialog.dismiss();
                                        Toast.makeText(v.getContext(), "만료일 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailed(String errorMsg) {
                                        dialog.dismiss();
                                        Toast.makeText(v.getContext(), "만료일 등록에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                                    }
                                };

                                API api = new API.Builder(apiCallback).build();

                                api.editFacilityExpiredAt(facility_id, expired_at);

                            }
                        }
                    });
                }
            }
        });

        //작업계획버튼 눌렀을 때
        buttonTaskPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_plan, null);
                builder.setView(view);

                RadioGroup radioGroupPlan = view.findViewById(R.id.radioGroupPlan);
                RadioButton radioStart = view.findViewById(R.id.radioStart);
                RadioButton radioEdit = view.findViewById(R.id.radioEdit);
                RadioButton radioDis = view.findViewById(R.id.radioDis);
                Spinner spinPlanTeam = view.findViewById(R.id.spinPlanTeam);
                TextView textDialogDelPlan = view.findViewById(R.id.textDialogDelPlan);
                TextView textDialogCancel4 = view.findViewById(R.id.textDialogCancel4);
                TextView textDialogSubmit4 = view.findViewById(R.id.textDialogSubmit4);

                if(textTaskState.getText() == "설치전") {
                    radioStart.toggle();
                    radioEdit.setVisibility(View.GONE);
                    radioDis.setVisibility(View.GONE);
                } else {
                    radioStart.setVisibility(View.GONE);
                }

                TeamSpinnerAdapter teamAdapter = new TeamSpinnerAdapter(v.getContext(), R.layout.spinner_item, new ArrayList<>());
                teamAdapter.setDropDownViewResource(R.layout.spinner_item_drop);

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {

                        ArrayList<Team> teams = (ArrayList<Team>) data;

                        for(Team team : teams) {
                            teamAdapter.add(team);
                        }

                        teamAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(v.getContext(), "팀목록을 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };

                API api = new API.Builder(apiCallback).build();

                api.getTeams();

                spinPlanTeam.setAdapter(teamAdapter);

                AlertDialog dialog = builder.create();

                textDialogDelPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                textDialogCancel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                textDialogSubmit4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String facility_id = facility.id;
                        String team_id = teamAdapter.getItem(spinPlanTeam.getSelectedItemPosition()).id;
                        int plan = -1;

                        if(radioStart.isChecked()){
                            plan = 1;
                        } else if(radioEdit.isChecked()) {
                            plan = 2;
                        } else if(radioDis.isChecked()) {
                            plan = 3;
                        }

                        API.APICallback apiCallback = new API.APICallback() {
                            @Override
                            public void onSuccess(Object data) {
                                Toast.makeText(v.getContext(), "작업계획 수정에 성공했습니다", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                Toast.makeText(v.getContext(), "작업계획 수정에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        };

                        API api = new API.Builder(apiCallback).build();

                        api.editFacilityTaskPlan(facility_id, team_id, plan);
                    }
                });

                dialog.show();
            }
        });
    }

    private void getFacility() {
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                Facility facility = (Facility) data;

                setFacility(facility);
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(FacilityActivity.this, "설비목록을 검색하는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();

        api.getFacility(facility_id);
    }

    private void setFacility(Facility facility) {
        this.facility = facility;

        onFacilityChanged();
    }

    private void onFacilityChanged() {

        //해체완료dis_finished_at부터 설치시작started_at으로 (끝에서 앞으로) 날짜정보가 있는지 확인해나간다
        //해체완료dis_finished_at이 있으면 해체완료 없으면 앞으로
        //해체시작dis_started_at이 있으면 해체시작 없으면 앞으로
        //수정완료edit_finished_at이 있으면 수정완료 없으면 앞으로
        //수정시작edit_started_at이 있으면 수정시작 없으면 앞으로
        //승인완료finished_at이 있으면 승인완료 없으면 앞으로
        //설치중started_at이 있으면 설치중 없으면 설치전
        if(facility.dis_finished_at != null) {
            State("해체완료");
        } else if(facility.dis_started_at != null) {
            State("해체중");
        } else if(facility.edit_finished_at != null) {
            State("수정완료");
        } else if(facility.edit_started_at != null) {
            State("수정중");
        } else if(facility.finished_at != null) {
            State("승인완료");
        } else  if(facility.started_at != null) {
            State("설치중");
        } else {
            State("설치전");
        }

        textFacSerial.setText(facility.serial);
        String stringFacType = "";
        switch (facility.type) {
            case 1:
                stringFacType = "설비";
                break;
            case 2:
                stringFacType = "전기";
                break;
            case 3:
                stringFacType = "건축";
                break;
            case 4:
                stringFacType = "기타";
                break;
            default:
                break;
        }
        textFacType.setText(stringFacType);
        textFacSubCon.setText(facility.subcontractor);
        textFacSpot.setText(facility.building + " " + facility.floor + " " + facility.spot);

        if(facility.super_manager.isEmpty()) {
            textSuperManager.setTextColor(Color.BLUE);
            textSuperManager.setText("등록하기");
        } else {
            textSuperManager.setTextColor(Color.BLACK);
            textSuperManager.setText(facility.super_manager);
        }

        if(facility.purpose.isEmpty()) {
            layoutPurpose.setVisibility(View.GONE);
        } else {
            layoutPurpose.setVisibility(View.VISIBLE);
            textPurpose.setText(facility.purpose);
        }

        //만료일 체크
        if(facility.finished_at != null){
            if(facility.expired_at != null){
                SimpleDateFormat expiredDateFormat = new SimpleDateFormat("~ yyyy. MM. dd");
                textExpiredDate.setText(expiredDateFormat.format(facility.expired_at));
                textExpiredDate.setTextColor(Color.BLACK);
            }
            else{
                textExpiredDate.setText("만료일등록");
                textExpiredDate.setTextColor(Color.BLUE);
            }
        } else
            textExpiredDate.setText("");

        if(is_manager_button){
            if (textTaskState.getText() == "설치전" || textTaskState.getText() == "승인완료" || textTaskState.getText() == "수정완료") {
                buttonTaskPlan.setVisibility(View.VISIBLE);
            } else {
                buttonTaskPlan.setVisibility(View.GONE);
            }
        }
    }


    //SeekBar와 상태글자
    private void State(String state) {

        switch(state) {
            case "설치전":
                seekBarTask.setProgress(0);
                break;
            case "설치중":
                seekBarTask.setProgress(1);
                break;
            case "승인완료":
            case "수정중":
            case "수정완료":
                seekBarTask.setProgress(2);
                break;
            case "해체중":
                seekBarTask.setProgress(3);
                break;
            case "해체완료":
                seekBarTask.setProgress(4);
                break;
            default:
                break;
        }
        textTaskState.setText(state);

    }

    /*
    //만료일 확인
    private void CheckExpiredDate() {
        if(facility.finished_at != null){
            if(facility.expired_at != null){
                SimpleDateFormat expiredDateFormat = new SimpleDateFormat("~ yyyy. MM. dd");
                textExpiredDate.setText(expiredDateFormat.format(facility.expired_at));
                textExpiredDate.setTextColor(Color.BLACK);
            }
            else{
                textExpiredDate.setText("만료일등록");
                textExpiredDate.setTextColor(Color.BLUE);
            }
        } else
            textExpiredDate.setText("");
    }
    */

    //만료일등록의 Picker처리
    private void PickerExpiredDate(DatePicker pickerExpiredDate, int monthAfter) {

        SimpleDateFormat formatYear =  new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd");

        Calendar calExpiredDate = Calendar.getInstance();

        if(monthAfter == 3 || monthAfter == 6) {
            calExpiredDate.setTime(facility.finished_at);
            if(monthAfter == 6) {
                calExpiredDate.add(Calendar.MONTH, 6);
            } else {
                calExpiredDate.add(Calendar.MONTH, 3);
            }
            calExpiredDate.add(Calendar.DATE, -1);
        } else {
            calExpiredDate.setTime(facility.expired_at);
        }
        int year = Integer.parseInt(formatYear.format(calExpiredDate.getTime()));
        int month = Integer.parseInt(formatMonth.format(calExpiredDate.getTime())) - 1;
        int date = Integer.parseInt(formatDate.format(calExpiredDate.getTime()));
        pickerExpiredDate.updateDate(year, month, date);
    }

}