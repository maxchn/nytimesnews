package com.maxchn.nytimesnewsapp.service;

import com.maxchn.nytimesnewsapp.model.MostPopularResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MostPopularApiService {
    @GET("emailed/30.json")
    Call<MostPopularResult> getEmailed(@Query(value = "api-key", encoded = true) String apiKey);

    @GET("shared/30/facebook.json")
    Call<MostPopularResult> getShared(@Query(value = "api-key", encoded = true) String apiKey);

    @GET("viewed/30.json")
    Call<MostPopularResult> getViewed(@Query(value = "api-key", encoded = true) String apiKey);
}