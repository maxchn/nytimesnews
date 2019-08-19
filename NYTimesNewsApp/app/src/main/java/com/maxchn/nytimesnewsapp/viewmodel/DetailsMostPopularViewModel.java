package com.maxchn.nytimesnewsapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.Favorites;
import com.maxchn.nytimesnewsapp.model.Result;
import com.maxchn.nytimesnewsapp.repository.Repository;
import com.maxchn.nytimesnewsapp.repository.FavoriteSQLiteRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

public class DetailsMostPopularViewModel extends ViewModel {

    private static final String TAG = "DetailsMostPopularVM";

    private String mFilesDir;
    private Result mResult;
    private Repository<Favorites> mRepository;

    private final MutableLiveData<Integer> mProgressBarVisibility;
    private final MutableLiveData<Integer> mContentFormVisibility;
    private final MutableLiveData<Integer> mAddToFavoritesVisibility;
    private final MutableLiveData<String> mContent;
    private final MutableLiveData<Boolean> mResultAddingToFavorites;
    private final MutableLiveData<Boolean> mLoadData;

    public DetailsMostPopularViewModel() {
        mFilesDir = null;
        mProgressBarVisibility = new MutableLiveData<>();
        mContentFormVisibility = new MutableLiveData<>();
        mAddToFavoritesVisibility = new MutableLiveData<>();
        mContent = new MutableLiveData<>();
        mResultAddingToFavorites = new MutableLiveData<>();
        mLoadData = new MutableLiveData<>();

        mProgressBarVisibility.setValue(View.GONE);
        mContentFormVisibility.setValue(View.VISIBLE);
        mAddToFavoritesVisibility.setValue(View.VISIBLE);
    }

    public void setFilesDir(String filesDir) {
        this.mFilesDir = filesDir;
    }

    public void setResult(Result result) {
        this.mResult = result;

        loadData();
        checkIsShowAddToFavorite();
    }

    public void setDbHelper(DbHelper dbHelper) {
        mRepository = new FavoriteSQLiteRepository(dbHelper);
    }

    public LiveData<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public LiveData<Integer> getAddToFavoritesVisibility() {
        return mAddToFavoritesVisibility;
    }

    public LiveData<Integer> getContentFormVisibility() {
        return mContentFormVisibility;
    }

    public LiveData<String> getContent() {
        return mContent;
    }

    public LiveData<Boolean> getResultAddingToFavorites() {
        return mResultAddingToFavorites;
    }

    public MutableLiveData<Boolean> getLoadData() {
        return mLoadData;
    }

    private void checkIsShowAddToFavorite() {
        try {
            List<Favorites> favorites = mRepository.find(DbHelper.FavoriteFields.URL + " LIKE ?", new String[]{"%" + mResult.getUrl() + "%"});
            mAddToFavoritesVisibility.setValue(favorites != null && favorites.size() > 0 ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "checkIsShowAddToFavorite: ", e);
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            mProgressBarVisibility.postValue(View.VISIBLE);
            mContentFormVisibility.postValue(View.INVISIBLE);
        } else {
            mProgressBarVisibility.postValue(View.GONE);
            mContentFormVisibility.postValue(View.VISIBLE);
        }
    }

    private void loadData() {
        showProgress(true);

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(mResult.getUrl())
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                @EverythingIsNonNull
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: ", e);
                    mLoadData.postValue(false);
                }

                @Override
                @EverythingIsNonNull
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        String html = response.body() != null ? response.body().string() : null;

                        if (html != null) {
                            Document document = Jsoup.parse(html);
                            document.select("meta,script,hidden,form").remove();

                            final String clearedHtml = document.html();

                            mContent.postValue(clearedHtml);
                            showProgress(false);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "loadData: ", e);
        }
    }

    public void addToFavorites() {
        try {
            if (mResult != null && !TextUtils.isEmpty(mContent.getValue())) {
                Favorites favorites = new Favorites();

                favorites.setTitle(mResult.getTitle());
                favorites.setPublished_date(mResult.getPublishedDate());
                favorites.setUpdated(mResult.getUpdated());
                favorites.setUrl(mResult.getUrl());
                favorites.setSource(mResult.getSource());

                String filename = mResult.getUrl().replaceAll("[^a-zA-Z]", "") + ".html";
                String path = mFilesDir + File.separator + filename;

                favorites.setData(filename);

                try {
                    if (mRepository.create(favorites)) {
                        try {
                            File file = new File(path);

                            if (!file.exists()) {
                                saveData(mContent.getValue(), path);

                                mResultAddingToFavorites.setValue(true);
                                mAddToFavoritesVisibility.setValue(View.GONE);
                            } else {
                                mResultAddingToFavorites.setValue(false);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "addToFavorites: ", e);
                            mResultAddingToFavorites.setValue(false);
                        }
                    } else {
                        mResultAddingToFavorites.setValue(false);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "addToFavorites: ", e);

                    mResultAddingToFavorites.setValue(false);
                }
            } else {
                mResultAddingToFavorites.setValue(false);
            }
        } catch (Exception e) {
            Log.e(TAG, "removeCurrentFavorites: ", e);
            mResultAddingToFavorites.setValue(false);
        }
    }

    private void saveData(String html, String path) {

        try (FileWriter fileWriter = new FileWriter(path);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(html);
        } catch (IOException e) {
            Log.e(TAG, "saveData: ", e);
        }
    }
}
