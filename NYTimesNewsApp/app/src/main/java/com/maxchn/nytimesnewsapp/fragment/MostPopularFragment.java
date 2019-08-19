package com.maxchn.nytimesnewsapp.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.activity.DetailsActivity;
import com.maxchn.nytimesnewsapp.databinding.FragmentMostPopularBinding;
import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.MostPopularType;
import com.maxchn.nytimesnewsapp.viewmodel.MostPopularViewModel;

public class MostPopularFragment extends Fragment {

    private static final String ARG_TYPE = "MostPopularType";
    private MostPopularType mType;

    public MostPopularFragment() {
        // Required empty public constructor
    }

    public static MostPopularFragment newInstance(MostPopularType type) {
        MostPopularFragment fragment = new MostPopularFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mType = (MostPopularType) getArguments().get(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentMostPopularBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_most_popular, container, false);

        MostPopularViewModel viewModel = ViewModelProviders.of(this)
                .get(MostPopularViewModel.class);

        viewModel.setDbHelper(new DbHelper(getContext()));
        viewModel.setType(mType);

        binding.setModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getSelected().observe(this, result -> {
            if (result != null) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.ARG_ITEM, result);

                FragmentActivity fragmentActivity = getActivity();

                if (fragmentActivity != null) {
                    fragmentActivity.startActivity(intent);
                }
            }
        });

        SwipeRefreshLayout mostPopularSwipeRefresh = binding.mostPopularSwipeRefresh;
        mostPopularSwipeRefresh.setOnRefreshListener(viewModel::loadDateFromNetwork);

        viewModel.getRefreshStatus().observe(this, status -> {
            if (status != null) {
                mostPopularSwipeRefresh.setRefreshing(status);
            }
        });

        return binding.getRoot();
    }
}