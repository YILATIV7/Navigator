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

        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
    }

    public void goToPlaceId1(View view) {
        Intent intent = new Intent(NFCScanActivity.this, PlaceActivity.class);
        intent.putExtra("PLACE_ID", 1);
        startActivity(intent);
        finish();
    }

    public void goToPlaceId2(View view) {
        Intent intent = new Intent(NFCScanActivity.this, PlaceActivity.class);
        intent.putExtra("PLACE_ID", 2);
        startActivity(intent);
        finish();
    }
}