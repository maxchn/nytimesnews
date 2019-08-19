package com.maxchn.nytimesnewsapp.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.databinding.ActivityDetailsBinding;
import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.Result;
import com.maxchn.nytimesnewsapp.viewmodel.DetailsMostPopularViewModel;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    public static final String ARG_ITEM = "arg_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // Если данные отсутствуют или произошла ошибка то закрываем текущую активность
        if (intent != null && intent.hasExtra(ARG_ITEM)) {
            try {
                Result result = intent.getParcelableExtra(ARG_ITEM);
                bindData(result);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: ", e);
                finish();
            }
        } else {
            finish();
        }
    }

    private void bindData(Result result) {
        ActivityDetailsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_details);

        final DetailsMostPopularViewModel viewModel
                = ViewModelProviders.of(this).get(DetailsMostPopularViewModel.class);

        DbHelper dbHelper = new DbHelper(this);
        viewModel.setFilesDir(getFilesDir().getAbsolutePath());
        viewModel.setDbHelper(dbHelper);
        viewModel.setResult(result);

        binding.setModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getResultAddingToFavorites().observe(this, r ->
                showToast(getString(r != null
                        ? R.string.adding_to_favorites_successfully
                        : R.string.adding_to_favorites_error))
        );

        viewModel.getLoadData().observe(this, status -> {
            if (status != null && !status) {
                showToast(getString(R.string.favorite_load_data_error));
                finish();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}