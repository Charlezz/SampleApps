package com.charles.camera2buffertest;

import android.app.Activity;
import android.os.Build;

/**
 * Created by Charles on 2/1/17.
 */
public class MaxstAR {

    public static String TAG = MaxstAR.class.getSimpleName();

    private static MaxstAR ourInstance = new MaxstAR();

    public static MaxstAR getInstance() {
        return ourInstance;
    }


    private CameraController cameraController;

    private MaxstAR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cameraController = new CameraController2();
        } else {
            cameraController = new CameraController1();
        }
    }

    public void init(Activity activity) {
        ContextUtil.getInstance().setActivity(activity);
    }

    public void deinit() {
        ContextUtil.getInstance().setActivity(null);
    }

    public void startCamera(int cameraId, int width, int height) {
        cameraController.start(cameraId, width, height);
    }

    public void stopCamera() {
        cameraController.stop();
    }

    public void updateFrame(byte[] data, int length, int width, int height, int format) {
        cameraController.updateFrame(data, length, width, height, format);
    }

    public CameraController getCameraController() {
        return cameraController;
    }

}
