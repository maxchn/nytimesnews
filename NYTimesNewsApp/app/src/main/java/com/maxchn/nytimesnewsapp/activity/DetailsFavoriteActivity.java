package com.maxchn.nytimesnewsapp.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.databinding.ActivityDetailsFavoriteBinding;
import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.Favorites;
import com.maxchn.nytimesnewsapp.viewmodel.DetailsFavoritesViewModel;

public class DetailsFavoriteActivity extends AppCompatActivity {

    private static final String TAG = "DetailsFavoriteActivity";
    public static final String ARG_ITEM = "arg_item";
    public static final String REMOVED_ITEM_ID = "arg_removed_item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // Если данные отсутствуют или произошла ошибка то закрываем текущую активность
        if (intent != null && intent.hasExtra(ARG_ITEM)) {
            try {
                Favorites favorites = intent.getParcelableExtra(ARG_ITEM);
                bindData(favorites);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: ", e);
                finish();
            }
        } else {
            finish();
        }
    }

    private void bindData(Favorites favorites) {
        ActivityDetailsFavoriteBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_details_favorite);

        final DetailsFavoritesViewModel viewModel
                = ViewModelProviders.of(this).get(DetailsFavoritesViewModel.class);

        DbHelper dbHelper = new DbHelper(this);
        viewModel.setFilesDir(getFilesDir().getAbsolutePath());
        viewModel.setDbHelper(dbHelper);
        viewModel.setFavorites(favorites);

        binding.setModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getRemovalResult().observe(this, r -> {
            // Отображаем результат удаления статьи из избранного
            if (r != null && r != -1) {
                showToast(getString(R.string.removing_favorites_successfully));

                Intent intent = new Intent();
                intent.putExtra(DetailsFavoriteActivity.REMOVED_ITEM_ID, r);

                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                showToast(getString(R.string.removing_favorites_error));
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}