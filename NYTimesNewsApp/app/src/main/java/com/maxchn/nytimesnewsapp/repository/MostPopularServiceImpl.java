package com.maxchn.nytimesnewsapp.repository;

import android.util.Log;

import com.maxchn.nytimesnewsapp.App;
import com.maxchn.nytimesnewsapp.model.MostPopularResult;
import com.maxchn.nytimesnewsapp.model.MostPopularType;
import com.maxchn.nytimesnewsapp.model.Result;
import com.maxchn.nytimesnewsapp.service.NetworkService;

import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularServiceImpl implements MostPopularService {

    private static final String TAG = "MostPopularRepImpl";

    public interface MostPopularRepositoryImplListener {
        void load(List<Result> results, boolean isCache);
    }

    private final MostPopularRepositoryImplListener mListener;

    public MostPopularServiceImpl(MostPopularRepositoryImplListener listener) {
        this.mListener = listener;
    }

    @Override
    public void getAll(MostPopularType type) {
        try {
            Call<MostPopularResult> call = null;

            switch (type) {
                case EMAILED:
                    call = NetworkService.getInstance()
                            .getMostPopularApiService()
                            .getEmailed(App.API_KEY);
                    break;
                case SHARED:
                    call = NetworkService.getInstance()
                            .getMostPopularApiService()
                            .getShared(App.API_KEY);
                    break;
                case VIEWED:
                    call = NetworkService.getInstance()
                            .getMostPopularApiService()
                            .getViewed(App.API_KEY);
                    break;
            }

            if (call != null) {
                call.enqueue(new Callback<MostPopularResult>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<MostPopularResult> call, Response<MostPopularResult> response) {

                        if (response.isSuccessful()) {
                            List<Result> results = response.body() != null ? response.body().getResults() : null;

                            if (mListener != null && results != null)
                                mListener.load(results, false);
                        } else {
                            Log.i(TAG, "onResponse: false");

                            if (mListener != null)
                                mListener.load(null, false);
                        }
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<MostPopularResult> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);

                        if (mListener != null)
                            mListener.load(null, false);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}