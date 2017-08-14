package charlezz.com.differentprocess;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

	public static final String TAG = MainActivity.class.getSimpleName();
	private MyService myService;

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e(TAG, "onCreate");

		intent = new Intent(MainActivity.this, MyService.class);
		startService(intent);

	}

	boolean mBound;

	ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			Log.e(TAG, "onServiceConnected");
			MyService.LocalBinder binder = (MyService.LocalBinder) iBinder;
			myService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			Log.e(TAG, "onServiceDisconnected");
			mBound = false;
		}
	};


	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		bindService(intent, connection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
		if (mBound) {
			unbindService(connection);
			mBound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
