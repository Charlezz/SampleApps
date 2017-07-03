package maxst.sampleapp;

/**
 * Created by Charles on 18/05/2017.
 */

public class VideoLibraryInterface {

    static {
//        System.loadLibrary("VideoPlayer");
        System.loadLibrary("VideoSample");
    }

    private static VideoLibraryInterface ourInstance = null;

    public static VideoLibraryInterface getInstance() {
        if (ourInstance == null) {
            ourInstance = new VideoLibraryInterface();
        }
        return ourInstance;
    }

    private VideoLibraryInterface() {
    }

    public native boolean open(int videoId, String path);

    public native void setTexture(int videoId, int textureId);

    public native int getWidth(int movieId);

    public native int getHeight(int movieId);

    public native void start(int videoId);

    public native void update(int videoId);

    public native void pause(int videoId);

    public native void stop(int videoId);

    public native void destroyAll();


}
