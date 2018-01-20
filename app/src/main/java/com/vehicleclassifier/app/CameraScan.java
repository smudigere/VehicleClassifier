package com.vehicleclassifier.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraScan extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 200;

    @SuppressWarnings("deprecation")
    private Camera mCamera;
    private Handler autoFocusHandler;

    private FrameLayout preview;

    private boolean previewing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        preview = (FrameLayout) findViewById(R.id.camera_preview);

        if (checkCameraHardware(getApplicationContext()))   {

            if (Build.VERSION.SDK_INT <= 22) {
                openCamera();
            } else {    //Request permission to access Camera.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION
                );
            }

        } else
            Toast.makeText(this, "No Camera Available", Toast.LENGTH_SHORT).show();
    }



    private void openCamera()   {

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        CameraPreview mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        preview.addView(mPreview);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)    {

            case CAMERA_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openCamera();


                } else {

                    Toast.makeText(CameraScan.this, "Camera Access Denied", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    @SuppressWarnings("deprecation")
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            //nada
        }
        return c;
    }


    /**
     * It is important that the camera preview is stopped,
     * in every occassion, the user exits or someother function is added to the main UI.
     */
    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }


    public void onPause() {
        super.onPause();
        releaseCamera();
    }


    // Mimic continuous auto-focusing
    @SuppressWarnings("deprecation")
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 3000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };


    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {

        }
    };
}
