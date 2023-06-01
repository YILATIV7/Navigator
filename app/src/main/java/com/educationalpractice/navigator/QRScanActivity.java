package com.educationalpractice.navigator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class QRScanActivity extends AppCompatActivity {

    private boolean isTorchOn = false;
    private Camera camera = null;

    private final int REQUEST_CODE_PERMISSIONS = 5555;
    private final String[] REQUIRED_PERMISSIONS = new String[] { "android.permission.CAMERA" };

    private final int SUSPENSION_TIME = 2000;
    PreviewView mPreviewView;
    public boolean isProcess;
    ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        mPreviewView = findViewById(R.id.camera_preview);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                this.finish();
            }
        }
    }

    public void QRCodeHandler(String qrCodeText) {
        Context context = this;

        int placeId = 0;
        if (qrCodeText.equals("https://t.me/Viktoriag16")) {
            placeId = 1; // room
        } else if (qrCodeText.equals("https://instagram.com/uks.vin?igshid=YTY2NzY3YTc=")) {
            placeId = 2; // near kitchen
        } else {
            runOnUiThread(() -> Toast.makeText(context, "UNDEFINED_QR_CODE", Toast.LENGTH_LONG).show());
        }

        if (placeId != 0) {
            Intent intent = new Intent(QRScanActivity.this, PlaceActivity.class);
            intent.putExtra("PLACE_ID", placeId);
            startActivity(intent);
            finish();
        }

        new Thread(() -> {
            try {
                Thread.sleep(SUSPENSION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isProcess = false;
        }).start();
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        imageAnalysis.setAnalyzer(Executors.newFixedThreadPool(1), new QRCodeDecoder(this));

        ImageCapture.Builder builder = new ImageCapture.Builder();

        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        Preview preview = new Preview.Builder().build();

        imageCapture = builder
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);
    }

    public void toggleTorch(View view) {
        CameraControl cameraControl = camera.getCameraControl();
        CameraInfo cameraInfo = camera.getCameraInfo();

        // Check if the camera supports flash
        if (cameraInfo.hasFlashUnit()) {
            FloatingActionButton fab = findViewById(R.id.fabFlashlight);
            isTorchOn = !isTorchOn;
            fab.setImageDrawable(AppCompatResources
                    .getDrawable(this, isTorchOn
                            ? R.drawable.baseline_flashlight_on_24
                            : R.drawable.baseline_flashlight_off_24));
            cameraControl.enableTorch(isTorchOn);
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException ignored) {
            }
        }, ContextCompat.getMainExecutor(this));
    }
}