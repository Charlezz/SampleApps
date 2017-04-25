package charlezz.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Charles on 24/04/2017.
 */

public class MusicActivity extends AppCompatActivity {

    public static final String KEY_MUSICITEM = "Music Item";
    public static final String TAG = MusicActivity.class.getSimpleName();
    @BindView(R.id.listView)
    ListView listView;
    MusicAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        mAdapter = new MusicAdapter();
        listView.setAdapter(mAdapter);
        Log.e(TAG, "" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        getList(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public void getList(String source) {
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        try {
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile()) {
                    int pos = file.getName().lastIndexOf(".");
                    String ext = file.getName().substring(pos + 1);
                    if (ext.equalsIgnoreCase("mp3")) {
                        mAdapter.add(new MusicItem(file.getName(), file.getAbsolutePath()));
                        Log.e(TAG, file.getName());
                    }
//
                } else if (file.isDirectory()) {
                    getList(file.getCanonicalPath().toString());
                }
            }

        } catch (IOException e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MusicActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(connection);
            mBound = false;
        }
    }

    private int lastPosition = -1;


    @OnItemClick(R.id.listView)
    public void OnItemClick(int position) {
//        MusicItem item = mAdapter.getItem(position);
//        Intent intent = new Intent(MusicActivity.this, MusicService.class);
//        intent.putExtra(KEY_MUSICITEM, item);
//        startService(intent);
        if (mBound) {
            mMusicService.start(mAdapter.getItem(position).path);
        }


//        MenuItem item = menu.add(0, position, 0, "" + position);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        lastPosition = position;
        if (lastPosition != -1) {
            menu.removeItem(lastPosition);
        }
//        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, lastPosition, 0, "" + lastPosition);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean mBound;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            mMusicService = ((MusicService.MusicServiceBinder) service).getMusicService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    MusicService mMusicService = null;


    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
}
