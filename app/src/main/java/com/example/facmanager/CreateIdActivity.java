package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_id);

        Resources resource = getResources();

        Spinner spinConstruction = findViewById(R.id.spinConstruction);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, resource.getStringArray(R.array.constructions));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinConstruction.setAdapter(adapter);

        EditText et_username = findViewById(R.id.editTextTextPersonName);
        EditText et_birthday = findViewById(R.id.editTextTextPersonName2);
        Button btn_add_user = findViewById(R.id.button2);

        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = et_username.getText().toString();
                String _birthday = et_birthday.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");

                Date birthday;

                try {

                    birthday = simpleDateFormat.parse(_birthday);

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateIdActivity.this, "유효하지 않은 생년월일입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

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

                api.add_user(username, birthday);



            }
        });




    }
}