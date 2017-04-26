package charlezz.ndkbuildlib;

/**
 * Created by Charles on 26/04/2017.
 */

public class NativeManager {
    static {
        System.loadLibrary("myndk");
    }

    public native String hello();
}
