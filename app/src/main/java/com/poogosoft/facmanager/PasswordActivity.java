package com.poogosoft.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    EditText eTextPassword;
    Button buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        eTextPassword = findViewById(R.id.eTextPassword);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "비밀번호란이 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else if(eTextPassword.getText().toString().length() < 8) {
                    Toast.makeText(v.getContext(), "8자리 이상으로 설정해주세요.", Toast.LENGTH_SHORT).show();
                }else if(eTextPassword.getText().toString().contains(" ")) {
                    Toast.makeText(v.getContext(), "공백문자를 없애주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    API.APICallback apiCallback = new API.APICallback() {

                        @Override
                        public void onSuccess(Object data) {

                            SharedPreferences userPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPrefs.edit();
                            editor.putString("userBirthday", "");
                            editor.commit();

                            finish();

                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Toast.makeText(v.getContext(), "비밀번호 변경에 실패했습니다. 사유: " +  errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    };

                    API api = new API.Builder(apiCallback).build();
                    api.editUserPassword(eTextPassword.getText().toString());

                }

            }
        });


    }
}