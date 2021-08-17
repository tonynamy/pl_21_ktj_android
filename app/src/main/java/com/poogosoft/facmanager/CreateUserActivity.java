package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.poogosoft.facmanager.models.Place;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateUserActivity extends AppCompatActivity {

    Spinner spinCreatePlace;
    EditText etextCreateUserName;
    LinearLayout layoutBirthday;
    LinearLayout layoutBirthdayPicker;
    DatePicker pickerCreateUserBirth;
    Button btnCreateUser;

    Boolean isPlaceLoad = false;
    Boolean isBirthdayOpen = false;
    HintSpinnerAdapter<String> adapter;
    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //뷰에서 가져오기
        spinCreatePlace = findViewById(R.id.spinCreatePlace);
        etextCreateUserName = findViewById(R.id.etextCreateUserName);
        layoutBirthday = findViewById(R.id.layoutBirthday);
        layoutBirthdayPicker = findViewById(R.id.layoutBirthdayPicker);
        pickerCreateUserBirth = findViewById(R.id.pickerCreateUserBirth);
        btnCreateUser = findViewById(R.id.btnCreateUser);

        //스피너 설정
        spinCreatePlace.setVisibility(View.GONE);
        adapter = new HintSpinnerAdapter<String>(this, R.layout.spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_item_drop);
        adapter.isBlack = true;
        spinCreatePlace.setAdapter(adapter);

        //이름칸에서 Enter치면 Picker열림
        etextCreateUserName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    layoutBirthday.performClick();
                    return true;
                }
                return false;
            }
        });

        //데이트피커설정
        pickerCreateUserBirth.updateDate(1970,0,1);
        layoutBirthdayPicker.setVisibility(View.GONE);

        //생년월일 글자 눌렀을때
        layoutBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(etextCreateUserName.getWindowToken(), 0);
                layoutBirthdayPicker.setVisibility(View.VISIBLE);
                isBirthdayOpen = true;
            }
        });

        //생성버튼
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isPlaceLoad) {
                    Toast.makeText(v.getContext(), "현장정보를 불러올때까지 기다려주세요.", Toast.LENGTH_SHORT).show();
                } else if(etextCreateUserName.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(!isBirthdayOpen) {
                    Toast.makeText(v.getContext(), "생년월일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    String place_id = places.get(spinCreatePlace.getSelectedItemPosition()).id;
                    String username = etextCreateUserName.getText().toString().replaceAll("\\s|\\t", "");

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(pickerCreateUserBirth.getYear(), pickerCreateUserBirth.getMonth(), pickerCreateUserBirth.getDayOfMonth());
                    Date birthday = calendar.getTime();

                    API.APICallback apiCallback = new API.APICallback() {
                        @Override
                        public void onSuccess(Object data) {
                            SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPrefs.edit();
                            editor.putBoolean("isUserInfo", true);
                            editor.putString("userPlace", place_id);
                            editor.putString("userName", "");
                            editor.putString("userBirthday", "");
                            editor.commit();

                            Toast.makeText(CreateUserActivity.this, "사용자 생성에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(CreateUserActivity.this, "사용자 생성에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };
                    API api = new API.Builder(apiCallback).build();
                    api.addUser(place_id, username, birthday);

                }

            }
        });

        //현장정보 API
        API.APICallback apiCallback = new API.APICallback() {
            @Override
            public void onSuccess(Object data) {

                places = (ArrayList<Place>) data;

                ArrayList<String> place_name = new ArrayList<>();

                for(Place place : places) {
                    place_name.add(place.name);
                }
                adapter.addAll(place_name);
                adapter.notifyDataSetChanged();
                spinCreatePlace.setVisibility(View.VISIBLE);
                isPlaceLoad = true;
            }
            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(CreateUserActivity.this, "현장정보를 불러오는데 실패했습니다.\n사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        API api = new API.Builder(apiCallback).build();
        api.getPlaces();

    }
}