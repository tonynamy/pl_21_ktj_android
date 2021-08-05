package com.example.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MenuActivity extends AppCompatActivity {

    int level;
    String place_id;
    String place_name;
    String user_name;
    String team_id;

    enum ButtonRight {
        team_leader(1), manager(2);
        int value;
        ButtonRight(int value){ this.value = value; }
    }

    TextView txtInfo;
    Button btnAttendMenu;
    Button btnTeamLeaderMenu;
    Button btnManagerMenu;
    Button btnSuperManager;
    Button btnAdminMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Intent에서 가져오기
        level = getIntent().getIntExtra("level", -2);
        place_id = getIntent().getStringExtra("place_id");
        place_name = getIntent().getStringExtra("place_name");
        user_name = getIntent().getStringExtra("user_name");
        team_id = getIntent().getStringExtra("team_id");

        //뷰에서 가져오기
        txtInfo = findViewById(R.id.txtInfo);
        btnAttendMenu = findViewById(R.id.btnAttendMenu);
        btnTeamLeaderMenu = findViewById(R.id.btnTeamLeaderMenu);
        btnManagerMenu = findViewById(R.id.btnManagerMenu);
        btnSuperManager = findViewById(R.id.btnSuperManager);
        btnAdminMenu = findViewById(R.id.btnAdminMenu);

        //권한없는 메뉴는 모두 안보이게 하기
        btnAttendMenu.setVisibility(View.GONE);
        btnTeamLeaderMenu.setVisibility(View.GONE);
        btnManagerMenu.setVisibility(View.GONE);
        btnSuperManager.setVisibility(View.GONE);
        btnAdminMenu.setVisibility(View.GONE);

        //팀장(1), 관리자(2), 최고관리자(3), 어드민(4) 일때 출퇴근관리 조회
        if (level == 1 || level == 2 || level == 3 || level == 4) {
            btnAttendMenu.setVisibility(View.VISIBLE);
            //내 팀이 있을때 팀장님메뉴 조회
            if(!team_id.isEmpty()){ btnTeamLeaderMenu.setVisibility(View.VISIBLE); }
        }
        //관리자(2), 최고관리자(3), 어드민(4) 일때 관리자메뉴 조회
        if (level == 2 || level == 3 || level == 4) {
            btnManagerMenu.setVisibility(View.VISIBLE);
        }
        //대기자가 아닐때 조회
        if (level != 0) {
            btnSuperManager.setVisibility(View.VISIBLE);
        }
        //최고관리자(3), 어드민(4) 일때 최고관리자메뉴 조회
        if (level == 3 || level == 4) {
            btnAdminMenu.setVisibility(View.VISIBLE);
        }

        //로그인 정보 표시
        String role_name;
        switch (level) {
            case 1:
                role_name = "팀장";
                break;
            case 2:
                role_name = "관리자";
                break;
            case 3:
            case 4:
                role_name = "최고관리자";
                break;
            case -1:
                role_name = "담당자";
                break;
            default:
                role_name = "대기자";
                break;
        }
        txtInfo.setText(place_name + " " + user_name + " " + role_name);


        //출퇴근관리 버튼
        btnAttendMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(team_id.isEmpty()) {
                    choiceAllTeam();
                } else {
                    choiceTeamDialog();
                }
            }
        });

        //팀장님메뉴 버튼
        btnTeamLeaderMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TaskListTeamActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("team_id", team_id);
                intent.putExtra("button_right", ButtonRight.team_leader);
                startActivity(intent);
            }
        });

        //관리자메뉴 버튼
        btnManagerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TaskListActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                intent.putExtra("button_right", ButtonRight.manager);
                startActivity(intent);
            }
        });

        //담당자별조회 버튼
        btnSuperManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(level == -1) {
                    Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("super_manager_name", user_name);
                    intent.putExtra("place_id", place_id);
                    startActivity(intent);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_spinner, null);
                    builder.setView(view);

                    //Shared Preferences에서 담당자 기록 가져오기
                    //SharedPreferences superManagerPrefs = getSharedPreferences("superManagerInfo", MODE_PRIVATE);
                    //String superManagerName = superManagerPrefs.getString("name", "");

                    Spinner spinnerSelectDialog = view.findViewById(R.id.spinnerSelectDialog);

                    TextView textDialogCancel8 = view.findViewById(R.id.textDialogCancel8);
                    TextView textDialogSubmit8 = view.findViewById(R.id.textDialogSubmit8);

                    //API에서 담당자정보 가져오기
                    API.APICallback apiCallback = new API.APICallback() {
                        @Override
                        public void onSuccess(Object data) {
                            String[] _super_manager_names = ((String) data).split(",");
                            ArrayList<String> super_manager_names = new ArrayList<>();
                            super_manager_names.add(0, "담당자 이름");
                            for(String string : _super_manager_names){
                                super_manager_names.add(string);
                            }

                            HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<>(v.getContext(), R.layout.spinner_item, super_manager_names);
                            adapter.setDropDownViewResource(R.layout.spinner_item_drop);
                            spinnerSelectDialog.setAdapter(adapter);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            //취소 선택시
                            textDialogCancel8.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            //확인 선택시
                            textDialogSubmit8.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(spinnerSelectDialog.getSelectedItemPosition() == 0) {
                                        Toast.makeText(v.getContext(), "담당자가 선택되지 않았습니다", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                                        intent.putExtra("level", level);
                                        intent.putExtra("super_manager_name", spinnerSelectDialog.getSelectedItem().toString());
                                        intent.putExtra("place_id", place_id);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }

                                /*
                                if(eTextDialogInput.getText().toString().isEmpty()) {
                                    Toast.makeText(v.getContext(), "담당자 이름이 없습니다", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Shared Preferences에 담당자 기록 저장
                                    SharedPreferences superManagerPrefs = getSharedPreferences("superManagerInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = superManagerPrefs.edit();
                                    editor.putString("name", eTextDialogInput.getText().toString());
                                    editor.commit();

                                    Intent intent = new Intent(v.getContext(), FacFilterActivity.class);
                                    intent.putExtra("level", level);
                                    intent.putExtra("super_manager_name", eTextDialogInput.getText().toString());
                                    intent.putExtra("place_id", place_id);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                                */
                                }
                            });






                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(v.getContext(), "담당자 정보를 가져오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };

                    API api = new API.Builder(apiCallback).build();
                    api.getSuperManagerInfo(place_id);
                }
            }
        });

        //최고관리자메뉴 버튼
        btnAdminMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdminActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("place_id", place_id);
                startActivity(intent);
            }
        });
    }

    //출퇴근관리 - 팀장일경우
    private void choiceTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_find_team_option, null);
        builder.setView(view);

        TextView txtFindMyTeam = view.findViewById(R.id.txtFindMyTeam);
        TextView txtFindAllTeam = view.findViewById(R.id.txtFindAllTeam);
        TextView txtDialogCancel2 = view.findViewById(R.id.textDialogCancel2);

        AlertDialog dialog = builder.create();
        dialog.show();

        txtFindMyTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AttendActivity.class);
                intent.putExtra("team_id", team_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        txtFindAllTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceAllTeam();
                dialog.dismiss();
            }
        });

        txtDialogCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //출퇴근관리 - 모든팀검색
    private void choiceAllTeam() {
        Intent intent = new Intent(this, AttendFilterActivity.class);
        intent.putExtra("place_id", place_id);
        startActivity(intent);
    }
}