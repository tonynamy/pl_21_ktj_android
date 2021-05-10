package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et_username = findViewById(R.id.tbxId);
        EditText et_password = findViewById(R.id.tbxPassword);

        Button btn_login = findViewById(R.id.btnLogin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                API.APICallback apiCallback = new API.APICallback() {
                    @Override
                    public void onSuccess(String data) {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        Toast.makeText(MainActivity.this, "로그인에 실패했습니다. 사유 : "+errorMsg, Toast.LENGTH_SHORT).show();
                    }
                };

                API api = new API.Builder(apiCallback).build();

                api.login(username, password);
            }
        });
    }
}