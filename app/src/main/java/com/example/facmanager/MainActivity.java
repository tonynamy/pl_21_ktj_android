package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.facmanager.models.Auth;
import com.example.facmanager.models.Place;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        int userPlaceInPrefs = userPrefs.getInt("userPlace", 0);
        String userNameInPrefs = userPrefs.getString("userName", "");
        String userBirthdayInPrefs = userPrefs.getString("userBirthday", "");

        Spinner spinPlace = findViewById(R.id.spinPlace);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinPlace.setAdapter(adapter);

        EditText et_username = findViewById(R.id.tbxName);
        EditText et_birthday = findViewById(R.id.tbxBirth);

        et_username.setText(userNameInPrefs);
        et_birthday.setText(userBirthdayInPrefs);

        Button btnCreateId = findViewById(R.id.btnCreateId);
        btnCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateIdActivity.class);
                startActivity(intent);
            }
        });

        Button btn_login = findViewById(R.id.btnLogin);

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
                spinPlace.setSelection(userPlaceInPrefs);
            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(MainActivity.this, "현장정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getPlaces();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place_id = places.get(spinPlace.getSelectedItemPosition()).id;
                String username = et_username.getText().toString();
                String birthday = et_birthday.getText().toString();

                API.APICallback apiCallback = new API.APICallback() {

                    @Override
                    public void onSuccess(Object data) {

                        SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPrefs.edit();
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
            }
        });
    }
}