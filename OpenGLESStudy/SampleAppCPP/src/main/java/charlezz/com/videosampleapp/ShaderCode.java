package charlezz.com.videosampleapp;


import static charlezz.com.videosampleapp.ShaderCode.Variable.MVP_MATRIX;
import static charlezz.com.videosampleapp.ShaderCode.Variable.TEXTURE;
import static charlezz.com.videosampleapp.ShaderCode.Variable.VERTEX_POSITION;
import static charlezz.com.videosampleapp.ShaderCode.Variable.VERTEX_TEXTURE_COORD;

/**
 * Created by Charles on 05/06/2017.
 */

public class ShaderCode {

    public static final String VERTEX_SHADER_QUAD = "" +
            "uniform mat4 " + MVP_MATRIX + ";" +
            "attribute vec4 " + VERTEX_POSITION + ";" +
            "attribute vec2 " + VERTEX_TEXTURE_COORD + ";" +
            "varying vec2 textureCoord;" +
            "void main() {" +
            "  gl_Position = " + MVP_MATRIX + " * " + VERTEX_POSITION + ";" +
            "  textureCoord = " + VERTEX_TEXTURE_COORD + ";" +
            "}";

    public static final String FRAGMENT_SHADER_QUAD = "" +
            "precision mediump float;" +
            "varying vec2 textureCoord;" +
            "uniform sampler2D " + TEXTURE + ";" +
            "void main() {" +
            "  gl_FragColor = texture2D( " + TEXTURE + ", textureCoord );" +
            "}";

    public interface Variable {
        String MVP_MATRIX = "mvpMatrix";
        String VERTEX_POSITION = "vertexPosition";
        String VERTEX_TEXTURE_COORD = "vertexTexCoord";
        String TEXTURE = "mTexture";
    }
}
