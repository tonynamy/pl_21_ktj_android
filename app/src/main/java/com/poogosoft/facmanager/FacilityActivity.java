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
import android.widget.CompoundButton;
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
    //String super_manager_name;
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
    TextView textFacSubCon;
    TextView textFacSpot;
    LinearLayout layoutPurpose;
    TextView textPurpose;
    TextView textFacSizeName;
    TextView textFacSize;
    LinearLayout layoutExpiredDate;
    TextView textExpiredDate;

    LinearLayout layoutTeamLeader;
    ImageView imageUp;
    TextView textManday;
    ImageView imageDown;
    Button buttonTask1;
    Button buttonTask2;
    Button buttonTaskPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        //Intent?????? ????????????
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        team_id = getIntent().getStringExtra("team_id");
        if(team_id == null) { team_id = ""; }
        facility_id = getIntent().getStringExtra("facility_id");
        //super_manager_name = getIntent().getStringExtra("super_manager_name");
        button_right = getIntent().getIntExtra("button_right", 0);

        //????????? ????????????
        textFacilityResult = findViewById(R.id.textFacilityResult);
        layoutFacility = findViewById(R.id.layoutFacility);
        textFacSerial = findViewById(R.id.textFacSerial);
        seekBarTask = findViewById(R.id.seekBarTask);
        textTaskState = findViewById(R.id.textTaskState);
        buttonFacManger = findViewById(R.id.buttonFacManger);

        textFacType = findViewById(R.id.textFacType);
        textSuperManager = findViewById(R.id.textSuperManager);
        textFacSubCon = findViewById(R.id.textFacSubCon);
        textFacSpot = findViewById(R.id.textFacSpot);
        layoutPurpose = findViewById(R.id.layoutPurpose);
        textPurpose = findViewById(R.id.textPurpose);
        textFacSizeName = findViewById(R.id.textFacSizeName);
        textFacSize = findViewById(R.id.textFacSize);
        layoutExpiredDate = findViewById(R.id.layoutExpiredDate);
        textExpiredDate = findViewById(R.id.textExpiredDate);

        layoutTeamLeader = findViewById(R.id.layoutTeamLeader);
        imageUp = findViewById(R.id.imageUp);
        textManday = findViewById(R.id.textManday);
        imageDown = findViewById(R.id.imageDown);
        buttonTask1 = findViewById(R.id.buttonTask1);
        buttonTask2 = findViewById(R.id.buttonTask2);
        buttonTaskPlan = findViewById(R.id.buttonTaskPlan);

        //SeekBar??? ????????? ?????????
        seekBarTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //????????? ????????? ?????? ??? ????????????
        layoutFacility.setVisibility(View.GONE);
        buttonFacManger.setVisibility(View.GONE);
        layoutTeamLeader.setVisibility(View.GONE);
        buttonTaskPlan.setVisibility(View.GONE);
        textExpiredDate.setText("");

        //????????? ???????????? ?????????
        if(button_right == ButtonRight.MANAGER) {
            buttonFacManger.setVisibility(View.VISIBLE);
            //buttonTaskPlan.setVisibility(View.VISIBLE);
        } else if(button_right == ButtonRight.TEAM_LEADER && !team_id.isEmpty()) {
            layoutTeamLeader.setVisibility(View.VISIBLE);
        } else if(level == 2 || level == 3 || level == 4) {
            buttonFacManger.setVisibility(View.VISIBLE);
            //buttonTaskPlan.setVisibility(View.VISIBLE);
        } else if(level == 1 && !team_id.isEmpty()) {
            layoutTeamLeader.setVisibility(View.VISIBLE);
        }

        //???????????? ?????? ????????????
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
                                    Toast.makeText(v.getContext(), "???????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "???????????? ????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
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

        //????????? ????????????
        textSuperManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????????????? ?????????(2), ???????????????(3), ?????????(4)??????
                if(level == 2 || level == 3 || level == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_text_input, null);
                    builder.setView(view);

                    EditText eTextDialogInput = view.findViewById(R.id.eTextDialogInput);
                    TextView textDialogCancel5 = view.findViewById(R.id.textDialogCancel5);
                    TextView textDialogSubmit5 = view.findViewById(R.id.textDialogSubmit5);
                    eTextDialogInput.setHint("?????????");

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
                                    Toast.makeText(v.getContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "????????? ????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            };

                            API api = new API.Builder(apiCallback).build();

                            api.editFacilitySuperManager(facility_id, eTextDialogInput.getText().toString());
                        }
                    });
                }
            }
        });

        //???????????? ????????????
        textPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????????????? ?????????(2), ???????????????(3), ?????????(4)??????
                if(level == 2 || level == 3 || level == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_text_input, null);
                    builder.setView(view);

                    EditText eTextDialogInput = view.findViewById(R.id.eTextDialogInput);
                    TextView textDialogCancel5 = view.findViewById(R.id.textDialogCancel5);
                    TextView textDialogSubmit5 = view.findViewById(R.id.textDialogSubmit5);
                    eTextDialogInput.setHint("????????????");

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
                                    Toast.makeText(v.getContext(), "???????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(v.getContext(), "???????????? ????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            };
                            API api = new API.Builder(apiCallback).build();
                            api.editFacilityPurpose(facility_id, eTextDialogInput.getText().toString());
                        }
                    });
                }
            }
        });

        //??????????????? ????????????
        textFacSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facility.cube_result.isEmpty() && facility.danger_result.isEmpty() && button_right==ButtonRight.MANAGER) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_fac_size_input, null);
                    builder.setView(view);

                    RadioButton radioRube = view.findViewById(R.id.radioRube);
                    RadioButton radioHebe = view.findViewById(R.id.radioHebe);
                    EditText eTextFacSizeInput = view.findViewById(R.id.eTextFacSizeInput);
                    TextView textDialogUnit = view.findViewById(R.id.textDialogUnit);
                    TextView textDialogCancel11 = view.findViewById(R.id.textDialogCancel11);
                    TextView textDialogSubmit11 = view.findViewById(R.id.textDialogSubmit11);
                    AlertDialog dialog = builder.create();

                    radioRube.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) { textDialogUnit.setText("m3"); }
                        }
                    });
                    radioHebe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) { textDialogUnit.setText("m2"); }
                        }
                    });

                    textDialogCancel11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    //????????????
                    textDialogSubmit11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String size_string = String.valueOf(eTextFacSizeInput.getText());
                            try {
                                Double size = Double.parseDouble(size_string);
                                Boolean is_danger = radioHebe.isChecked() ? true : false;

                                if(size > 0) {
                                    API.APICallback apiCallback = new API.APICallback() {
                                        @Override
                                        public void onSuccess(Object data) {
                                            getFacility();
                                            dialog.dismiss();
                                            Toast.makeText(v.getContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailed(String errorMsg) {
                                            dialog.dismiss();
                                            Toast.makeText(v.getContext(), "?????? ????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    API api = new API.Builder(apiCallback).build();
                                    api.editFacilitySize(facility_id, String.format("%.1f", size), is_danger);

                                } else {
                                    Toast.makeText(v.getContext(), "0?????? ??? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(v.getContext(), "????????? ?????? ????????????.", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    dialog.show();
                }
            }
        });

        //?????????????????? ????????????
        textExpiredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(facility.finished_at != null && (!team_id.isEmpty() || level == 2 || level == 3 || level == 4)) {
                    //??????(1)????????? ???????????? ??????????????? ??????????????????
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
                                            Toast.makeText(v.getContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailed(String errorMsg) {
                                            dialog.dismiss();
                                            Toast.makeText(v.getContext(), "????????? ????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    API api = new API.Builder(apiCallback).build();
                                    api.editFacilityExpiredAt(facility_id, expired_at);
                                }
                            }
                        });

                        dialog.show();
                    }
                }
            }
        });

        //???????????? ???????????????
        textManday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manday = Integer.parseInt(facility.attendance) > 1 ? Integer.parseInt(facility.attendance) : 1;
                textManday.setText(String.valueOf(manday));
            }
        });

        //???????????? ?????????
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

        //????????????
        ArrayList<Button> taskButtons = new ArrayList<>();
        taskButtons.add(buttonTask1);
        taskButtons.add(buttonTask2);

        for(Button taskButton : taskButtons) {

            taskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    API.APICallback apiCallback = new API.APICallback() {
                        @Override
                        public void onSuccess(Object data) {
                            Toast.makeText(v.getContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            getFacility();
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(v.getContext(), "??????????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };
                    API api = new API.Builder(apiCallback).build();
                    if(taskButton.getText() == "??????") {
                        api.addTask(team_id, facility.o_serial, manday, 1);
                    } else if(taskButton.getText() == "??????") {
                        api.addTask(team_id, facility.o_serial, manday, 2);
                    } else if(taskButton.getText() == "??????") {
                        api.addTask(team_id, facility.o_serial, manday, 3);
                    }

                    //Facility ?????? ????????????
                    //getFacility();
                }
            });
        }

        //?????????????????? ????????? ???
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

                //????????????
                if(textTaskState.getText() == "?????????") {
                    radioStart.toggle();
                    radioEdit.setVisibility(View.GONE);
                    radioDis.setVisibility(View.GONE);
                } else {
                    radioStart.setVisibility(View.GONE);
                }
                textDialogDelPlan.setVisibility(View.GONE);

                //????????????
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

                //?????????API
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
                        Toast.makeText(v.getContext(), "???????????? ??????????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };
                API api = new API.Builder(apiCallback).build();
                api.getTeams(place_id);


                //???????????? ??????
                textDialogDelPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteTaskPlan(dialog);

                    }
                });

                //??????
                textDialogCancel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //??????
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
                            Toast.makeText(v.getContext(), "??????????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        API.APICallback apiCallback = new API.APICallback() {
                            @Override
                            public void onSuccess(Object data) {
                                String doneMsg = (facility.taskplan_type != null) ? "???????????? ????????? ??????????????????" : "???????????? ????????? ??????????????????";
                                Toast.makeText(v.getContext(), doneMsg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                getFacility();
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                Toast.makeText(v.getContext(), "???????????? ????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        };
                        API api = new API.Builder(apiCallback).build();
                        api.editTaskPlan(place_id, facility.o_serial, teams.get(spinPlanTeam.getSelectedItemPosition()).id, type);
                    }
                });
            }
        });

        //Facility ?????? ????????????
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
                textFacilityResult.setText("????????????????????? ??????????????? ??????????????????.\n??????: " + errorMsg);
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

        //????????????
        if(facility.dis_finished_at != null) {
            state("????????????");
        } else if(facility.dis_started_at != null) {
            state("?????????");
        } else if(facility.edit_finished_at != null) {
            state("????????????");
        } else if(facility.edit_started_at != null) {
            state("?????????");
        } else if(facility.finished_at != null) {
            state("????????????");
        } else  if(facility.started_at != null) {
            state("?????????");
        } else {
            state("?????????");
        }

        //??????
        textFacSerial.setText(facility.serial);
        String stringFacType = "";
        switch (facility.type) {
            case 1:
                stringFacType = "??????";
                break;
            case 2:
                stringFacType = "??????";
                break;
            case 3:
                stringFacType = "??????";
                break;
            case 4:
                stringFacType = "??????";
                break;
            default:
                break;
        }
        textFacType.setText(stringFacType);

        //?????????
        if(facility.super_manager.isEmpty()) {
            textSuperManager.setTextColor(Color.BLUE);
            textSuperManager.setText("????????????");
        } else {
            textSuperManager.setTextColor(Color.BLACK);
            textSuperManager.setText(facility.super_manager);
        }

        //????????????
        textFacSubCon.setText(facility.subcontractor);

        //????????????
        textFacSpot.setText(facility.building + " " + facility.floor + " " + facility.spot);

        //????????????
        if(facility.purpose.isEmpty()) {
            layoutPurpose.setVisibility(View.GONE);
        } else {
            layoutPurpose.setVisibility(View.VISIBLE);
            textPurpose.setText(facility.purpose);
        }

        //??????
        textFacSize.setTextColor(Color.BLACK);
        if(!facility.cube_result.isEmpty()) {
            textFacSizeName.setText("??????(??????)");
            textFacSize.setText(facility.cube_result + "???");
        } else if(!facility.danger_result.isEmpty()) {
            textFacSizeName.setText("??????(??????)");
            textFacSize.setText(facility.danger_result + "???");
        } else {
            textFacSizeName.setText("??????");
            if(button_right == ButtonRight.MANAGER) {
                textFacSize.setText("????????????");
                textFacSize.setTextColor(Color.BLUE);
            } else{
                textFacSize.setText("");
            }
        }

        //????????????
        if(facility.finished_at != null) {
            layoutExpiredDate.setVisibility(View.VISIBLE);
            if(facility.expired_at != null) {
                Date now = Calendar.getInstance().getTime();
                if(now.after(facility.expired_at)) {
                    textExpiredDate.setTextColor(Color.RED);
                } else {
                    textExpiredDate.setTextColor(Color.BLACK);
                }
                SimpleDateFormat expiredDateFormat = new SimpleDateFormat("~ yyyy. MM. dd");
                textExpiredDate.setText(expiredDateFormat.format(facility.expired_at));
            }
            else if(!team_id.isEmpty() || level == 2 || level == 3 || level == 4) {
                textExpiredDate.setText("???????????????");
                textExpiredDate.setTextColor(Color.BLUE);
            }
        } else {
            layoutExpiredDate.setVisibility(View.GONE);
        }

        //????????? ?????? ????????? ??????
        if(!team_id.isEmpty()) {

            buttonTask1.setVisibility(View.INVISIBLE);
            buttonTask2.setVisibility(View.INVISIBLE);
            manday = Integer.parseInt(facility.attendance) > 1 ? Integer.parseInt(facility.attendance) : 1;
            textManday.setText(String.valueOf(manday));

            //??????????????? ?????? ?????? ????????? ?????? ??????????????? ?????????
            if(facility.taskplan_type != null && facility.taskplan_team_id.equals(team_id)) {

                if(facility.taskplan_type.equals("1")) {
                    buttonTask2.setText("??????");
                    buttonTask2.setVisibility(View.VISIBLE);
                } else if(facility.taskplan_type.equals("2")) {
                    buttonTask2.setText("??????");
                    buttonTask2.setVisibility(View.VISIBLE);
                } else if(facility.taskplan_type.equals("3")) {
                    buttonTask2.setText("??????");
                    buttonTask2.setVisibility(View.VISIBLE);
                }

            } else {
                if(textTaskState.getText() == "?????????" || textTaskState.getText() == "?????????") {
                    buttonTask2.setText("??????");
                    buttonTask2.setVisibility(View.VISIBLE);
                } else if(textTaskState.getText() == "????????????" || textTaskState.getText() == "????????????" || textTaskState.getText() == "?????????") {
                    buttonTask1.setText("??????");
                    buttonTask1.setVisibility(View.VISIBLE);
                    buttonTask2.setText("??????");
                    buttonTask2.setVisibility(View.VISIBLE);
                } else if(textTaskState.getText() == "?????????") {
                    buttonTask2.setText("??????");
                    buttonTask2.setVisibility(View.VISIBLE);
                }
            }
        }

        //???????????? ?????? ????????? ??????
        if(level == 2 || level == 3 || level == 4) {
            if(button_right != ButtonRight.TEAM_LEADER) {
                if(textTaskState.getText() == "?????????" || textTaskState.getText() == "????????????" || textTaskState.getText() == "????????????") {
                    if(facility.taskplan_type != null) {
                        buttonTaskPlan.setText("???????????? ??????");
                    } else {
                        buttonTaskPlan.setText("????????????");
                    }
                    buttonTaskPlan.setVisibility(View.VISIBLE);
                } else {
                    buttonTaskPlan.setVisibility(View.GONE);
                }
            }
        }

        //??????????????? ????????? ????????? ????????? ???????????? ????????????
        textFacilityResult.setVisibility(View.GONE);
        layoutFacility.setVisibility(View.VISIBLE);
    }

    //SeekBar??? ????????????
    private void state(String state) {

        switch(state) {
            case "?????????":
                seekBarTask.setProgress(0);
                break;
            case "?????????":
                seekBarTask.setProgress(1);
                break;
            case "????????????":
            case "?????????":
            case "????????????":
                seekBarTask.setProgress(2);
                break;
            case "?????????":
                seekBarTask.setProgress(3);
                break;
            case "????????????":
                seekBarTask.setProgress(4);
                break;
            default:
                break;
        }
        textTaskState.setText(state);

    }

    //?????????????????? Picker??????
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

    //???????????? ??????
    private void deleteTaskPlan() {
        deleteTaskPlan(null);
    }
    private void deleteTaskPlan(AlertDialog dialog) {

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                Toast.makeText(FacilityActivity.this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                if(dialog != null) { dialog.dismiss(); }
                getFacility();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(FacilityActivity.this, "??????????????? ??????????????? ??????????????????. ??????: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.deleteTaskplan(place_id, facility.o_serial);
    }

}