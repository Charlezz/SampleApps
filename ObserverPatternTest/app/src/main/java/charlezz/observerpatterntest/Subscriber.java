package charlezz.observerpatterntest;

import android.util.Log;

/**
 * Created by Charles on 2/20/17.
 */

public class Subscriber implements OnCustomClickListener {
    public static final String TAG = Subscriber.class.getSimpleName();
    private String name;

    public Subscriber(String name) {
        this.name = name;
    }


    @Override
    public void onClick() {
        Log.e(TAG, name);
    }
}
