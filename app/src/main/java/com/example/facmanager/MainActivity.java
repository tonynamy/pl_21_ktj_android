package com.example.facmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et_username = findViewById(R.id.tbxId);
        EditText et_password = findViewById(R.id.tbxPassword);

        Button btn_login = findViewById(R.id.btnLogin);

        PersistentCookie.setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext())));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(PersistentCookie.getCookieJar())
                .build();

        PersistentCookie.setOkHttpClient(okHttpClient);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.URL)
                        .client(PersistentCookie.getOkHttpClient())
                        .build();

                APIService apiService = retrofit.create(APIService.class);

                Call<ResponseBody> login = apiService.login(username, password);
                login.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            Log.d("login", response.body().string());

                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
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
        });
    }
}