package com.educationalpractice.navigator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        findViewById(R.id.scanNFCButton).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NFCScanActivity.class)));

        findViewById(R.id.scanQRButton).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, QRScanActivity.class)));

        findViewById(R.id.viewMapButton).setOnClickListener(b ->
                startActivity(new Intent(MainActivity.this, MapActivity.class)));
    }
}