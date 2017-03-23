package charlezz.openglesstudy.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import charlezz.openglesstudy.R;

/**
 * Created by Charles on 3/23/17.
 */

public class BasicDraw extends View {

    public static final String TAG = BasicDraw.class.getSimpleName();

    Context context;
    int width, height;

    public BasicDraw(Context context) {
        super(context);
        this.context = context;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");

        //draw line
        Paint linePaint = new Paint();
        linePaint.setColor(Color.RED);
        canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), linePaint);

        //draw Circle
        Paint circlePaint = new Paint();
        circlePaint.setShader(
                new RadialGradient(
                        canvas.getWidth() / 2,
                        canvas.getHeight() / 4,
                        canvas.getHeight() / 10,
                        Color.RED,
                        Color.GREEN,
                        Shader.TileMode.MIRROR
                )
        );
        circlePaint.setColor(Color.RED);
        canvas.drawCircle(width / 2, height / 2, 100, circlePaint);

        //draw text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24);
        canvas.drawText("Hello World!!", 100, 100, textPaint);

        //draw image
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.spearman);
        canvas.drawBitmap(bm, 200, 200, null);
        //draw image scale2X
        Bitmap bitmapScale = Bitmap.createScaledBitmap(bm, 150, 150, false);
        canvas.drawBitmap(bitmapScale, canvas.getWidth() / 2 - bitmapScale.getScaledWidth(canvas) / 2, canvas.getHeight() * 2 / 4 - bitmapScale.getScaledHeight(canvas) / 2, null);

        //dra image with matrix
        Matrix matrixDirX = new Matrix();
        matrixDirX.preScale(-1, 1);

        Bitmap matrixBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrixDirX, false);
        canvas.drawBitmap(matrixBm, 0, canvas.getHeight() - matrixBm.getHeight(), null);

        bm.recycle();
        bitmapScale.recycle();

    }
}
