package charlezz.cmakebuildlib;

/**
 * Created by Charles on 26/04/2017.
 */

public class NativeManager {
    static {
        System.loadLibrary("cmake-lib");
    }

    public native String hello();
}
