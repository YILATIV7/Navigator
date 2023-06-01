package com.educationalpractice.navigator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Intent intent = getIntent();
        int placeId = intent.getIntExtra("PLACE_ID", 0);

        ImageView imageView = findViewById(R.id.current_place_view);

        if (placeId == 1) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.room_mini));
        } else if (placeId == 2) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.kitchen_near_mini));
        } else {
            Toast.makeText(this, "UNKNOWN_PLACE_ID", Toast.LENGTH_LONG).show();
            finish();
        }

        findViewById(R.id.show_on_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeId == 1) {
                    Intent intent = new Intent(PlaceActivity.this, MapActivity.class);
                    intent.putExtra("FILE_NAME", "room.png");
                    startActivity(intent);
                } else if (placeId == 2) {
                    Intent intent = new Intent(PlaceActivity.this, MapActivity.class);
                    intent.putExtra("FILE_NAME", "kitchen_near.png");
                    startActivity(intent);
                }
            }
        });

        Context context = this;
        findViewById(R.id.show_on_ar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeId == 0) return;

                String packageName = "";
                if (placeId == 1) packageName = "com.educationalpractice.navigator.ar.id3";
                else if (placeId == 2) packageName = "com.educationalpractice.navigator.ar.id4";

                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    String pkgName = packageName;
                    runOnUiThread(() -> Toast.makeText(context, "PACKAGE_NOT_FOUND:\n" + pkgName, Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}