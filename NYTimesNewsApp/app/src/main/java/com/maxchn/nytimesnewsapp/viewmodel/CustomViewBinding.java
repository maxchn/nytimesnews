package com.maxchn.nytimesnewsapp.viewmodel;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;

public class CustomViewBinding {
    @BindingAdapter("setAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @BindingAdapter("setWebViewContent")
    public static void bindWebViewContent(WebView webView, String webViewContent) {
        webView.loadData(webViewContent, "text/html", "utf-8");
    }
}