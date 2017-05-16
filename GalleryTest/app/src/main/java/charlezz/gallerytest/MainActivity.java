package charlezz.gallerytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final float IMAGE_STANDARD_WIDTH = 100f;

    @BindView(R.id.gridView)
    GridView gridView;

    GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int space = 5;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int columnCount = (int) (dm.widthPixels / dm.density / IMAGE_STANDARD_WIDTH);
        Log.e(TAG, "dm.densityDpi:" + dm.densityDpi);
        Log.e(TAG, "dm.widthPixels:" + dm.widthPixels);
        Log.e(TAG, "dm.density:" + dm.density);

        gridView.setNumColumns(columnCount);
        gridView.setVerticalSpacing(space);
        gridView.setHorizontalSpacing(space);
        gridView.setPadding(space, space, space, space);

        mGalleryAdapter = new GalleryAdapter((dm.widthPixels - (space * (columnCount + 1))) / columnCount);
        gridView.setAdapter(mGalleryAdapter);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
}
