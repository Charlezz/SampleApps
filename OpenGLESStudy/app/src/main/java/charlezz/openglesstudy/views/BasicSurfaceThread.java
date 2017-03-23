package charlezz.openglesstudy.views;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Charles on 3/23/17.
 */

public class BasicSurfaceThread extends Thread {
    private SurfaceHolder surfaceholder;
    private SurfaceDraw mainView;
    private boolean running = false;

    public BasicSurfaceThread(SurfaceHolder surfaceHolder, SurfaceDraw mainView) {
        surfaceholder = surfaceHolder;
        this.mainView = mainView;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceholder;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public void run() {
        Log.d("mainThread", "run called:" + running);
        try {
            Canvas c;
            while (running) {
                c = null;
                try {
                    c = surfaceholder.lockCanvas(null);
                    synchronized (surfaceholder) {
                        try {
                            Thread.sleep(1);
                            mainView.onDraw(c);
                        } catch (Exception exTemp) {
                            Log.e("에러", exTemp.toString());
                        }
                    }
                } finally {
                    if (c != null)
                        surfaceholder.unlockCanvasAndPost(c);
                }
            }
        } catch (Exception exTot) {
            Log.e("에러", exTot.toString());
        }
    }
}
