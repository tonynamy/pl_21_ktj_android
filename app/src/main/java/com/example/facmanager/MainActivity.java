package com.example.facmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facmanager.models.Auth;
import com.example.facmanager.models.Place;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner spinPlace;
    EditText et_username;
    EditText et_birthday;
    Button btnCreateId;
    Button btn_login;

    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰에서 가져오기
        spinPlace = findViewById(R.id.spinPlace);
        et_username = findViewById(R.id.tbxName);
        et_birthday = findViewById(R.id.tbxBirth);
        btnCreateId = findViewById(R.id.btnCreateId);
        btn_login = findViewById(R.id.btnLogin);

        //Shared Preferences에서 이전 로그인 기록 확인
        SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        Boolean isUserInfo = userPrefs.getBoolean("isUserInfo", false);
        int userPlaceInPrefs = userPrefs.getInt("userPlace", 0);
        String userNameInPrefs = userPrefs.getString("userName", "");
        String userBirthdayInPrefs = userPrefs.getString("userBirthday", "");

        //Shared Preferences에서 이전 로그인기록 이름, 생일에 넣기
        if(isUserInfo){
            et_username.setText(userNameInPrefs);
            et_birthday.setText(userBirthdayInPrefs);
        }

        //스피너에 넣을 어댑터
        HintSpinnerAdapter<String> adapter = new HintSpinnerAdapter<String>(this, R.layout.spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_item_drop);
        spinPlace.setAdapter(adapter);

        //API
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                places = (ArrayList<Place>) data;

                ArrayList<String> place_name = new ArrayList<>();
                place_name.add(0, "현장명");

                for(Place place : places) {
                    place_name.add(place.name);
                }

                adapter.addAll(place_name);
                adapter.notifyDataSetChanged();

                //Shared Preferences에서 이전 로그인기록 Spinner에 넣기
                if(isUserInfo){
                    spinPlace.setSelection(userPlaceInPrefs);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(MainActivity.this, "현장정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();
        api.getPlaces();


        //아이디 생성 버튼
        btnCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateIdActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinPlace.getAdapter() == adapter) {
                    String place_id = "";
                    if(spinPlace.getSelectedItemPosition() > 0) {
                        place_id = places.get(spinPlace.getSelectedItemPosition() - 1).id;
                    }
                    String username = et_username.getText().toString();
                    String birthday = et_birthday.getText().toString();

                    API.APICallback apiCallback = new API.APICallback() {

                        @Override
                        public void onSuccess(Object data) {
                            //Shared Preferences에 로그인 기록 저장
                            SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPrefs.edit();
                            editor.putBoolean("isUserInfo", true);
                            editor.putInt("userPlace", spinPlace.getSelectedItemPosition());
                            editor.putString("userName", et_username.getText().toString());
                            editor.putString("userBirthday", et_birthday.getText().toString());
                            editor.commit();

                            Auth auth = (Auth) data;

                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            intent.putExtra("level", auth.level);
                            intent.putExtra("place_id", auth.place.id);
                            intent.putExtra("place_name", auth.place.name);
                            intent.putExtra("user_name", auth.user_name);
                            intent.putExtra("team_id", auth.team_id);

                            startActivity(intent);
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(MainActivity.this, "로그인에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };

                    API api = new API.Builder(apiCallback).build();

                    api.login(place_id, username, birthday);

                } else {
                    Toast.makeText(v.getContext(), "현장명을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}