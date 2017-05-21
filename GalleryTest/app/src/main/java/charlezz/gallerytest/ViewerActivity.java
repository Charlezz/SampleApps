package charlezz.gallerytest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewerActivity extends AppCompatActivity {

    public static final String KEY_IMAGE = "key image";

    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ButterKnife.bind(this);

        String path = getIntent().getStringExtra(KEY_IMAGE);
        Bitmap bm = BitmapFactory.decodeFile(path);
        imageView.setImage(ImageSource.bitmap(bm));

    }

}
