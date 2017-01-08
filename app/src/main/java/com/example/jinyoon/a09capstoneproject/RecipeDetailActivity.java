package com.example.jinyoon.a09capstoneproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class RecipeDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webview = (WebView) findViewById(R.id.recipe_webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(getIntent().getExtras().getString(getString(R.string.publisher_url_key)));

    }

    @Override
    public void onStart() {
        super.onStart();
        //Get tracker.
        Tracker tracker = ((MyApplication) getApplication()).getTracker();

        //Set screen name
        tracker.setScreenName(LOG_TAG);
        //Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
