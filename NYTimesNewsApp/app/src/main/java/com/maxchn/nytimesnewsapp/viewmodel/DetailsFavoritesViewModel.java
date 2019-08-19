package com.maxchn.nytimesnewsapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.Favorites;
import com.maxchn.nytimesnewsapp.repository.Repository;
import com.maxchn.nytimesnewsapp.repository.FavoriteSQLiteRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFavoritesViewModel extends ViewModel {

    private static final String TAG = "DetailsFavoritesVM";

    private ExecutorService mExecutor;
    private String mFilesDir;
    private Favorites mFavorites;
    private Repository<Favorites> mRepository;

    private final MutableLiveData<Integer> mProgressBarVisibility;
    private final MutableLiveData<Integer> mContentFormVisibility;
    private final MutableLiveData<String> mContent;
    private final MutableLiveData<Integer> mRemovalResult;

    public DetailsFavoritesViewModel() {
        mProgressBarVisibility = new MutableLiveData<>();
        mContentFormVisibility = new MutableLiveData<>();
        mContent = new MutableLiveData<>();
        mRemovalResult = new MutableLiveData<>();

        showProgress(false);
    }

    public void setFilesDir(String filesDir) {
        this.mFilesDir = filesDir;
    }

    public void setFavorites(Favorites favorites) {
        this.mFavorites = favorites;

        loadData();
    }

    public void setDbHelper(DbHelper dbHelper) {
        mRepository = new FavoriteSQLiteRepository(dbHelper);
    }

    public LiveData<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public LiveData<Integer> getContentFormVisibility() {
        return mContentFormVisibility;
    }

    public LiveData<String> getContent() {
        return mContent;
    }

    public LiveData<Integer> getRemovalResult() {
        return mRemovalResult;
    }

    private void loadData() {
        showProgress(true);
        loadContent();
    }

    private void loadContent() {

        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mExecutor = null;
        }

        mExecutor = Executors.newSingleThreadExecutor();

        mExecutor.submit(() -> {
            String path = String.format("%s%s%s", mFilesDir, File.separator, mFavorites.getData());

            try (FileReader fileReader = new FileReader(path);
                 BufferedReader reader = new BufferedReader(fileReader)) {
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                mContent.postValue(builder.toString());
                showProgress(false);
            } catch (IOException e) {
                Log.e(TAG, "saveData: ", e);
            } finally {
                mProgressBarVisibility.postValue(View.GONE);
                mContentFormVisibility.postValue(View.VISIBLE);
            }
        });
    }

    private void showProgress(boolean show) {
        if (show) {
            mProgressBarVisibility.setValue(View.VISIBLE);
            mContentFormVisibility.setValue(View.INVISIBLE);
        } else {
            mProgressBarVisibility.setValue(View.GONE);
            mContentFormVisibility.setValue(View.VISIBLE);
        }
    }

    public void removeFavorites() {
        try {
            String fileName = mFavorites.getData();

            if (mRepository.delete(mFavorites.getId())) {
                try {
                    String path = String.format("%s%s%s", mFilesDir, File.separator, fileName);

                    File file = new File(path);

                    if (file.exists()) {
                        if (!file.delete()) {
                            throw new Exception("File not deleted!!!");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "removeFavorites: ", e);
                }

                mRemovalResult.setValue(mFavorites.getId());
            } else {
                mRemovalResult.setValue(-1);
            }
        } catch (Exception e) {
            Log.e(TAG, "removeCurrentFavorites: ", e);
            mRemovalResult.setValue(-1);
        }
    }
}
