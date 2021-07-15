package com.example.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.facmanager.models.Facility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FacilityActivity extends AppCompatActivity {

    Facility facility = new Facility();

    TextView textFacSerial;
    SeekBar seekBarTask;
    TextView textTaskState;
    Button buttonFacManger;

    TextView textFacType;
    TextView textFacSubCon;
    TextView textFacSpot;
    TextView textExpiredDate;

    LinearLayout layoutTeamLeader;
    Button buttonCreate;
    Button buttonEdit;
    Button buttonDisassem;
    LinearLayout layoutManager;
    Button buttonPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        //예시데이터
        facility.serial = "FM-설비-FAB-002";
        facility.type = 1;
        facility.subcontractor = "우현,세일,두원";
        facility.building = "FAB";
        facility.floor = "5F";
        facility.spot = "B-C/13-14열";
        facility.created_at = new Date(System.currentTimeMillis());
        //예시데이터


        //뷰 불러오기
        textFacSerial = findViewById(R.id.textFacSerial);
        seekBarTask = findViewById(R.id.seekBarTask);
        textTaskState = findViewById(R.id.textTaskState);
        buttonFacManger = findViewById(R.id.buttonFacManger);

        textFacType = findViewById(R.id.textFacType);
        textFacSubCon = findViewById(R.id.textFacSubCon);
        textFacSpot = findViewById(R.id.textFacSpot);
        textExpiredDate = findViewById(R.id.textExpiredDate);

        layoutTeamLeader = findViewById(R.id.layoutTeamLeader);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDisassem = findViewById(R.id.buttonDisassem);
        layoutManager = findViewById(R.id.layoutManager);
        buttonPlan = findViewById(R.id.buttonPlan);


        //SeekBar에 터치가 안되게
        seekBarTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


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

        CheckExpiredDate();


        //유저레벨에 따라 뷰가 다르게 보임
        //level 2이상일때
        buttonFacManger.setVisibility(View.VISIBLE);
        layoutTeamLeader.setVisibility(View.GONE);
        layoutManager.setVisibility(View.VISIBLE);
        //else
        //buttonFacManger.setVisibility(View.GONE);
        //layoutTeamLeader.setVisibility(View.VISIBLE);
        //layoutManager.setVisibility(View.GONE);


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

                //현재상태와 같은 것을 눌렀을때는 날짜가 기록되지 않는다
                //예) 수정중 상태에서 수정중을 눌렀을때 수정시작일은 기록되지 않는다
                textFacReady.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //설치시작일started_at ~ 해체완료일dis_finish_at까지 모든 날짜 삭제
                        facility.started_at = null;
                        facility.finished_at = null;
                        facility.edit_started_at = null;
                        facility.edit_finished_at = null;
                        facility.dis_started_at = null;
                        facility.dis_finished_at = null;
                        facility.expired_at = null;
                        State("설치전");
                        CheckLog();
                        dialog.dismiss();
                    }
                });

                textFacCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //현장승인일finihshed_at ~ 해체완료일dis_finish_at까지 모든 날짜 삭제
                        //설치시작일started_at에 버튼을 누른 날짜 입력, 현재상태와 같으면 입력 안됨
                        if(textTaskState.getText() != "설치중")
                            facility.started_at = new Date(System.currentTimeMillis());
                        facility.finished_at = null;
                        facility.edit_started_at = null;
                        facility.edit_finished_at = null;
                        facility.dis_started_at = null;
                        facility.dis_finished_at = null;
                        facility.expired_at = null;
                        State("설치중");
                        CheckLog();
                        dialog.dismiss();
                    }
                });

                textFacFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //수정시작일edit_started_at ~ 해체완료일dis_finish_at까지 모든 날짜 삭제
                        //현장승인일finished_at에 버튼을 누른 날짜 입력, 현재상태와 같으면 입력 안됨
                        if(textTaskState.getText() != "승인완료")
                            facility.finished_at = new Date(System.currentTimeMillis());
                        facility.edit_started_at = null;
                        facility.edit_finished_at = null;
                        facility.dis_started_at = null;
                        facility.dis_finished_at = null;
                        State("승인완료");
                        dialog.dismiss();
                        CheckLog();
                    }
                });

                textFacEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //수정완료일edit_finished_at ~ 해체완료일dis_finish_at까지 모든 날짜 삭제
                        //수정시작일edit_started_at에 버튼을 누른 날짜 입력, 현재상태와 같으면 입력 안됨
                        if(textTaskState.getText() != "수정중")
                            facility.edit_started_at = new Date(System.currentTimeMillis());
                        facility.edit_finished_at = null;
                        facility.dis_started_at = null;
                        facility.dis_finished_at = null;
                        State("수정중");
                        CheckLog();
                        dialog.dismiss();
                    }
                });

                textFacEditFin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //해체시작일dis_started_at ~ 해체완료일dis_finish_at까지 모든 날짜 삭제
                        //수정완료일edit_finished_at에 버튼을 누른 날짜 입력, 현재상태와 같으면 입력 안됨
                        if(textTaskState.getText() != "수정완료")
                            facility.edit_finished_at = new Date(System.currentTimeMillis());
                        facility.dis_started_at = null;
                        facility.dis_finished_at = null;
                        State("수정완료");
                        CheckLog();
                        dialog.dismiss();
                    }
                });

                textFacDisassem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //해체완료일dis_finish_at날짜 삭제
                        //해체시작일dis_started_at에 버튼을 누른 날짜 입력, 현재상태와 같으면 입력 안됨
                        if(textTaskState.getText() != "해체중")
                            facility.dis_started_at = new Date(System.currentTimeMillis());
                        facility.dis_finished_at = null;
                        State("해체중");
                        CheckLog();
                        dialog.dismiss();
                    }
                });

                textFacDisFin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //해체완료일dis_finish_at에 버튼을 누른 날짜 입력, 현재상태와 같으면 입력 안됨
                        if(textTaskState.getText() != "해체완료")
                            facility.dis_finished_at = new Date(System.currentTimeMillis());
                        State("해체완료");
                        CheckLog();
                        dialog.dismiss();
                    }
                });

                textFacDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        //만료일등록 글자를 눌렀을때
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
                                facility.expired_at = calExpiredDate.getTime();
                                CheckExpiredDate();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
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
        CheckExpiredDate();
    }


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


    //디버그 로그
    private void CheckLog() {
        String log_created_at = "NULL";
        String log_started_at = "NULL";
        String log_finished_at = "NULL";
        String log_edit_started_at = "NULL";
        String log_edit_finished_at = "NULL";
        String log_dis_started_at = "NULL";
        String log_dis_finished_at = "NULL";
        String log_expired_at = "NULL";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd");
        if(facility.created_at != null)
            log_created_at = simpleDateFormat.format(facility.created_at);
        if(facility.started_at != null)
            log_started_at = simpleDateFormat.format(facility.started_at);
        if(facility.finished_at != null)
            log_finished_at = simpleDateFormat.format(facility.finished_at);
        if(facility.edit_started_at != null)
            log_edit_started_at = simpleDateFormat.format(facility.edit_started_at);
        if(facility.edit_finished_at != null)
            log_edit_finished_at = simpleDateFormat.format(facility.edit_finished_at);
        if(facility.dis_started_at != null)
            log_dis_started_at = simpleDateFormat.format(facility.dis_started_at);
        if(facility.dis_finished_at != null)
            log_dis_finished_at = simpleDateFormat.format(facility.dis_finished_at);
        if(facility.expired_at != null)
            log_expired_at = simpleDateFormat.format(facility.expired_at);

        Log.d("코딩하자", "created_at: " + log_created_at
                 + ",     started_at: " + log_started_at
                 + ",     finished_at: " + log_finished_at
                 + ",     edit_started_at: " + log_edit_started_at
                 + ",     edit_finished_at: " + log_edit_finished_at
                 + ",     dis_started_at: " + log_dis_started_at
                 + ",     dis_finished_at: " + log_dis_finished_at
                 + ",     expired_at: " + log_expired_at);
    }

}