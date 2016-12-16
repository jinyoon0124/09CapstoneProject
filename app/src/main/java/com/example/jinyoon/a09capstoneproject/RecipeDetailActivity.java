package com.example.jinyoon.a09capstoneproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RecipeDetailActivity extends AppCompatActivity {

    private final String PUBLISHER_URL_KEY = "publisher_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webview = (WebView) findViewById(R.id.recipe_webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(getIntent().getExtras().getString(PUBLISHER_URL_KEY));

    }

}
