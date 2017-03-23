package charlezz.ipcameratest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class IPCameraService extends Service {
    public IPCameraService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
