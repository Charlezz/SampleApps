package charlezz.openglesstudy.views.basic_gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Charles on 3/23/17.
 */

public class MainGLSurfaceView extends GLSurfaceView {
    MainGLRenderer mRenderer;
    public MainGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new MainGLRenderer();
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);

    }
}
