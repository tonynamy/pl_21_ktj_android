package com.example.facmanager;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    public interface NetworkCallback {
        public default void onFinished(String result) {
            Log.d("result", result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                int statusCode = jsonObject.getInt("status");

                if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                    onSuccess(jsonObject.getString("results"));
                } else {
                    onFailed(jsonObject.getJSONObject("messages").getString("error"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                onFailed("응답요청 분석에 실패했습니다.");
            }
        }

        public void onSuccess(String result);

        public void onFailed(String error);
    }

    private String url;
    private ContentValues values;
    private Boolean isPOST;
    private NetworkCallback networkCallback;

    public NetworkTask(String url, NetworkCallback networkCallback) {
        this.url = url;
        this.values = null;
        this.isPOST = false;
        this.networkCallback = networkCallback;
    }

    public NetworkTask(String url, ContentValues values, boolean isPOST, NetworkCallback networkCallback) {
        this.url = url;
        this.values = values;
        this.isPOST = isPOST;
        this.networkCallback = networkCallback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;

        if (isPOST) {
            result = RequestHttpUrlConnection.getInstance().requestPOST(this.url, this.values);
        } else {
            result = RequestHttpUrlConnection.getInstance().requestGET(this.url, this.values);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        networkCallback.onFinished(s);
    }
}
