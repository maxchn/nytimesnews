package com.maxchn.nytimesnewsapp.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.activity.DetailsFavoriteActivity;
import com.maxchn.nytimesnewsapp.databinding.FragmentFavoritesBinding;
import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.listener.FavoriteActionListener;
import com.maxchn.nytimesnewsapp.viewmodel.FavoritesViewModel;

import static android.app.Activity.RESULT_OK;

public class FavoritesFragment extends Fragment {

    private static final int REMOVE_FAVORITE_RESULT = 1;
    private FavoriteActionListener mFavoritesActionListener = null;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentFavoritesBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_favorites, container, false);

        FavoritesViewModel viewModel = ViewModelProviders.of(this)
                .get(FavoritesViewModel.class);

        DbHelper dbHelper = new DbHelper(getContext());
        viewModel.setDbHelper(dbHelper);

        binding.setModel(viewModel);
        binding.setLifecycleOwner(this);

        mFavoritesActionListener = viewModel.getFavoritesActionListener();

        viewModel.getSelected().observe(this, result -> {
            if (result != null) {
                Intent intent = new Intent(getContext(), DetailsFavoriteActivity.class);
                intent.putExtra(DetailsFavoriteActivity.ARG_ITEM, result);

                this.startActivityForResult(intent, REMOVE_FAVORITE_RESULT);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        if (requestCode == REMOVE_FAVORITE_RESULT) {
            if (resultCode == RESULT_OK) {
                int id = data.getIntExtra(DetailsFavoriteActivity.REMOVED_ITEM_ID, -1);

                if (id != -1 && mFavoritesActionListener != null) {

                    // Уведомляем адаптер об удалении элемента
                    mFavoritesActionListener.onRemove(id);
                }
            }
        }
    }
}