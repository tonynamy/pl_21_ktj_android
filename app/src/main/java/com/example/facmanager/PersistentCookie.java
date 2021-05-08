package com.example.facmanager;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

public class PersistentCookie {

    private static CookieJar cookieJar;

    private static OkHttpClient okHttpClient;


    public static CookieJar getCookieJar() {
        return cookieJar;
    }

    public static void setCookieJar(CookieJar cookieJar) {
        PersistentCookie.cookieJar = cookieJar;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        PersistentCookie.okHttpClient = okHttpClient;
    }
}
