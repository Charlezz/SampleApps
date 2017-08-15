package charlezz.com.videosampleapp;

import android.Manifest;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("SampleApp");
    }

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String SAMPLE_VIDEO_PATH = "http://ia902205.us.archive.org/31/items/Unexpect2001/Unexpect2001_512kb.mp4";

    GLSurfaceView mGlSurfaceView;

    int videoWidth, videoHeight;

    MediaPlayer mediaPlayer;
    SurfaceTexture mSurfaceTexture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        new TedPermission(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        setup();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


    }

    public void setup() {
        Log.e(TAG, "setup");
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(SAMPLE_VIDEO_PATH);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoWidth = mp.getVideoWidth();
                    videoHeight = mp.getVideoHeight();
                    mGlSurfaceView = new GLSurfaceView(MainActivity.this);
                    mGlSurfaceView.setEGLContextClientVersion(2);
                    mGlSurfaceView.setRenderer(mRenderer);
                    mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

                    setContentView(mGlSurfaceView);
                    Log.e(TAG, "setup1");
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    GLSurfaceView.Renderer mRenderer = new GLSurfaceView.Renderer() {

        int mSurfaceWidth, mSurfaceHeight;
        int mDestTextureId;
        int mMediaTextureId;
        boolean isFrameAvalable;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.e(TAG, "onSurfaceCreated");
            mDestTextureId = init(videoWidth, videoHeight);
            mMediaTextureId = initFBO();
            mSurfaceTexture = new SurfaceTexture(mMediaTextureId);
            Surface mSurface = new Surface(mSurfaceTexture);
            mediaPlayer.setSurface(mSurface);
            mSurface.release();
            mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//                    Log.i(TAG,"onFrameAvailable");
                    isFrameAvalable = true;
                }
            });


        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.e(TAG, "onSurfaceChanged");
            mSurfaceWidth = width;
            mSurfaceHeight = height;
            initSurface(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
//            Log.i(TAG,"onDrawFrame");
            if (isFrameAvalable) {
                mSurfaceTexture.updateTexImage();
                isFrameAvalable = false;
            }
            draw(mSurfaceWidth, mSurfaceHeight);


        }

    };


    private native int init(int videoWidth, int videoHeight);

    private native int initFBO();

    private native int initSurface(int width, int height);

    private native void draw(int surfaceWidth, int surfaceHeight);
}
