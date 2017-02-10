package com.charles.camera2buffertest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Charles on 2/1/17.
 */

public class CameraController1 extends CameraController implements SurfaceHolder.Callback, Camera.PreviewCallback {

    public static final String TAG = CameraController1.class.getSimpleName();

    private static final int PREFERRED_WIDTH = 640;
    private static final int PREFERRED_HEIGHT = 480;

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private Context context;
    private SurfaceHolder mHolder;


    boolean keepAlive;

    @Override
    public void start(int cameraId, int width, int height) {
        context = ContextUtil.getInstance().getActivity();

        mSurfaceView = new SurfaceView(context);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);


        mCamera = Camera.open(cameraId);
        Camera.Parameters params = mCamera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();

        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        List<Camera.Size> cameraPreviewList = params.getSupportedPreviewSizes();
        Camera.Size cameraSize = getOptimalPreviewSize(cameraPreviewList);

        params.setPreviewSize(cameraSize.width, cameraSize.height);
        params.setPreviewFormat(ImageFormat.NV21);

        PixelFormat p = new PixelFormat();
        PixelFormat.getPixelFormatInfo(params.getPreviewFormat(), p);
        int bufSize = (cameraSize.width * cameraSize.height * p.bitsPerPixel) / 8;

        byte[] buffer = new byte[bufSize];
        mCamera.addCallbackBuffer(buffer);
        buffer = new byte[bufSize];
        mCamera.addCallbackBuffer(buffer);
        buffer = new byte[bufSize];
        mCamera.addCallbackBuffer(buffer);
        buffer = new byte[bufSize];
        mCamera.addCallbackBuffer(buffer);
        mCamera.setPreviewCallbackWithBuffer(this);
        mCamera.setParameters(params);

        keepAlive = true;

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateFrame(byte[] data, int length, int width, int height, int formant) {
        Log.e(TAG, "update frame");
    }

    @Override
    public void stop() {
        if (mCamera == null) {
            return;
        }

        keepAlive = false;

        mCamera.stopPreview();
        mCamera.setPreviewCallbackWithBuffer(null);
        mCamera.release();
        mCamera = null;
    }

    @Override
    public SurfaceView getSurfaceView() {
        return mSurfaceView;
    }


    /**
     * Check if this device has a mCamera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a mCamera
            return true;
        } else {
            // no mCamera on this device
            return false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mSurfaceView.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mHolder = null;
    }

    Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes) {
        double minRegion = Double.MAX_VALUE;
        Camera.Size optimalSize = null;
        for (Camera.Size size : sizes) {
            if (size.width <= PREFERRED_WIDTH && size.height <= PREFERRED_HEIGHT) {
                if (Math.abs(size.width * size.height - PREFERRED_WIDTH * PREFERRED_HEIGHT) <= minRegion) {
                    minRegion = Math.abs(size.width * size.height - PREFERRED_WIDTH * PREFERRED_HEIGHT);
                    optimalSize = size;

                    Log.i(TAG, "Optimal Preview width  : " + optimalSize.width + " height : " + optimalSize.height);
                }
            }
        }

        return optimalSize;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (keepAlive) {
//            MaxstARJNI.setNewCameraFrame(data, data.length, cameraSize.width, cameraSize.height, 2);
//            surfaceManager.requestRender();
            camera.addCallbackBuffer(bytes);
            updateFrame(bytes, bytes.length, PREFERRED_WIDTH, PREFERRED_HEIGHT, 0);

        }
    }
}
