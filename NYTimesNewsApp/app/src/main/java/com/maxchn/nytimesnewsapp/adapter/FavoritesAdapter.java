package com.maxchn.nytimesnewsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.callback.FavoritesListCallback;
import com.maxchn.nytimesnewsapp.listener.FavoriteActionListener;
import com.maxchn.nytimesnewsapp.model.Favorites;
import com.maxchn.nytimesnewsapp.viewmodel.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> implements FavoriteActionListener {

    private final List<Favorites> mData;
    private final FavoritesViewModel mViewModel;
    private final FavoritesListCallback mCallback;

    public FavoritesAdapter(List<Favorites> data, FavoritesViewModel viewModel) {
        this.mData = data;
        this.mViewModel = viewModel;

        mCallback = new FavoritesListCallback();
    }

    public void update(List<Favorites> newData) {
        mCallback.setOldList(mData);
        mCallback.setNewList(newData);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mCallback);

        mData.clear();
        mData.addAll(newData);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_template, viewGroup, false);

        return new FavoritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder viewHolder, int position) {
        Favorites favorites = mData.get(position);
        viewHolder.bind(favorites);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onRemove(int id) {
        List<Favorites> newData = new ArrayList<>(mData);

        for (int i = 0; i < newData.size(); i++) {
            if (newData.get(i).getId().equals(id)) {
                newData.remove(i);
                update(newData);
                return;
            }
        }
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

        private void bind(final Favorites favorites) {
            mTitle.setText(favorites.getTitle());
            mSource.setText(favorites.getSource());
            mPublishedDate.setText(favorites.getPublished_date());

            itemView.setOnClickListener(v -> {
                Favorites selectedFavorites = mData.get(getAdapterPosition());

                if (selectedFavorites != null) {
                    mViewModel.setSelected(selectedFavorites);
                }
            });
        }
    }
}