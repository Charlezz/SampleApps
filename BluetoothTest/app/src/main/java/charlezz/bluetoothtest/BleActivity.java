package charlezz.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BleActivity extends AppCompatActivity {

    public static final String SERVICE_UUID = "0000b81d-0000-1000-8000-00805f9b34fb";
    public static final String TAG = BleActivity.class.getSimpleName();
    private static int REQUEST_ENABLE_BT = 0;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        initBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        unbinder.unbind();
    }

    private void initBluetooth() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
//            initBluetooth();
        } else {
            finish();
        }
    }


    @OnClick(R.id.scanLe)
    public void scanLeDevice() {
        Log.e(TAG, "scanLeDevice");
        List<ScanFilter> filter = new ArrayList<>();
        ScanFilter scanFilter = new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(UUID.fromString(SERVICE_UUID)))
                .build();

        filter.add(scanFilter);

        ScanSettings mScanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        mBluetoothAdapter.getBluetoothLeScanner().startScan(filter, mScanSettings, mScanCallBack);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                mBluetoothAdapter.getBluetoothLeScanner().startScan(mScanCallBack);
//            }
//        });

    }

    @OnClick(R.id.stopLe)
    public void stopScan() {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallBack);
    }

    @OnClick(R.id.peripheral)
    public void startAdvertising(Button btn) {
        Log.e(TAG, "startAdvertising");
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                .setTimeout(0)
                .build();

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString(SERVICE_UUID));

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(pUuid)
                .build();
        mBluetoothAdapter.getBluetoothLeAdvertiser().startAdvertising(settings, data, advertisingCallback);

    }

    @OnClick(R.id.stopPeripheral)
    public void stopAdvertising() {
        Log.e(TAG, "stopAdvertising");
        mBluetoothAdapter.getBluetoothLeAdvertiser().stopAdvertising(advertisingCallback);
    }

    private AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.e(TAG, "Advertising onStartFailure: " + errorCode);
            super.onStartFailure(errorCode);
        }
    };

    private boolean isConnected;

    private ScanCallback mScanCallBack = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.e(TAG, result.getDevice().getName() + "|" + result.getRssi() + "|");
            if (!isConnected) {
                isConnected = true;
                result.getDevice().connectGatt(BleActivity.this, false, mGattCallback);
            }

        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "onScanFailed:" + errorCode);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e(TAG, "onBatchScanResults");
        }
    };
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e(TAG, "newState:" + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isConnected = false;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> list = gatt.getServices();
            gatt.setCharacteristicNotification()
        }
    };
}
