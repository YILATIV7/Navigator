package com.educationalpractice.navigator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NFCScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_scan);

        findViewById(R.id.nfc_scan_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NFCScanActivity.this, PlaceActivity.class));
            }
        });

        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
    }
}