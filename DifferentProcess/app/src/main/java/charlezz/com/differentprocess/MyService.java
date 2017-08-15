package charlezz.com.differentprocess;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 21/07/2017.
 */

public class MyService extends Service {
	public static final String TAG = MyService.class.getSimpleName();


	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		return START_STICKY;

	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Log.e(TAG, "onTaskRemoved");
		Intent intent = new Intent(getApplicationContext(), MyService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
		super.onTaskRemoved(rootIntent);

	}

	class LocalBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}

	LocalBinder binder = new LocalBinder();

}
