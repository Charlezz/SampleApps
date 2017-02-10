package com.charles.camera2buffertest;

import android.view.SurfaceView;

/**
 * Created by Charles on 2/1/17.
 */

public abstract class CameraController {


    public abstract void start(int cameraId, int width, int height);

    public abstract void updateFrame(byte[] data, int length, int width, int height, int format);

    public abstract void stop();

    public abstract SurfaceView getSurfaceView();

}
