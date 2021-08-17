package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Auth;
import com.poogosoft.facmanager.models.Place;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CheckBox checkRecordLogIn;
    Spinner spinPlace;
    EditText et_username;
    EditText et_birthday;
    Button btnCreateId;
    Button btn_login;

    Boolean isPlaceLoad = false;
    HintSpinnerAdapter<String> adapter;
    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰에서 가져오기
        checkRecordLogIn = findViewById(R.id.checkRecordLogIn);
        spinPlace = findViewById(R.id.spinPlace);
        et_username = findViewById(R.id.tbxName);
        et_birthday = findViewById(R.id.tbxBirth);
        btnCreateId = findViewById(R.id.btnCreateId);
        btn_login = findViewById(R.id.btnLogin);

        checkRecordLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = userPrefs.edit();

                if(isChecked == false) {
                    editor.putBoolean("isChecked", false);
                    editor.putString("userPlace", "");
                    editor.putString("userName", "");
                    editor.putString("userBirthday", "");
                } else {
                    editor.putBoolean("isChecked", true);
                }
                editor.commit();
            }
        });

        //스피너에 넣을 어댑터
        adapter = new HintSpinnerAdapter<String>(this, R.layout.spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_item_drop);
        spinPlace.setAdapter(adapter);

        //EditText선택시 내용삭제
        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(et_username.hasFocus()){
                    et_username.setText("");
                }
            }
        });
        et_birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(et_birthday.hasFocus()){
                    et_birthday.setText("");
                }
            }
        });
        et_birthday.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    btn_login.performClick();
                    return true;
                }
                return false;
            }
        });


        //아이디 생성 버튼
        btnCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaceLoad) {
                    //현장을 선택했다면
                    String place_id;
                    if(spinPlace.getSelectedItemPosition() > 0) {
                        place_id = places.get(spinPlace.getSelectedItemPosition() - 1).id;
                    } else {
                        place_id = "";
                    }

                    String username = et_username.getText().toString().replaceAll("\\s|\\t", "");
                    String birthday = et_birthday.getText().toString().replaceAll("\\s|\\t", "");

                    et_username.setText(username);
                    et_birthday.setText(birthday);

                    API.APICallback apiCallback = new API.APICallback() {

                        @Override
                        public void onSuccess(Object data) {
                            //Shared Preferences에 로그인 기록 저장
                            SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPrefs.edit();
                            editor.putBoolean("isUserInfo", true);
                            if(checkRecordLogIn.isChecked()){
                                editor.putBoolean("isChecked", true);
                                editor.putString("userPlace", place_id);
                                editor.putString("userName", username);
                                editor.putString("userBirthday", birthday);
                            } else {
                                editor.putBoolean("isChecked", false);
                                editor.putString("userPlace", "");
                                editor.putString("userName", "");
                                editor.putString("userBirthday", "");
                            }
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
                            Toast.makeText(MainActivity.this, "로그인에 실패했습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };
                    API api = new API.Builder(apiCallback).build();
                    api.login(place_id, username, birthday);
                } else {
                    Toast.makeText(MainActivity.this, "현장정보를 불러올때까지 기다려주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //로그인정보 불러오기
        loadLogInInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loadLogInInfo();
    }

    //로그인정보 불러오기
    private void loadLogInInfo() {

        isPlaceLoad = false;
        adapter.clear();
        adapter.add("현장명");

        //Shared Preferences에서 이전 로그인 기록 확인
        SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        Boolean isUserInfo = userPrefs.getBoolean("isUserInfo", false);
        Boolean isChecked = userPrefs.getBoolean("isChecked", false);
        String userPlaceInPrefs = userPrefs.getString("userPlace", "0");
        String userNameInPrefs = userPrefs.getString("userName", "");
        String userBirthdayInPrefs = userPrefs.getString("userBirthday", "");

        //Shared Preferences에서 이전 로그인기록에서 이름, 생일 가져오기
        if(isUserInfo){
            checkRecordLogIn.setChecked(isChecked);
            et_username.setText(userNameInPrefs);
            et_birthday.setText(userBirthdayInPrefs);
        }

        //API
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                places = (ArrayList<Place>) data;

                ArrayList<String> place_name = new ArrayList<>();

                for(int i = 0; i < places.size(); i++) {
                    Place place = places.get(i);
                    place_name.add(place.name);
                    //Shared Preferences에서 이전 로그인기록에서 현장정보 가져오기
                    if(isUserInfo && place.id.equals(userPlaceInPrefs)) {
                        spinPlace.setSelection(i + 1);
                    }
                }
                adapter.addAll(place_name);
                adapter.notifyDataSetChanged();
                isPlaceLoad = true;
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(MainActivity.this, "현장정보를 불러오는데 실패했습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getPlaces();
    }
}