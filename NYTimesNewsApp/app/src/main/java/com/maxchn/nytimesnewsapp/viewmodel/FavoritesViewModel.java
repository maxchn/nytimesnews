package com.maxchn.nytimesnewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.maxchn.nytimesnewsapp.adapter.FavoritesAdapter;
import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.listener.FavoriteActionListener;
import com.maxchn.nytimesnewsapp.model.Favorites;
import com.maxchn.nytimesnewsapp.repository.FavoriteSQLiteRepository;
import com.maxchn.nytimesnewsapp.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends ViewModel {

    private Repository<Favorites> mRepository;
    private final FavoritesAdapter mAdapter;

    private final MutableLiveData<Favorites> mSelected;
    private final MutableLiveData<Integer> mProgressBarVisibility;
    private final MutableLiveData<Integer> mContentFormVisibility;

    public FavoritesViewModel() {
        mAdapter = new FavoritesAdapter(new ArrayList<>(), this);

        mSelected = new MutableLiveData<>();
        mProgressBarVisibility = new MutableLiveData<>();
        mContentFormVisibility = new MutableLiveData<>();
    }

    public void setDbHelper(DbHelper dbHelper) {
        mRepository = new FavoriteSQLiteRepository(dbHelper);

        loadData();
    }

    public FavoritesAdapter getAdapter() {
        return mAdapter;
    }

    public FavoriteActionListener getFavoritesActionListener() {
        return mAdapter;
    }

    public MutableLiveData<Favorites> getSelected() {
        return mSelected;
    }

    public void setSelected(Favorites selected) {
        this.mSelected.setValue(selected);
    }


    public MutableLiveData<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public MutableLiveData<Integer> getContentFormVisibility() {
        return mContentFormVisibility;
    }

    private void loadData() {
        showProgress(true);

        List<Favorites> favoritesList = mRepository.getAll();
        mAdapter.update(favoritesList);

        showProgress(false);
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
}