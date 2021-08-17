package com.poogosoft.facmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Place;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    int level;
    String place_id;

    ArrayList<Place> places;

    Button btnMandayMenu;
    Button btnPlaceSet;
    Button btnUserLevelSet;
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Intent에서 Extra 가져오기
        level = getIntent().getIntExtra("level", 0);
        place_id = getIntent().getStringExtra("place_id");

        //뷰 가져오기
        btnMandayMenu = findViewById(R.id.btnMandayMenu);
        btnPlaceSet = findViewById(R.id.btnPlaceSet);
        btnUserLevelSet = findViewById(R.id.btnUserLevelSet);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        //권한에 따른 버튼 활성화
        if(level == 4) {
            btnPlaceSet.setVisibility(View.VISIBLE);
        } else if(level == 3) {
            btnPlaceSet.setVisibility(View.GONE);
        } else {
            btnPlaceSet.setVisibility(View.GONE);
            btnUserLevelSet.setVisibility(View.GONE);
            btnChangePassword.setVisibility(View.GONE);
        }

        //공수조회버튼 눌렀을때
        btnMandayMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MandaySearchActivity.class);
                startActivity(intent);
            }
        });

        //현장관리버튼 눌렀을때
        btnPlaceSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaceActivity.class);
                startActivity(intent);
            }
        });

        //직원등급관리 눌렀을때
        btnUserLevelSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //어드민일 경우
                if(level == 4) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_select_spinner, null);
                    builder.setView(view);

                    Spinner spinnerSelectDialog = view.findViewById(R.id.spinnerSelectDialog);
                    TextView textDialogCancel8 = view.findViewById(R.id.textDialogCancel8);
                    TextView textDialogSubmit8 = view.findViewById(R.id.textDialogSubmit8);
                    AlertDialog dialog = builder.create();

                    API.APICallback apiCallback = new API.APICallback() {
                        @Override
                        public void onSuccess(Object data) {
                            places = (ArrayList<Place>) data;

                            HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<String>(v.getContext(), R.layout.spinner_item, new ArrayList<>());
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
                            Toast.makeText(v.getContext(), "현장정보를 불러오는데 실패했습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                            intent.putExtra("level", level);
                            intent.putExtra("place_id", places.get(spinnerSelectDialog.getSelectedItemPosition()).id);
                            startActivity(intent);
                        }
                    });


                } else {
                    Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("place_id", place_id);
                    startActivity(intent);
                }
            }
        });

        //비밀번호변경 눌렀을때
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(v.getContext(), "비밀번호를 변경할수 없습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };
                API api = new API.Builder(apiCallback).build();
                api.authCheck();

            }
        });
    }
}