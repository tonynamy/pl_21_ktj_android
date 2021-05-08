package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AttendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.URL)
                .client(PersistentCookie.getOkHttpClient())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Button btnAttend = findViewById(R.id.btnAttend);

        Call<ResponseBody> attendance = apiService.attendance();
        attendance.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    int count = Integer.parseInt(response.errorBody().string());

                    if(count == 0) { // 출근 버튼 보여주기

                        btnAttend.setText("출근");

                        Call<ResponseBody> attendance_on = apiService.attendance_on();
                        attendance_on.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                // 성공
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    } else if (count == 1) { // 퇴근 버튼 보여주기

                        btnAttend.setText("퇴근");

                        Call<ResponseBody> attendance_off = apiService.attendance_off();
                        attendance_off.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                // 성공
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                   } else { // 퇴근 완료 버튼 보여주기

                        btnAttend.setText("퇴근 완료");
                        btnAttend.setEnabled(false);
                        btnAttend.setOnClickListener(null);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("error", t.getMessage());

            }
        });

    }
}