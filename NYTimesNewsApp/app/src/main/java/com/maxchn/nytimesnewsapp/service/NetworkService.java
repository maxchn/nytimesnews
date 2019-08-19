package com.maxchn.nytimesnewsapp.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_API_URL = "https://api.nytimes.com/svc/mostpopular/v2/";
    private final Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }

        return mInstance;
    }

    public MostPopularApiService getMostPopularApiService() {
        return mRetrofit.create(MostPopularApiService.class);
    }
}