package com.terry.librarysearch;

import android.app.Application;
import android.content.Context;
import android.webkit.CookieManager;

import java.net.CookieHandler;
import java.net.CookiePolicy;

public class LibrarySearchApplication extends Application {
    private static LibrarySearchApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        this.instance = this;
        context = getApplicationContext();
        CookieManager.getInstance().setAcceptCookie(true);
        WebkitCookieManagerProxy webkitCookieManagerProxy = new WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(webkitCookieManagerProxy);
    }

    public static Context getContext() {
        return context;
    }
}
