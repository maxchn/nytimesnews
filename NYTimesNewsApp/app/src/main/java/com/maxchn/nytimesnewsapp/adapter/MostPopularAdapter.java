package com.maxchn.nytimesnewsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.callback.MostPopularListCallback;
import com.maxchn.nytimesnewsapp.model.Result;
import com.maxchn.nytimesnewsapp.viewmodel.MostPopularViewModel;

import java.util.List;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.ViewHolder> {

    private final List<Result> mData;
    private final MostPopularViewModel mViewModel;
    private final MostPopularListCallback mCallback;

    public MostPopularAdapter(List<Result> data, MostPopularViewModel viewModel) {
        this.mData = data;
        this.mViewModel = viewModel;

        mCallback = new MostPopularListCallback();
    }

    public void update(List<Result> newData) {
        mCallback.setOldList(mData);
        mCallback.setNewList(newData);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mCallback);

        mData.clear();
        mData.addAll(newData);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MostPopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_template, viewGroup, false);

        return new MostPopularAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPopularAdapter.ViewHolder viewHolder, int position) {
        Result result = mData.get(position);
        viewHolder.bind(result);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final TextView mSource;
        private final TextView mPublishedDate;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tvTitle);
            mSource = itemView.findViewById(R.id.tvSource);
            mPublishedDate = itemView.findViewById(R.id.tvPublishedDate);
        }

        private void bind(final Result result) {
            mTitle.setText(result.getTitle());
            mSource.setText(result.getSource());
            mPublishedDate.setText(result.getPublishedDate());

            itemView.setOnClickListener(v -> {
                final Result result1 = mData.get(getAdapterPosition());

                if (result1 != null) {
                    mViewModel.setSelected(result1);
                }
            });
        }
    }
}