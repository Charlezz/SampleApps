package maxst.sampleapp;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glGetUniformLocation;


/**
 * Created by Charles on 18/05/2017.
 */

public class VideoGLRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    static {
//        System.loadLibrary("VideoPlayer");
        System.loadLibrary("VideoSample");
    }

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private float[] mModelMatrix_FBO = new float[16];
    private float[] mViewMatrix_FBO = new float[16];
    private float[] mProjectionMatrix_FBO = new float[16];
    private float[] mMVPMatrix_FBO = new float[16];


    int frameBuffer = 0;
    int mDestTextureId = 0;
    int mediaPlayerTexture = 0;

    SurfaceTexture mSurfaceTexture;
    public static final String TAG = VideoGLRenderer.class.getSimpleName();


    private static final int FLOAT_SIZE_BYTES = 4;

    private final String mVertexShader_OES =
            "uniform mat4 uMVPMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec2 aTextureCoord;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "  vTextureCoord = aTextureCoord;\n" +
                    "}";

    private final String mFragmentShader_OES =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";


    public static final String VERTEX_SHADER_QUAD = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 aPosition;" +
            "attribute vec2 aTextureCoord;" +
            "varying vec2 vTextureCoord;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * aPosition;" +
            "  vTextureCoord = aTextureCoord;" + "}";

    public static final String FRAGMENT_SHADER_QUAD = "precision mediump float;" +
            "varying vec2 vTextureCoord;" +
            "uniform sampler2D mDestTextureId;" +
            "void main() {" +
            "  gl_FragColor = texture2D( mDestTextureId, vTextureCoord );" +
            "}";


    private int mTextureHandle;

    private int mShaderId;
    private int mMVPMatrixHandle;
    private int mVertexPositionHandle;
    private int mTextureCoordHandle;

    private int mShaderId_OES;
    private int muMVPMatrixHandle_FBO;
    private int mVertexPositionHandle_OES;
    private int maTextureHandle_FBO;

    private static int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    MediaPlayer mediaPlayer;

    int mVertexBuffer = 0;
    int mUVBuffer = 0;

    int mVertexBufferOES;
    int uvBufferFBO;


    int mTextureHandleOES;

    /**
     * @param mediaPlayer
     */

    float quadCoord[] = new float[]{
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
    };

    float uvCoord[] = new float[]{
            0f, 0f,
            1f, 0f,
            0f, 1f,
            0f, 1f,
            1f, 0f,
            1f, 1f
    };
    Context context;

    private int videoWidth, videoHeight;

    public VideoGLRenderer(Context context, MediaPlayer mediaPlayer, int videoWidth, int videoHeight) {
        this.mediaPlayer = mediaPlayer;
        this.context = context;
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //매트릭스 초기화
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);

        //뷰매트릭스 초기화
        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 1.0f;

        float lookX = 0.0f;
        float lookY = 0.0f;
        float lookZ = 0.0f;

        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);


        //기본적인 그릴 쉐이더
        mShaderId = createProgram(VERTEX_SHADER_QUAD, FRAGMENT_SHADER_QUAD);

        checkGlError("mShaderId");

        //이 쉐이더를 쓸거야
        GLES20.glUseProgram(mShaderId);
        checkGlError("use mShaderId");

        //최종 렌더링 될 텍스쳐
        mTextureHandle = glGetUniformLocation(mShaderId, "mDestTextureId");

        //aPosition = 버텍스 포지션 배열
        mVertexPositionHandle = GLES20.glGetAttribLocation(mShaderId, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (mVertexPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        //aTextureCoord = 버텍스의 UV 배열
        mTextureCoordHandle = GLES20.glGetAttribLocation(mShaderId, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (mTextureCoordHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        //uMVPMatrix 알쥐
        mMVPMatrixHandle = glGetUniformLocation(mShaderId, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (mMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }


        //사각형 그리는 부분, 최종적인 텍스쳐가 그려질 부분 영역 잡아주기
        int[] vertexBuffers = new int[1];
        GLES20.glGenBuffers(1, vertexBuffers, 0);
        mVertexBuffer = vertexBuffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, quadCoord.length * 4, FloatBuffer.wrap(quadCoord), GLES20.GL_STATIC_DRAW);


        //텍스쳐가 적용될 UV
        int[] uvBuffers = new int[1];
        GLES20.glGenBuffers(1, uvBuffers, 0);
        mUVBuffer = uvBuffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mUVBuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, uvCoord.length * 4, FloatBuffer.wrap(uvCoord), GLES20.GL_STATIC_DRAW);

        //최종적으로 렌더링 될 텍스처
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        mDestTextureId = textures[0];

        //텍스쳐 바인드
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mDestTextureId);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, videoWidth, videoHeight, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);


        /**
         * FBO*****************************************************************************************************************************
         * */
        checkGlError("");

        Matrix.setIdentityM(mModelMatrix_FBO, 0);
        Matrix.setIdentityM(mViewMatrix_FBO, 0);
        Matrix.setIdentityM(mMVPMatrix_FBO, 0);
        Matrix.setIdentityM(mProjectionMatrix_FBO, 0);

        Matrix.setLookAtM(mViewMatrix_FBO, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);


        //FBO에 동영상 그릴 쉐이더 만들기
        mShaderId_OES = createProgram(mVertexShader_OES, mFragmentShader_OES);
        Log.e(TAG, "mShaderId_OES:" + mShaderId_OES);

        GLES20.glUseProgram(mShaderId_OES);


        mTextureHandleOES = glGetUniformLocation(mShaderId_OES, "sTexture");

        //aPosition = 버텍스 포지션 배열
        mVertexPositionHandle_OES = GLES20.glGetAttribLocation(mShaderId_OES, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (mVertexPositionHandle_OES == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        //aTextureCoord = 버텍스의 UV 배열
        maTextureHandle_FBO = GLES20.glGetAttribLocation(mShaderId_OES, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle_FBO == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        //uMVPMatrix 알쥐
        muMVPMatrixHandle_FBO = glGetUniformLocation(mShaderId_OES, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle_FBO == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }
        checkGlError("");
        //사각형 그리는 부분, 최종적인 텍스쳐가 그려질 부분 영역 잡아주기
        int[] vertexBuffersFBO = new int[1];
        GLES20.glGenBuffers(1, vertexBuffersFBO, 0);
        mVertexBufferOES = vertexBuffersFBO[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBufferOES);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, quadCoord.length * 4, FloatBuffer.wrap(quadCoord), GLES20.GL_STATIC_DRAW);


        //텍스쳐가 적용될 UV
        int[] uvBuffersFBO = new int[1];
        GLES20.glGenBuffers(1, uvBuffersFBO, 0);
        uvBufferFBO = uvBuffersFBO[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, uvBufferFBO);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, uvCoord.length * 4, FloatBuffer.wrap(uvCoord), GLES20.GL_STATIC_DRAW);


        //비디오입힐 OES 텍스쳐 만들기
        int[] mediaPlayerTextures = new int[1];
        GLES20.glGenTextures(0, mediaPlayerTextures, 0);
        mediaPlayerTexture = mediaPlayerTextures[0];

        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mediaPlayerTexture);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //서피스 텍스쳐 만들기
        mSurfaceTexture = new SurfaceTexture(mediaPlayerTexture);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                updateSurface = true;
            }
        });
        //서피스 텍스쳐
        Surface mSurface = new Surface(mSurfaceTexture);
        mediaPlayer.setSurface(mSurface);
        mSurface.release();

        mediaPlayer.start();

        //프레임버퍼 만들기
        int[] frameBuffers = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        frameBuffer = frameBuffers[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mDestTextureId, 0);

        //GL초기화
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);


        synchronized (this) {
            updateSurface = false;
        }
    }

    private boolean updateSurface = false;

    private int windowHeight, windowWidth;

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "windowWidth:" + width);
        Log.e(TAG, "windowHeight:" + height);
        updateRendering(width, height);
        windowWidth = width;
        windowHeight = height;

        GLES20.glViewport(0, 0, width, height);

        final float left = -width / 2f;
        final float right = width / 2f;
        final float bottom = -height / 2f;
        final float top = height / 2f;
        final float near = 1.0f;
        final float far = 2000f;

        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);


        float leftFBO = -videoWidth / 2f;
        float rightFBO = videoWidth / 2f;
        float bottomFBO = -videoHeight / 2f;
        float topFBO = videoHeight / 2f;
        float nearFBO = 1.0f;
        float farFBO = 2000f;
        Matrix.orthoM(mProjectionMatrix_FBO, 0,
                leftFBO,
                rightFBO,
                bottomFBO,
                topFBO,
                nearFBO,
                farFBO
        );


//        final float ratio = (float) width / height;
//        final float left = -ratio;
//        final float right = ratio;
//        final float bottom = -1.0f;
//        final float top = 1.0f;
//        final float near = 0.1f;
//        final float far = 2000.0f;
//
//        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    synchronized public void onFrameAvailable(SurfaceTexture surface) {
        updateSurface = true;
    }

    int scaleFactor = 200;


    float gradient = 0.0f;

    boolean reverse;

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (updateSurface) {
                mSurfaceTexture.updateTexImage();
                updateSurface = false;
            }
        }
        //프레임버퍼에 렌더링 하기
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);
        checkGlError("glBindFramebuffer:" + frameBuffer);


        GLES20.glViewport(0, 0, videoWidth, videoHeight);

        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mShaderId_OES);

        Matrix.setIdentityM(mModelMatrix_FBO, 0);
        Matrix.scaleM(mModelMatrix_FBO, 0, videoWidth, videoHeight, 1);

        Matrix.multiplyMM(mMVPMatrix_FBO, 0, mViewMatrix_FBO, 0, mModelMatrix_FBO, 0);
        Matrix.multiplyMM(mMVPMatrix_FBO, 0, mProjectionMatrix_FBO, 0, mMVPMatrix_FBO, 0);

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle_FBO, 1, false, FloatBuffer.wrap(mMVPMatrix_FBO));
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mediaPlayerTexture);


        GLES20.glUniform1i(mTextureHandleOES, 0);

        GLES20.glEnableVertexAttribArray(mVertexPositionHandle_OES);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBufferOES);
        GLES20.glVertexAttribPointer(mVertexPositionHandle_OES, 3, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glEnableVertexAttribArray(maTextureHandle_FBO);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, uvBufferFBO);
        GLES20.glVertexAttribPointer(maTextureHandle_FBO, 2, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glDisableVertexAttribArray(mVertexPositionHandle_OES);
        GLES20.glDisableVertexAttribArray(maTextureHandle_FBO);


        //clear
        if (!reverse) {
            gradient += 0.01;
            if (gradient >= 1.0f) {
                reverse = true;
            }
        } else {
            gradient -= 0.01;
            if (gradient <= 0.0f) {
                reverse = false;
            }
        }

        //실제 그려지는 영역
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, windowWidth, windowHeight);

        GLES20.glClearColor(gradient, gradient, gradient, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mShaderId);

        Matrix.setIdentityM(mModelMatrix, 0);
//        float ratio = (float) videoWidth / (float) windowWidth;
//        Log.e(TAG, "ratio:" + ratio);
//        ratio = 1;
        Matrix.scaleM(mModelMatrix, 0, windowWidth, -windowHeight, 1);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);


        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, FloatBuffer.wrap(mMVPMatrix));
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mDestTextureId);

        GLES20.glUniform1i(mTextureHandle, 0);

        GLES20.glEnableVertexAttribArray(mVertexPositionHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBuffer);
        GLES20.glVertexAttribPointer(mVertexPositionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);


        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mUVBuffer);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glDisableVertexAttribArray(mVertexPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);

        GLES20.glFinish();


    }


    public native void initRendering();

    private native void updateRendering(int width, int height);

    private native void drawScene();


    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }
}
