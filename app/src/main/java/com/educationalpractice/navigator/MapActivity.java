package com.educationalpractice.navigator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        WebView webView = findViewById(R.id.web_view);
        String mapFileName = getIntent().getStringExtra("FILE_NAME");

        webView.loadDataWithBaseURL("file:///android_asset/", "<img src='" + mapFileName + "' />", "text/html", "utf-8", null);
    }
}