package com.terry.librarysearch.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.terry.librarysearch.LibrarySearchApplication;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    private VolleySingleton(){
        requestQueue = Volley.newRequestQueue(LibrarySearchApplication.getContext());
    }

    public static VolleySingleton getInstance(){
        if (instance == null) {
            instance = new VolleySingleton();
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }
}
