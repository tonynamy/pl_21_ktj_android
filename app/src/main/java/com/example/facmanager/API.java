package com.example.facmanager;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class API {

    public interface APICallback {
        public void onSuccess(String data);
        public void onFailed(String errorMsg);
    }

    public static class Builder {

        private final APICallback apiCallback;

        public Builder(APICallback apiCallback) {
            this.apiCallback = apiCallback;
        }

        public API build() {
            return new API(this);
        }
    }

    private APICallback apiCallback;

    public API(Builder builder) {
        this.apiCallback = builder.apiCallback;
    }

    public void login(String username, String password) {

        String url = "http://49.247.19.209/";
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {
            @Override
            public void onFinished(String result) {

                Log.d("result", result);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    int statusCode = jsonObject.getInt("status");

                    if(statusCode== HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {

                        apiCallback.onSuccess(jsonObject.getString("results"));

                    } else {

                        apiCallback.onFailed(jsonObject.getJSONObject("messages").getString("error"));

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    apiCallback.onFailed("응답 요청 분석에 실패했습니다.");
                }

            }
        };

        new NetworkTask(url, values, true, networkCallback).execute();


    }
}
