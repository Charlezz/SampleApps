package charlezz.myapplication;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        Log.e(TAG, "width:" + width);
        Log.e(TAG, "height:" + height);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        Log.e(TAG, "width2:" + width);
        Log.e(TAG, "height2:" + height);

        Log.e(TAG, "dm.densityDpi:" + dm.densityDpi);
        Log.e(TAG, "density:" + dm.density);
        Log.e(TAG, "scaledDensity:" + dm.scaledDensity);
        Log.e(TAG, "dm.xdpi" + dm.xdpi);
        Log.e(TAG, "dm.ydpi" + dm.ydpi);
    }
}
