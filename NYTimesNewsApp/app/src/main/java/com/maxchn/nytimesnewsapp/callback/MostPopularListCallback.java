package com.maxchn.nytimesnewsapp.callback;

import android.support.v7.util.DiffUtil;

import com.maxchn.nytimesnewsapp.model.Result;

import java.util.List;

public class MostPopularListCallback extends DiffUtil.Callback {

    private List<Result> mOldList;
    private List<Result> mNewList;

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return mOldList.get(i).getId().equals(mNewList.get(i1).getId());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return mOldList.get(i).equals(mNewList.get(i1));
    }

    public void setOldList(List<Result> oldList) {
        this.mOldList = oldList;
    }

    public void setNewList(List<Result> newList) {
        this.mNewList = newList;
    }
}