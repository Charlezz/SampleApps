package com.charles.camera2buffertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class CameraActivity extends AppCompatActivity {

    FrameLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        view = (FrameLayout) findViewById(R.id.activity_camera);
        MaxstAR.getInstance().init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MaxstAR.getInstance().startCamera(0, 1280, 720);
        view.addView(MaxstAR.getInstance().getCameraController().getSurfaceView());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MaxstAR.getInstance().stopCamera();
        view.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MaxstAR.getInstance().deinit();
    }
}
