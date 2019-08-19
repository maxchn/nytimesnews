package com.maxchn.nytimesnewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.maxchn.nytimesnewsapp.adapter.MostPopularAdapter;
import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.MostPopularType;
import com.maxchn.nytimesnewsapp.model.Result;
import com.maxchn.nytimesnewsapp.repository.MostPopularService;
import com.maxchn.nytimesnewsapp.repository.MostPopularServiceImpl;
import com.maxchn.nytimesnewsapp.repository.MostPopularSQLiteRepository;
import com.maxchn.nytimesnewsapp.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class MostPopularViewModel extends ViewModel implements MostPopularServiceImpl.MostPopularRepositoryImplListener {

    private MostPopularType mType;
    private final MostPopularService mService;
    private final MostPopularAdapter mAdapter;
    private final MutableLiveData<Result> mSelected;
    private final MutableLiveData<Integer> mProgressBarVisibility;
    private final MutableLiveData<Integer> mContentFormVisibility;
    private final MutableLiveData<Boolean> mRefreshStatus;
    private Repository<Result> mRepository;

    public MostPopularViewModel() {
        mType = MostPopularType.SHARED;
        mService = new MostPopularServiceImpl(this);
        mAdapter = new MostPopularAdapter(new ArrayList<>(), this);

        mSelected = new MutableLiveData<>();
        mProgressBarVisibility = new MutableLiveData<>();
        mContentFormVisibility = new MutableLiveData<>();
        mRefreshStatus = new MutableLiveData<>();
    }

    public void setDbHelper(DbHelper dbHelper) {
        mRepository = new MostPopularSQLiteRepository(dbHelper);
    }

    public void setType(MostPopularType type) {
        this.mType = type;

        loadData();
    }

    public MutableLiveData<Result> getSelected() {
        return mSelected;
    }

    public void setSelected(Result selectedResult) {
        this.mSelected.setValue(selectedResult);
    }

    public MutableLiveData<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public MutableLiveData<Integer> getContentFormVisibility() {
        return mContentFormVisibility;
    }

    public MutableLiveData<Boolean> getRefreshStatus() {
        return mRefreshStatus;
    }

    private void loadData() {
        showProgress(true);

        String[] args = new String[]{mType.toString()};
        List<Result> items = mRepository.find(DbHelper.MostPopularFields.TYPE + " LIKE ?", args);

        // Проверяем наличие данных в кэше
        if (items.size() > 0) {
            // если есть то загружаем данный из кэша
            this.load(items, true);
        } else {
            // иначе грузим из сети
            mService.getAll(mType);
        }
    }

    public void loadDateFromNetwork() {
        mService.getAll(mType);
    }

    public MostPopularAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void load(List<Result> results, boolean isCache) {
        if (results != null) {
            mAdapter.update(results);

            if (!isCache) {
                List<Result> items = mRepository.find(DbHelper.MostPopularFields.TYPE + " LIKE ?", new String[]{mType.toString()});

                // Если в кэше есть данные
                if (items.size() > 0) {
                    // то удаляем их
                    for (Result item : items) {
                        mRepository.delete(item.getId());
                    }
                }

                for (Result item : results) {
                    item.setType(mType);
                    mRepository.create(item);
                }
            }
        }

        mRefreshStatus.postValue(false);
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