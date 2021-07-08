package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.facmanager.models.Place;
import com.example.facmanager.models.Team;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateIdActivity extends AppCompatActivity {

    DatePicker pickerCreateUserBirth;
    ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_id);

        Spinner spinConstruction = findViewById(R.id.spinCreatePlace);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinConstruction.setAdapter(adapter);

        EditText et_username = findViewById(R.id.tbxCreateUserName);
        pickerCreateUserBirth = findViewById(R.id.pickerCreateUserBirth);
        Button btn_add_user = findViewById(R.id.btnCreateUser);

        pickerCreateUserBirth.init(1970,0,1,null);

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

            }

            @Override
            public void onFailed(String errorMsg) {
                Toast.makeText(CreateIdActivity.this, "현장정보를 불러오는데 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        API api = new API.Builder(apiCallback).build();

        api.getPlaces();

        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place_id = places.get(spinConstruction.getSelectedItemPosition()).id;
                String username = et_username.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.set(pickerCreateUserBirth.getYear(), pickerCreateUserBirth.getMonth(), pickerCreateUserBirth.getDayOfMonth());
                Date birthday = calendar.getTime();

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(Object data) {
                        Toast.makeText(CreateIdActivity.this, "사용자 생성에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateIdActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(CreateIdActivity.this, "사용자 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                };

                API api = new API.Builder(apiCallback).build();

                api.add_user(place_id, username, birthday);
            }
        });

    }
}