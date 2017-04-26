package charlezz.ndkbuildlibtwo;

/**
 * Created by Charles on 26/04/2017.
 */

public class NativeManager {
    static {
        System.loadLibrary("myndk2");
    }

    public native String hello();
}
