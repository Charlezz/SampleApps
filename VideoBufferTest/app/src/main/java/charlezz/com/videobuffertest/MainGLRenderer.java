package charlezz.com.videobuffertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tomcat2 on 2015-06-02.
 */
public class MainGLRenderer implements GLSurfaceView.Renderer {

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    protected FloatBuffer mUvBuffer;
    protected static float mUvs[];
    private final float[] mMtrxView = new float[16];

    private int mHandleBitmap;

    // 쉐이더 이미지
    public static final String vs_Image = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "  v_texCoord = a_texCoord;" + "}";
    public static final String fs_Image = "precision mediump float;" +
            "varying vec2 v_texCoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
            "}";
    float mSquareCoords[] = {
            -0.5f, 0.5f, 0.0f,  // top left
            -0.5f, -0.5f, 0.0f,  // bottom left
            0.5f, -0.5f, 0.0f,  // bottom right
            0.5f, 0.5f, 0.0f};
    // top right
    private short mDrawOrder[] = {0, 1, 2, 0, 2, 3};
    // order to draw vertices
    private int mProgram;
    private int mPositionHandle;


    private Tex mTex;
    private Context mContext;

    public MainGLRenderer(Context context) {
        mContext = context;
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
//        mTex = new Tex(this, bm);

//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
//        int bufferId = -1;
//        GLES20.glGenBuffers(bufferId, imageBuffer, 0);
//        GLES20.glBindBuffer(mHandleBitmap, bufferId);
//
//        GLES20.glUseProgram(mProgram);
//        Matrix.setIdentityM(mMtrxView, 0);
//        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
//        int texCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
//        GLES20.glEnableVertexAttribArray(texCoordLoc);
//        GLES20.glVertexAttribPointer(texCoordLoc, 2, GLES20.GL_FLOAT, false, 0, mUvBuffer);
//        int mtrxhandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
//        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mMtrxView, 0);
//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mHandleBitmap);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawOrder.length,
//                GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
//        GLES20.glDisableVertexAttribArray(mPositionHandle);
//        GLES20.glDisableVertexAttribArray(texCoordLoc);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), mContext.getResources().getIdentifier(
//                "drawable/b_spearman1", null, mContext.getPackageName()
//        ));
//        mTex = new Tex(this, image);


//        ByteBuffer bb = ByteBuffer.allocateDirect(mSquareCoords.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        mVertexBuffer = bb.asFloatBuffer();
//        mVertexBuffer.put(mSquareCoords);
//        mVertexBuffer.position(0);
//        ByteBuffer dlb = ByteBuffer.allocateDirect(mDrawOrder.length * 2);
//        dlb.order(ByteOrder.nativeOrder());
//        mDrawListBuffer = dlb.asShortBuffer();
//        mDrawListBuffer.put(mDrawOrder);
//        mDrawListBuffer.position(0);
//        mUvs = new float[]{
//                0.0f, 0.0f,
//                0.0f, 1.0f,
//                1.0f, 1.0f,
//                1.0f, 0.0f
//        };
//
//        ByteBuffer bbUvs = ByteBuffer.allocateDirect(mUvs.length * 4);
//        bbUvs.order(ByteOrder.nativeOrder());
//        mUvBuffer = bbUvs.asFloatBuffer();
//        mUvBuffer.put(mUvs);
//        mUvBuffer.position(0);
//        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs_Image);
//        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs_Image);
//        mProgram = GLES20.glCreateProgram();
//        GLES20.glAttachShader(mProgram, vertexShader);
//        GLES20.glAttachShader(mProgram, fragmentShader);
//        GLES20.glLinkProgram(mProgram);
//
//        int[] texturenames = new int[1];
//        GLES20.glGenTextures(1, texturenames, 0);
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        mHandleBitmap = texturenames[0];
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }


    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    int[] imageBuffer = new int[]{};

    public void setBuffer(byte[] byteArray) {
        IntBuffer intBuf = ByteBuffer.wrap(byteArray)
                .order(ByteOrder.BIG_ENDIAN)
                .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        imageBuffer = array;
    }

    Bitmap bm;

    public void setBitmap(Bitmap bm) {
        this.bm = bm;
    }

}
