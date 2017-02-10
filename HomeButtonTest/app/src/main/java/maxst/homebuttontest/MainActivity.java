package maxst.homebuttontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                Log.e(TAG,"onHomePressed");
            }

            @Override
            public void onHomeLongPressed() {
                Log.e(TAG,"onHomeLongPressed");
            }
        });
        mHomeWatcher.startWatch();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.e(TAG, "onUserLeaveHint");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.e(TAG, "keyCode:" + keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
