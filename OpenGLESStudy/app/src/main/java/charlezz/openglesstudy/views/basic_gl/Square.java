package charlezz.openglesstudy.views.basic_gl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Charles on 3/24/17.
 */

public class Square {

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private final String mVertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main(){" +
                    "gl_Position = vPosition;" +
                    "}";

}
