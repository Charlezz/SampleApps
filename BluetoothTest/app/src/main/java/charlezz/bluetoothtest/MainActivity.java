package charlezz.bluetoothtest;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import charlezz.bluetoothtest.ble.central.CentralActivity;
import charlezz.bluetoothtest.ble.peripheral.PeripheralActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.ble)
    Button bleBtn;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbinder.unbind();
    }


    @OnClick(R.id.ble)
    public void OnBleButtonClick() {
        Log.e(TAG, "OnBleButtonClick");
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "not supported", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(MainActivity.this, BleActivity.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsGranted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsDenied");
        requestPermission();
    }

    private void requestPermission() {
        Log.e(TAG, "requestPermission");
        if (EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION})) {
            Log.e(TAG, "hasPermissions");
        } else {
            Log.e(TAG, "requestPermissions");
            EasyPermissions.requestPermissions(this, "Bluettoth Permission", 0, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION});
        }
    }

    @OnClick(R.id.peripheral)
    public void startPeripheralActivity() {
        startActivity(new Intent(MainActivity.this, PeripheralActivity.class));
    }

    @OnClick(R.id.central)
    public void startCentralActivity() {
        startActivity(new Intent(MainActivity.this, CentralActivity.class));
    }

    @OnClick(R.id.setDiscoverable)
    public void setDiscoverable() {
//            Class<BluetoothAdapter> classBluetoothAdapter = (Class<BluetoothAdapter>) Class.forName(BluetoothAdapter.class.getCanonicalName());


        try {
            Method m_getScanMode = BluetoothAdapter.class.getMethod("getScanMode");
            Method m_setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class);

            Log.e(TAG, "getScaneMode1:" + m_getScanMode.invoke(BluetoothAdapter.getDefaultAdapter()));
            m_setScanMode.invoke(BluetoothAdapter.getDefaultAdapter(), SCAN_MODE_CONNECTABLE_DISCOVERABLE);
            Log.e(TAG, "getScaneMode:" + m_getScanMode.invoke(BluetoothAdapter.getDefaultAdapter()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public static final int SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23;
    public static final int SCAN_MODE_NONE = 20;

    @OnClick(R.id.setScanModeNone)
    public void setScanModeNone() {
        try {
            Method m_getScanMode = BluetoothAdapter.class.getMethod("getScanMode");
            Method m_setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class);

            Log.e(TAG, "getScaneMode1:" + m_getScanMode.invoke(BluetoothAdapter.getDefaultAdapter()));
            m_setScanMode.invoke(BluetoothAdapter.getDefaultAdapter(), SCAN_MODE_NONE);
            Log.e(TAG, "getScaneMode:" + m_getScanMode.invoke(BluetoothAdapter.getDefaultAdapter()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
