package com.charles.camera2buffertest;

import android.app.Activity;

/**
 * Created by Charles on 2/1/17.
 */
public class ContextUtil {

    public static String TAG = ContextUtil.class.getSimpleName();

    private static ContextUtil ourInstance = new ContextUtil();

    public static ContextUtil getInstance() {
        return ourInstance;
    }

    private ContextUtil() {
    }

    private Activity context;

    public Activity getActivity() {
        return context;
    }

    public void setActivity(Activity context) {
        this.context = context;
    }
}
