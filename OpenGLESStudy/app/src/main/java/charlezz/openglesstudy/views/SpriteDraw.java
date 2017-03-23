package charlezz.openglesstudy.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import charlezz.openglesstudy.R;

/**
 * Created by Charles on 3/23/17.
 */

public class SpriteDraw extends View {

    public static final String TAG = SpriteDraw.class.getSimpleName();

    float scale;
    int bitmapWidth = 100;
    int bitmapHeight = 150;

    public SpriteDraw(Context context) {
        super(context);
        scale = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.spearmen);
        canvas.drawBitmap(bm,
                new Rect(0, 0, (int) (bitmapWidth * scale), (int) (bitmapHeight * scale)),
                new Rect(0, 0, (int) (bitmapWidth * scale), (int) (bitmapHeight * scale)),
                null);
        canvas.drawBitmap(bm,
                new Rect((int) (bitmapWidth * scale), 0, (int) (bitmapWidth * 2 * scale), (int) (bitmapHeight * scale)),
                new Rect(0, (int) (bitmapHeight * scale), (int) (bitmapWidth * scale), (int) (bitmapHeight * 2 * scale)),
                null);
        canvas.drawBitmap(bm,
                new Rect((int) (bitmapWidth * 2 * scale), 0, (int) (bitmapWidth * 3 * scale), (int) (bitmapHeight * scale)),
                new Rect(0, (int) (bitmapHeight * 2 * scale), (int) (bitmapWidth * scale), (int) (bitmapHeight * 3 * scale)),
                null);

        bm.recycle();


    }

}
