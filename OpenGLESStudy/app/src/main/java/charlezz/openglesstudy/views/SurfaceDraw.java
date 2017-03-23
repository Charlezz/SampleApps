package charlezz.openglesstudy.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Charles on 3/23/17.
 */

public class SurfaceDraw extends SurfaceView implements SurfaceHolder.Callback {

    Context context;
    BasicSurfaceThread mainThread;

    public SurfaceDraw(Context context) {
        super(context);
        getHolder().addCallback(this);
        mainThread = new BasicSurfaceThread(getHolder(), this);
        setFocusable(true);
        this.context = context;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        try {
            if (mainThread.getState() == Thread.State.TERMINATED) {
                mainThread = new BasicSurfaceThread(getHolder(), this);
                mainThread.setRunning(true);
                setFocusable(true);
                mainThread.start();
            } else {
                mainThread.start();
            }
        } catch (Exception ex) {
            Log.d("MainView", "ex:" + ex.toString());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("MainView", "surfaceDestoryed called");
        boolean retry = true;
        mainThread.setRunning(false);
        while (retry) {
            try {
                mainThread.join();
                retry = false;
            } catch (Exception e) {
                Log.i("MainView", "surfaceDestoryed ex" + e.toString());
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 배경을 그림.
        canvas.drawColor(Color.BLUE);
        Paint backPaint = new Paint();
        backPaint.setColor(Color.rgb(255, 0, 0));
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0, 0, width, height, backPaint);
        backPaint.setColor(Color.rgb(0, 255, 0));
        canvas.drawRect(
                (int) (width * 1 / 10), (int) (height * 1 / 10),
                (int) (width * 9 / 10), (int) (height * 9 / 10), backPaint);
        backPaint.setColor(Color.rgb(0, 0, 255));
        canvas.drawRect(
                (int) (width * 2 / 10), (int) (height * 2 / 10),
                (int) (width * 8 / 10), (int) (height * 8 / 10), backPaint);
        backPaint.setColor(Color.rgb(0, 0, 0));
        canvas.drawRect(
                (int) (width * 3 / 10), (int) (height * 3 / 10),
                (int) (width * 7 / 10), (int) (height * 7 / 10), backPaint);
        backPaint.setColor(Color.rgb(255, 255, 255));
        backPaint.setTextSize(32);
        canvas.drawText("C",
                (int) (width / 2), (int) (height / 2), backPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
