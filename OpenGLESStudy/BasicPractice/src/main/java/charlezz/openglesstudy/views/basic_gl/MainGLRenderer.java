package charlezz.openglesstudy.views.basic_gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Charles on 3/23/17.
 */

public class MainGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    public static final String TAG = MainGLRenderer.class.getSimpleName();

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        mTriangle.draw();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.e(TAG, "onSurfaceCreated");
        mTriangle = new Triangle(this);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.e(TAG, "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

}
