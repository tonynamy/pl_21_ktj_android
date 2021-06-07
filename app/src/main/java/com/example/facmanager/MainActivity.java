package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resource = getResources();

        Spinner spinConstruction = findViewById(R.id.spinConstruction);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, resource.getStringArray(R.array.constructions));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinConstruction.setAdapter(adapter);

        EditText et_username = findViewById(R.id.tbxId);

        Button btnCreateId = findViewById(R.id.btnCreateId);
        btnCreateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateIdActivity.class);
                startActivity(intent);
            }
        });

        Button btn_login = findViewById(R.id.btnLogin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();

                API.APICallback apiCallback = new API.APICallback() {

                    @Override
                    public void onSuccess(Object data) {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(MainActivity.this, "로그인에 실패했습니다. 사유: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };

                API api = new API.Builder(apiCallback).build();

                api.login(username);
            }
        });
    }
}