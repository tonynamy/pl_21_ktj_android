package com.poogosoft.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Facility;
import com.poogosoft.facmanager.models.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FacilityActivity extends AppCompatActivity {

    int level;
    String place_id;
    String team_id;
    String facility_id;
    String super_manager_name;
    int button_right;

    Facility facility = new Facility();
    ArrayList<Team> teams;
    int manday = 1;

    TextView textFacilityResult;
    ConstraintLayout layoutFacility;
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
    ImageView imageUp;
    TextView textManday;
    ImageView imageDown;
    Button buttonCreate;
    Button buttonEdit;
    Button buttonEdit2;
    Button buttonDis;
    Button buttonTaskPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        //Intent에서 가져오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        if(team_id == null) { team_id = ""; }
        facility_id = getIntent().getStringExtra("facility_id");
        super_manager_name = getIntent().getStringExtra("super_manager_name");
        button_right = getIntent().getIntExtra("button_right", 0);

        //뷰에서 가져오기
        textFacilityResult = findViewById(R.id.textFacilityResult);
        layoutFacility = findViewById(R.id.layoutFacility);
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
        imageUp = findViewById(R.id.imageUp);
        textManday = findViewById(R.id.textManday);
        imageDown = findViewById(R.id.imageDown);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit2 = findViewById(R.id.buttonEdit2);
        buttonDis = findViewById(R.id.buttonDis);
        buttonTaskPlan = findViewById(R.id.buttonTaskPlan);

        //SeekBar에 터치가 안되게
        seekBarTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //권한이 필요한 모든 뷰 비활성화
        layoutFacility.setVisibility(View.GONE);
        buttonFacManger.setVisibility(View.GONE);
        layoutTeamLeader.setVisibility(View.GONE);
        buttonTaskPlan.setVisibility(View.GONE);

        //관리자 버튼으로 왔을시
        if(button_right == ButtonRight.MANAGER) {
            buttonFacManger.setVisibility(View.VISIBLE);
            buttonTaskPlan.setVisibility(View.VISIBLE);
        } else if(button_right == ButtonRight.TEAM_LEADER) {
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
                    //팀장(1)레벨은 만료일이 공백일때만 등록할수있다
                    if(level != 1 || facility.expired_at == null) {
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
                                pickerExpiredDate(pickerExpiredDate,3);
                                pickerExpiredDate.setVisibility(View.VISIBLE);
                            }
                        });

                        textAfter6month.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pickerExpiredDate(pickerExpiredDate,6);
                                pickerExpiredDate.setVisibility(View.VISIBLE);
                            }
                        });

                        textDirectInput.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(facility.expired_at != null)
                                    pickerExpiredDate(pickerExpiredDate,0);
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
            }
        });

        //투입인원 화살표
        ArrayList<ImageView> arrow_list = new ArrayList<>();
        arrow_list.add(imageUp);
        arrow_list.add(imageDown);
        for(int i = 0; i < arrow_list.size(); i++) {
            ImageView arrow_image = arrow_list.get(i);
            int num = i;
            arrow_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(num == 0 && manday < 100) {
                        manday++;
                    } else if(num == 1 && manday > 1) {
                        manday--;
                    }
                    textManday.setText(String.valueOf(manday));
                }
            });
        }

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
                AlertDialog dialog = builder.create();

                //기본세팅
                if(textTaskState.getText() == "설치전") {
                    radioStart.toggle();
                    radioEdit.setVisibility(View.GONE);
                    radioDis.setVisibility(View.GONE);
                } else {
                    radioStart.setVisibility(View.GONE);
                }
                textDialogDelPlan.setVisibility(View.GONE);

                //수정상황
                if(facility.taskplan_type != null) {

                    if(facility.taskplan_type.equals("1")) {
                        radioStart.toggle();
                    } else if(facility.taskplan_type.equals("2")) {
                        radioEdit.toggle();
                    } else if(facility.taskplan_type.equals("3")) {
                        radioDis.toggle();
                    }
                    textDialogDelPlan.setVisibility(View.VISIBLE);
                }

                //팀목록API
                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {

                        teams = (ArrayList<Team>) data;

                        HintSpinnerAdapter teamAdapter = new HintSpinnerAdapter(v.getContext(), R.layout.spinner_item, new ArrayList<>());
                        teamAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
                        teamAdapter.isBlack = true;
                        spinPlanTeam.setAdapter(teamAdapter);

                        ArrayList<String> team_name = new ArrayList<>();

                        for(int i = 0; i < teams.size(); i++) {
                            Team team = teams.get(i);
                            team_name.add(team.name);

                            if(facility.taskplan_type != null && team.id.equals(facility.taskplan_team_id)) {
                                spinPlanTeam.setSelection(i);
                            }
                        }

                        teamAdapter.addAll(team_name);
                        teamAdapter.notifyDataSetChanged();

                        dialog.show();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(v.getContext(), "팀목록을 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };
                API api = new API.Builder(apiCallback).build();
                api.getTeams(place_id);


                //작업계획 삭제
                textDialogDelPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteTaskPlan(dialog);

                    }
                });

                //취소
                textDialogCancel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //확인
                textDialogSubmit4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int type;
                        if(radioStart.isChecked()){
                            type = 1;
                        } else if(radioEdit.isChecked()) {
                            type = 2;
                        } else if(radioDis.isChecked()) {
                            type = 3;
                        } else {
                            Toast.makeText(v.getContext(), "작업내용이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        API.APICallback apiCallback = new API.APICallback() {
                            @Override
                            public void onSuccess(Object data) {
                                String doneMsg = (facility.taskplan_type != null) ? "작업계획 수정에 성공했습니다" : "작업계획 등록에 성공했습니다";
                                Toast.makeText(v.getContext(), doneMsg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                getFacility();
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                Toast.makeText(v.getContext(), "작업계획 등록에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        };
                        API api = new API.Builder(apiCallback).build();
                        api.editTaskPlan(facility.id, teams.get(spinPlanTeam.getSelectedItemPosition()).id, type);
                    }
                });
            }
        });

        //Facility 정보 가져오기
        getFacility();
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
                textFacilityResult.setGravity(Gravity.NO_GRAVITY);
                textFacilityResult.setText("설치계획정보를 불러오는데 실패했습니다.\n사유: " + errorMsg);
                textFacilityResult.setVisibility(View.VISIBLE);
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getFacility(facility_id, team_id);
    }

    private void setFacility(Facility facility) {
        this.facility = facility;

        onFacilityChanged();
    }

    private void onFacilityChanged() {

        //진행상황
        if(facility.dis_finished_at != null) {
            state("해체완료");
        } else if(facility.dis_started_at != null) {
            state("해체중");
        } else if(facility.edit_finished_at != null) {
            state("수정완료");
        } else if(facility.edit_started_at != null) {
            state("수정중");
        } else if(facility.finished_at != null) {
            state("승인완료");
        } else  if(facility.started_at != null) {
            state("설치중");
        } else {
            state("설치전");
        }

        //공종
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

        //담당자
        if(facility.super_manager.isEmpty()) {
            textSuperManager.setTextColor(Color.BLUE);
            textSuperManager.setText("등록하기");
        } else {
            textSuperManager.setTextColor(Color.BLACK);
            textSuperManager.setText(facility.super_manager);
        }

        //설치목적
        if(facility.purpose.isEmpty()) {
            layoutPurpose.setVisibility(View.GONE);
        } else {
            layoutPurpose.setVisibility(View.VISIBLE);
            textPurpose.setText(facility.purpose);
        }

        //만료일
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

        //작업계획
        if(button_right == ButtonRight.MANAGER){
            if(textTaskState.getText() == "설치전" || textTaskState.getText() == "승인완료" || textTaskState.getText() == "수정완료") {
                if(facility.taskplan_type != null) {
                    buttonTaskPlan.setText("작업계획 수정");
                } else {
                    buttonTaskPlan.setText("작업계획");
                }
                buttonTaskPlan.setVisibility(View.VISIBLE);
            } else {
                if(facility.taskplan_type != null) {
                    deleteTaskPlan();
                }
                buttonTaskPlan.setVisibility(View.GONE);
            }
        //맨데이 투입
        } else if(button_right == ButtonRight.TEAM_LEADER) {

            buttonCreate.setVisibility(View.GONE);
            buttonEdit.setVisibility(View.GONE);
            buttonEdit2.setVisibility(View.INVISIBLE);
            buttonDis.setVisibility(View.GONE);

            if(Integer.parseInt(facility.attendance) > 0 && manday == 1){
                manday = Integer.parseInt(facility.attendance);
                textManday.setText(String.valueOf(manday));
            }
            if(textTaskState.getText() == "설치전" || textTaskState.getText() == "설치중") {
                buttonCreate.setVisibility(View.VISIBLE);
            } else if(textTaskState.getText() == "승인완료" || textTaskState.getText() == "수정완료") {
                buttonEdit2.setVisibility(View.VISIBLE);
                buttonDis.setVisibility(View.VISIBLE);
            } else if(textTaskState.getText() == "수정중") {
                buttonEdit.setVisibility(View.VISIBLE);
            } else if(textTaskState.getText() == "해체중") {
                buttonDis.setVisibility(View.VISIBLE);
            }
        }
        textFacilityResult.setVisibility(View.GONE);
        layoutFacility.setVisibility(View.VISIBLE);
    }

    //SeekBar와 상태글자
    private void state(String state) {

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

    //만료일등록의 Picker처리
    private void pickerExpiredDate(DatePicker pickerExpiredDate, int monthAfter) {

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

    //작업계획 삭제
    private void deleteTaskPlan() {
        deleteTaskPlan( null);
    }
    private void deleteTaskPlan(AlertDialog dialog) {

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                if(dialog != null) { dialog.dismiss(); }
                getFacility();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(FacilityActivity.this, "작업계획을 삭제하는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.deleteTaskplan(facility.id);
    }

}