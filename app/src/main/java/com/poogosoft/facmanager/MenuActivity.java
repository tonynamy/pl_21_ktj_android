package com.poogosoft.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {

    public static class Dashboard {
        double cube;
        double square;
        int manday;
        int safe_point;
    }

    int level;
    String place_id;
    String place_name;
    String user_name;
    String team_id;

    ArrayList<Place> places;

    TextView txtInfo;
    LinearLayout layoutTeamBoard;
    LinearLayout layoutProductivity;
    TextView textSafePoint;
    Button btnAttendMenu;
    Button btnTeamLeaderMenu;
    Button btnManagerMenu;
    Button btnSuperManager;
    Button btnProductivity;
    Button btnSafePoint;
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
        layoutTeamBoard = findViewById(R.id.layoutTeamBoard);
        layoutProductivity = findViewById(R.id.layoutProductivity);
        textSafePoint = findViewById(R.id.textSafePoint);
        btnAttendMenu = findViewById(R.id.btnAttendMenu);
        btnTeamLeaderMenu = findViewById(R.id.btnTeamLeaderMenu);
        btnManagerMenu = findViewById(R.id.btnManagerMenu);
        btnSuperManager = findViewById(R.id.btnSuperManagerMenu);
        btnProductivity = findViewById(R.id.btnProductivity);
        btnSafePoint = findViewById(R.id.btnSafePoint);
        btnAdminMenu = findViewById(R.id.btnAdminMenu);

        //권한없는 메뉴는 모두 안보이게 하기
        layoutTeamBoard.setVisibility(View.GONE);
        btnAttendMenu.setVisibility(View.GONE);
        btnTeamLeaderMenu.setVisibility(View.GONE);
        btnManagerMenu.setVisibility(View.GONE);
        btnSuperManager.setVisibility(View.GONE);
        btnProductivity.setVisibility(View.GONE);
        btnSafePoint.setVisibility(View.GONE);
        btnAdminMenu.setVisibility(View.GONE);

        //팀장(1), 관리자(2), 최고관리자(3), 어드민(4) 일때 출퇴근관리 조회
        if (level == 1 || level == 2 || level == 3 || level == 4) {
            btnAttendMenu.setVisibility(View.VISIBLE);
            //내 팀이 있을때 팀장님메뉴 조회
            if(!team_id.isEmpty() && level != 4){
                layoutTeamBoard.setVisibility(View.VISIBLE);
                btnTeamLeaderMenu.setVisibility(View.VISIBLE);
            } else if (level == 2 || level == 3 || level == 4) {
                btnProductivity.setVisibility(View.VISIBLE);
                btnSafePoint.setVisibility(View.VISIBLE);
            }
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

        //대시보드 - 생산성(팀장)
        layoutProductivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewProductivity();
            }
        });

        //대시보드 - 안전점수(팀장)
        textSafePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSafePoint();
            }
        });

        //출퇴근관리 버튼
        btnAttendMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(level == 4) {
                    choicePlaceDialog(v.getContext(), TeamSelectActivity.class);
                } else if(team_id.isEmpty()) {
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
                intent.putExtra("button_right", ButtonRight.TEAM_LEADER);
                startActivity(intent);
            }
        });

        //관리자메뉴 버튼
        btnManagerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(level == 4) {
                    choicePlaceDialog(v.getContext(), TaskListActivity.class);
                } else {
                    Intent intent = new Intent(v.getContext(), TaskListActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("place_id", place_id);
                    intent.putExtra("button_right", ButtonRight.MANAGER);
                    startActivity(intent);
                }
            }
        });

        //담당자별조회 버튼
        btnSuperManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //담당자일때
                if(level == -1) {
                    Intent intent = new Intent(v.getContext(), FacSearchActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("super_manager_name", user_name);
                    intent.putExtra("place_id", place_id);
                    startActivity(intent);
                //어드민일때
                } else if(level == 4) {
                    choiceSupermanagerByPlace(v.getContext());

                }else {
                    choiceSupermanager(v.getContext());
                }
            }
        });

        //생산성 버튼(관리자)
        btnProductivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewProductivity();
            }
        });

        //안전점수 버튼(관리자)
        btnSafePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSafePoint();
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

        loadProductivity();
    }

    private void loadProductivity() {

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;

        TextView dashboard_subject = findViewById(R.id.textView5);
        TextView productivity_text = findViewById(R.id.textView6);
        TextView manday_text = findViewById(R.id.textView7);
        TextView safepoint_text = findViewById(R.id.textSafePoint);

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                dashboard_subject.setText(month + "월의 생산성");

                Dashboard dashboard = (Dashboard) data;

                productivity_text.setText(dashboard.cube + " + " + dashboard.square);
                manday_text.setText(String.valueOf(dashboard.manday));
                safepoint_text.setText(String.valueOf(100 + dashboard.safe_point));
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(MenuActivity.this, "생산성 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();
        api.getTeamProductivity(place_id, team_id);


    }

    //생산성 보기
    private void ViewProductivity() {

        if(level == 4) {
            choicePlaceDialog(this, ProductivityActivity.class);
        } else {
            Intent intent = new Intent(this, ProductivityActivity.class);
            intent.putExtra("place_id", place_id);
            startActivity(intent);
        }

    }

    //안전점수 보기
    private void ViewSafePoint() {
        Intent intent = new Intent(this, SafePointActivity.class);
        intent.putExtra("team_id", team_id);
        intent.putExtra("place_id", place_id);
        startActivity(intent);
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

        //내팀조회
        txtFindMyTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AttendActivity.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("team_id", team_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        //전체팀조회
        txtFindAllTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceAllTeam();
                dialog.dismiss();
            }
        });

        //취소
        txtDialogCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //출퇴근관리 - 모든팀검색
    private void choiceAllTeam() {
        Intent intent = new Intent(this, TeamSelectActivity.class);
        intent.putExtra("place_id", place_id);
        startActivity(intent);
    }

    //출퇴근관리 & 관리자메뉴 - 어드민일경우
    private void choicePlaceDialog(Context context, Class intent_class) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_spinner, null);
        builder.setView(view);

        Spinner spinnerSelectDialog = view.findViewById(R.id.spinnerSelectDialog);
        TextView textDialogCancel8 = view.findViewById(R.id.textDialogCancel8);
        TextView textDialogSubmit8 = view.findViewById(R.id.textDialogSubmit8);

        AlertDialog dialog = builder.create();

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                places = (ArrayList<Place>) data;

                HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<String>(context, R.layout.spinner_item, new ArrayList<>());
                adapter.setDropDownViewResource(R.layout.spinner_item_drop);
                adapter.isBlack = true;
                spinnerSelectDialog.setAdapter(adapter);

                ArrayList<String> place_name = new ArrayList<>();

                for(Place place : places) {
                    place_name.add(place.name);
                }

                adapter.addAll(place_name);
                adapter.notifyDataSetChanged();
                dialog.show();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(context, "현장정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getPlaces();


        //취소버튼 눌렀을때
        textDialogCancel8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //확인버튼 눌렀을때
        textDialogSubmit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, intent_class);
                intent.putExtra("place_id", places.get(spinnerSelectDialog.getSelectedItemPosition()).id);
                if(intent_class == TaskListActivity.class) {
                    intent.putExtra("level", level);
                    intent.putExtra("button_right", ButtonRight.MANAGER);
                }
                startActivity(intent);
            }
        });
    }

    //담당자별 조회 - 일반
    private void choiceSupermanager(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_spinner, null);
        builder.setView(view);

        Spinner spinnerSelectDialog = view.findViewById(R.id.spinnerSelectDialog);

        TextView textDialogCancel8 = view.findViewById(R.id.textDialogCancel8);
        TextView textDialogSubmit8 = view.findViewById(R.id.textDialogSubmit8);

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                String[] super_manager_names = ((String) data).split(",");

                HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<>(context, R.layout.spinner_item, new ArrayList<>());
                adapter.setDropDownViewResource(R.layout.spinner_item_drop);
                spinnerSelectDialog.setAdapter(adapter);

                adapter.add("담당자 이름");
                adapter.addAll(super_manager_names);

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
                        String super_manager_name = "";
                        if(spinnerSelectDialog.getSelectedItemPosition() != 0) {
                            super_manager_name = spinnerSelectDialog.getSelectedItem().toString();
                        }
                        Intent intent = new Intent(v.getContext(), FacSearchActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("super_manager_name", super_manager_name);
                        intent.putExtra("place_id", place_id);
                        intent.putExtra("team_id", team_id);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(context, "담당자 정보를 불러오는데 실패했습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();
        api.getSuperManager(place_id);
    }

    //담당자별 조회 - 어드민일 경우
    private void choiceSupermanagerByPlace(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_supermanager_place, null);
        builder.setView(view);

        Spinner spinnerPlace = view.findViewById(R.id.spinnerPlace);

        Spinner spinnerSuperManager = view.findViewById(R.id.spinnerSuperManager);
        HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<>(context, R.layout.spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_item_drop);
        spinnerSuperManager.setAdapter(adapter);
        adapter.add("담당자 이름");
        AlertDialog dialog = builder.create();

        TextView textDialogCancel9 = view.findViewById(R.id.textDialogCancel9);
        TextView textDialogSubmit9 = view.findViewById(R.id.textDialogSubmit9);

        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {
                places = (ArrayList<Place>) data;

                HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<String>(context, R.layout.spinner_item, new ArrayList<>());
                adapter.setDropDownViewResource(R.layout.spinner_item_drop);
                adapter.isBlack = true;
                spinnerPlace.setAdapter(adapter);

                ArrayList<String> place_name = new ArrayList<>();

                for(Place place : places) {
                    place_name.add(place.name);
                }

                adapter.addAll(place_name);
                adapter.notifyDataSetChanged();
                dialog.show();
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(context, "현장정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getPlaces();

        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<>(context, R.layout.spinner_item, new ArrayList<>());
                adapter.setDropDownViewResource(R.layout.spinner_item_drop);
                spinnerSuperManager.setAdapter(adapter);
                adapter.add("담당자 이름");


                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        String[] super_manager_names = ((String) data).split(",");
                        adapter.addAll(super_manager_names);
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(context, "담당자 정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };
                API api = new API.Builder(apiCallback).build();
                api.getSuperManager(places.get(position).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //취소 선택시
        textDialogCancel9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //확인 선택시
        textDialogSubmit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String super_manager_name = "";
                if(spinnerSuperManager.getSelectedItemPosition() != 0) {
                    super_manager_name = spinnerSuperManager.getSelectedItem().toString();
                }
                Intent intent = new Intent(v.getContext(), FacSearchActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("super_manager_name", super_manager_name);
                intent.putExtra("place_id", places.get(spinnerPlace.getSelectedItemPosition()).id);
                intent.putExtra("team_id", team_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });


    }
}