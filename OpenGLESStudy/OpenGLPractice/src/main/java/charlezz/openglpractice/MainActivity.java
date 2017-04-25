package charlezz.openglpractice;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;

public class MainActivity extends Activity {


    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    protected FloatBuffer mUvBuffer;
    protected static float mUvs[];
    private final float[] mMtrxView = new float[16];

    private int mHandleBitmap;
    public static final String TAG = MainActivity.class.getSimpleName();

    private final String vs_Image =
            "uniform mat4 uMVPMatrix;\n" +
                    "uniform mat4 uSTMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec4 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n" +
                    "}\n";

    private final String fs_Image =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";
    float mSquareCoords[] = {
            -1f, 1f, 0.0f,  // top left
            -1f, -1f, 0.0f,  // bottom left
            1f, -1f, 0.0f,  // bottom right
            1f, 1f, 0.0f}; //top right
    // top right
    private short mDrawOrder[] = {0, 1, 2, 0, 2, 3};
    // order to draw vertices
    private int mProgram;
    private int mPositionHandle;


    boolean updateSurface;
    Object lock = new Object();
    GLSurfaceView glSurfaceView;
    GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer() {
//        Tex mTex;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            textureId = initTexture();
            ByteBuffer bb = ByteBuffer.allocateDirect(mSquareCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            mVertexBuffer = bb.asFloatBuffer();
            mVertexBuffer.put(mSquareCoords);
            mVertexBuffer.position(0);

            ByteBuffer dlb = ByteBuffer.allocateDirect(mDrawOrder.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            mDrawListBuffer = dlb.asShortBuffer();
            mDrawListBuffer.put(mDrawOrder);
            mDrawListBuffer.position(0);

            mUvs = new float[]{
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f
            };
            ByteBuffer bbUvs = ByteBuffer.allocateDirect(mUvs.length * 4);
            bbUvs.order(ByteOrder.nativeOrder());
            mUvBuffer = bbUvs.asFloatBuffer();
            mUvBuffer.put(mUvs);
            mUvBuffer.position(0);

            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs_Image);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs_Image);

            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexShader);
            checkGlError("glAttachShader:" + mProgram);
            GLES20.glAttachShader(mProgram, fragmentShader);
            checkGlError("glAttachShader:" + mProgram);
            GLES20.glLinkProgram(mProgram);
            checkGlError("glLinkProgram:" + mProgram);
            GLES20.glUseProgram(mProgram);
            checkGlError("glUseProgram:" + mProgram);
//            mTex = new Tex(mProgram);
//            mTex.setBitmap(textureId, 320, 600);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {

            synchronized (this) {
                if (updateSurface) {
                    surfaceTexture.updateTexImage();
                    updateSurface = false;
                }
            }

            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glUseProgram(mProgram);
            checkGlError("glUseProgram:" + mProgram);
            Matrix.setIdentityM(mMtrxView, 0);


            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
            checkGlError("glVertexAttribPointer maPosition");

            int texCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
            GLES20.glEnableVertexAttribArray(texCoordLoc);
            checkGlError("glEnableVertexAttribArray maPositionHandle");
            GLES20.glVertexAttribPointer(texCoordLoc, 2, GLES20.GL_FLOAT, false, 0, mUvBuffer);
            checkGlError("glVertexAttribPointer maTextureHandle");

            int mtrxhandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mMtrxView, 0);
            surfaceTexture.updateTexImage();
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawOrder.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
            checkGlError("glDrawArrays");
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(texCoordLoc);


            synchronized (this) {
                updateSurface = false;
            }

        }

        public int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }
    };

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


//        textureId = initTexture();
        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                updateSurface = true;
                glSurfaceView.requestRender();
            }
        });
        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor afd = getAssets().openFd("sample.mp4");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sample.mp4");
//            FileInputStream fis = new FileInputStream(file);
//
//            mediaPlayer.setDataSource(fis.getFD());
//            fis.close();

//            mediaPlayer.setSurface(new Surface(surfaceTexture));
//            mediaPlayer.prepare();
//            mediaPlayer.start();
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
//        glSurfaceView.setRenderer(renderer);
        VideoRender renderer2 = new VideoRender(this);
        renderer2.setMediaPlayer(mediaPlayer);
        renderer2.setGLSurfaceView(glSurfaceView);
        glSurfaceView.setRenderer(renderer2);
//        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


        setContentView(glSurfaceView);

        Log.e(TAG, "onCreate2");

    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    public int initTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textures[0]);
        checkGlError("glBindTexture mTextureID");
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        return textures[0];
    }


}
