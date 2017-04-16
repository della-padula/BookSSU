package com.terry.librarysearch;

import android.app.Application;
import android.content.Context;

public class LibrarySearchApplication extends Application {
    private static LibrarySearchApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        this.instance = this;
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
