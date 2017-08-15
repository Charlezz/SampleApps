package charlezz.com.videobuffertest;

import android.Manifest;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.glSurfaceView)
    GLSurfaceView glSurfaceView;
    MainGLRenderer renderer;
    MediaPlayer mp = new MediaPlayer();

    @BindView(R.id.imageView)
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new MainGLRenderer(this);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        new TedPermission(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        try {
                            mp.setDataSource("http://ia902205.us.archive.org/31/items/Unexpect2001/Unexpect2001_512kb.mp4");
//                            mp.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/exid.mp4");
                            mp.prepare();

                            ImageReader mImageReader = ImageReader.newInstance(mp.getVideoWidth(), mp.getVideoHeight(), ImageFormat.YUV_420_888, 2);
                            mImageReader.setOnImageAvailableListener(listener, new Handler());


                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Log.e(TAG, "onCompletion");
                                }
                            });
                            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                    Log.e(TAG, "OnErrorListener");
                                    return false;
                                }
                            });
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    Log.e(TAG, "prepared");
                                    mp.start();
                                }
                            });
                            mp.setSurface(mImageReader.getSurface());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "denied", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


    }

    @OnClick(R.id.start)
    public void OnStartClick() {
        Log.e(TAG, "OnStartClick");
        mp.start();
    }

    @OnClick(R.id.stop)
    public void OnStopClick() {
        Log.e(TAG, "OnStopClick");
        mp.pause();
    }

    ImageReader.OnImageAvailableListener listener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.e(TAG, "reader");
            Image image = reader.acquireNextImage();
        }
    };
}
