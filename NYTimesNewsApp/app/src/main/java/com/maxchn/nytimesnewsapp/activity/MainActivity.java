package com.maxchn.nytimesnewsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.maxchn.nytimesnewsapp.R;
import com.maxchn.nytimesnewsapp.adapter.TabAdapter;
import com.maxchn.nytimesnewsapp.fragment.FavoritesFragment;
import com.maxchn.nytimesnewsapp.fragment.MostPopularFragment;
import com.maxchn.nytimesnewsapp.model.MostPopularType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

        adapter.addFragment(MostPopularFragment.newInstance(MostPopularType.EMAILED), getString(R.string.most_emailed));
        adapter.addFragment(MostPopularFragment.newInstance(MostPopularType.SHARED), getString(R.string.most_shared));
        adapter.addFragment(MostPopularFragment.newInstance(MostPopularType.VIEWED), getString(R.string.most_viewed));
        adapter.addFragment(FavoritesFragment.newInstance(), getString(R.string.favorites));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}