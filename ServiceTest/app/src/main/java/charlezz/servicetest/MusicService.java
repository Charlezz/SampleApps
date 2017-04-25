package charlezz.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

public class MusicService extends Service {

    public static final String TAG = MusicService.class.getSimpleName();

    private IBinder mBinder = new MusicServiceBinder();

    public MusicService(String name) {
//        super(name);
//    }
    }

    MediaPlayer mp = null;


    public MusicService() {
//        super(MusicService.class.getSimpleName());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                switch (action) {
                    case "previous":
                        Log.e(TAG, "previous");
                        break;
                    case "play":
                        Log.e(TAG, "play");
                        break;
                    case "next":
                        Log.e(TAG, "next");
                        break;
                }
            }

        }
        return START_REDELIVER_INTENT;

    }

    private void showNotification() {
        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction("previous");
        PendingIntent pPreviousIntent = PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction("play");
        PendingIntent pPlayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction("next");
        PendingIntent pNextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("TutorialsFace Music Player")
                .setTicker("TutorialsFace Music Player")
                .setContentText("My song")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .addAction(android.R.drawable.ic_media_previous, "Previous", pPreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "Play", pPlayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", pNextIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Test"))
                .build();

        startForeground(101, notification);

    }

    public void start(String path) {
        stop();
        mp = MediaPlayer.create(this, Uri.parse("file://" + path));
        mp.start();
        showNotification();
    }

    public void stop() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        stopForeground(true);
    }

    public class MusicServiceBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }


}
