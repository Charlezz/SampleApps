package charlezz.openglpractice;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    boolean updateSurface;
    GLSurfaceView glSurfaceView;
    int textureId;
    SurfaceTexture surfaceTexture;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }


        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor afd = getAssets().openFd("sample.mp4");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "something wrong");
            e.printStackTrace();
        }

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        VideoRender renderer2 = new VideoRender();
        renderer2.setMediaPlayer(mediaPlayer);
        renderer2.setGLSurfaceView(glSurfaceView);
        glSurfaceView.setRenderer(renderer2);


        setContentView(glSurfaceView);

        Log.e(TAG, "onCreate2");

    }


}
