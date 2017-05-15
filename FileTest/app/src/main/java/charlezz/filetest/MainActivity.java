package charlezz.filetest;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileTest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new TedPermission(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Permission denied")
                .check();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };

    void fileTest() {
        Log.e(TAG, "getDataDirectory:" + Environment.getDataDirectory().getAbsolutePath());
        Log.e(TAG, "getDownloadCacheDirectory:" + Environment.getDownloadCacheDirectory().getAbsolutePath());
        Log.e(TAG, "getRootDirectory:" + Environment.getRootDirectory().getAbsolutePath());
        Log.e(TAG, "getExternalStorageDirectory:" + Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.e(TAG, "getExternalStoragePublicDirectory:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        Log.e(TAG, "---------------------------------------------");
        Log.e(TAG, "getDataDirectory:" + writeFile(Environment.getDataDirectory()));
        Log.e(TAG, "getDownloadCacheDirectory:" + writeFile(Environment.getDownloadCacheDirectory()));
        Log.e(TAG, "getRootDirectory:" + writeFile(Environment.getRootDirectory()));
        Log.e(TAG, "getExternalStorageDirectory:" + writeFile(Environment.getExternalStorageDirectory()));
        Log.e(TAG, "getExternalStoragePublicDirectory:" + writeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
        Log.e(TAG, "---------------------------------------------");
        Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_PICTURES):" + getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + ":" + writeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        Log.e(TAG, "getFilesDir():" + getFilesDir().getAbsolutePath() + ":" + writeFile(getFilesDir()));


    }

    private boolean writeFile(File directory) {
        File file = new File(directory, "test.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            String text = "Hello World!!";
            fos.write(text.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
