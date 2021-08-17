package com.poogosoft.facmanager;

import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class FacApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }
}
