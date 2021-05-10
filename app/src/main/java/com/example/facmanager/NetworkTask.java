package com.example.facmanager;

import android.content.ContentValues;
import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    public interface NetworkCallback {
        public void onFinished(String result);
    }

    private String url;
    private ContentValues values;
    private Boolean isPOST;
    private NetworkCallback networkCallback;

    public NetworkTask(String url, ContentValues values, Boolean isPOST, NetworkCallback networkCallback) {
        this.url = url;
        this.values = values;
        this.isPOST = isPOST;
        this.networkCallback = networkCallback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;

        if(isPOST) {
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
