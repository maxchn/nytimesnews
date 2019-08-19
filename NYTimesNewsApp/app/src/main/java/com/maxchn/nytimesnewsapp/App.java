package com.maxchn.nytimesnewsapp;

import android.app.Application;

import com.maxchn.nytimesnewsapp.service.NetworkService;

public class App extends Application {

    public static final String API_KEY = "Mu25pKFA98r3Ht2BCfgEeJEhhjlJ7MVm";

    private final NetworkService mNetworkService;

    public App() {
        super();

        mNetworkService = NetworkService.getInstance();
    }

    public NetworkService getNetworkService() {
        return mNetworkService;
    }
}