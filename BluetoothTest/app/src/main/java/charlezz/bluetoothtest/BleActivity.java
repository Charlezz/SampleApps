package charlezz.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
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
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BleActivity extends AppCompatActivity {

    public static final UUID CUSTOM_SERVICE = UUID.fromString("00000329-0000-1000-8000-00805F9B34FB");
    private static final UUID CUSTOM_CHARACTERISTIC = UUID.fromString("00000330-0000-1000-8000-00805F9B34FB");
//    private static final UUID CHARACTERISTIC_USER_DESCRIPTION_UUID = UUID.fromString("00000331-0000-1000-8000-00805F9B34FB");
//    private static final UUID CLIENT_CHARACTERISTIC_CONFIGURATION_UUID = UUID.fromString("00000332-0000-1000-8000-00805F9B34FB");

    private static final UUID CHARACTERISTIC_USER_DESCRIPTION_UUID = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIGURATION_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final String TAG = BleActivity.class.getSimpleName();
    private static int REQUEST_ENABLE_BT = 0;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattServer mServer;
    private BluetoothGattCharacteristic customCharacteristic;


    private ArrayList<BluetoothDevice> connectedDevices = new ArrayList<>();

    @BindView(R.id.seekBar)
    SeekBar mSeekbar;

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
//                .setServiceUuid(new ParcelUuid(CUSTOM_SERVICE))
                .build();

        filter.add(scanFilter);

        ScanSettings mScanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();
        mBluetoothAdapter.getBluetoothLeScanner().startScan(filter, mScanSettings, mScanCallBack);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallBack);
            }
        }, 5000);
    }

    @OnClick(R.id.stopLe)
    public void stopScan() {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallBack);
    }

    @OnClick(R.id.peripheral)
    public void startAdvertising(Button btn) {
        Log.e(TAG, "startAdvertising");

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build();

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .addServiceUuid(new ParcelUuid(CUSTOM_SERVICE))
                .addServiceData(new ParcelUuid(CUSTOM_SERVICE), buildTempPacket())
                .build();


        mBluetoothAdapter.getBluetoothLeAdvertiser().startAdvertising(settings, data, advertisingCallback);


    }

    private byte[] buildTempPacket() {
        int value;
        try {
            value = Integer.parseInt("0");
        } catch (NumberFormatException e) {
            value = 0;
        }

        return new byte[]{(byte) value, 0x00};
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
            Log.e(TAG, result.getDevice().getName() + " rssi=" + result.getRssi());
            if (isConnected) {
                mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallBack);
            } else {
                BluetoothGatt gatt = result.getDevice().connectGatt(BleActivity.this, false, mGattCallback);
                isConnected = true;
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


    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_DISCONNECTING = 3;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e(TAG, gatt.getDevice().getName() + "|" + status + "|" + newState);
            gatt.discoverServices();
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.e(TAG, "onServicesDiscovered");

            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                Log.e(TAG, "service:" + service.getUuid());


                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    Log.e(TAG, "characteristic:" + characteristic.getUuid());

                    gatt.setCharacteristicNotification(characteristic, true);

                    gatt.readCharacteristic(characteristic);
                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                    for (BluetoothGattDescriptor descriptor : descriptors) {
                        gatt.readDescriptor(descriptor);
                        Log.e(TAG, descriptor.toString());
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicRead");
            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
            for (BluetoothGattDescriptor descriptor : descriptors) {
                gatt.readDescriptor(descriptor);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
            Log.e(TAG, "onCharacteristicChanged:" + characteristic.getValue());

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorWrite");
        }
    };


    @OnClick(R.id.openGattServer)
    public void openGattServer() {

        mServer = mBluetoothManager.openGattServer(this, new BluetoothGattServerCallback() {

                    @Override
                    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                        super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
                        Log.e(TAG, "onCharacteristicReadRequest");
                    }

                    @Override
                    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                        Log.e(TAG, "onCharacteristicWriteRequest");
                    }

                    @Override
                    public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
                        super.onDescriptorReadRequest(device, requestId, offset, descriptor);
                        Log.e(TAG, "onDescriptorReadRequest");
                    }

                    @Override
                    public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                        super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
                        Log.e(TAG, "onDescriptorWriteRequest");
                    }

                    @Override
                    public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
                        super.onExecuteWrite(device, requestId, execute);
                        Log.e(TAG, "onExecuteWrite");
                    }

                    @Override
                    public void onMtuChanged(BluetoothDevice device, int mtu) {
                        super.onMtuChanged(device, mtu);
                        Log.e(TAG, "onMtuChanged");
                    }

                    @Override
                    public void onNotificationSent(BluetoothDevice device, int status) {
                        super.onNotificationSent(device, status);
                        Log.e(TAG, "onNotificationSent");
                    }

                    @Override
                    public void onServiceAdded(int status, BluetoothGattService service) {
                        super.onServiceAdded(status, service);
                        Log.e(TAG, "onServiceAdded");
                    }

                    @Override
                    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                        super.onConnectionStateChange(device, status, newState);
                        switch (newState) {
                            case BluetoothGattServer.STATE_CONNECTING:
                                Log.e(TAG, "STATE_CONNECTING");
                                break;
                            case BluetoothGattServer.STATE_CONNECTED:
                                Log.e(TAG, "STATE_CONNECTED:" + device.getName() + "|" + device.getAddress());
                                connectedDevices.add(device);
                                break;
                            case BluetoothGattServer.STATE_DISCONNECTING:
                                Log.e(TAG, "STATE_DISCONNECTING");
                                break;
                            case BluetoothGattServer.STATE_DISCONNECTED:
                                Log.e(TAG, "STATE_DISCONNECTED:" + device.getName() + "|" + device.getAddress());
                                isConnected = false;
                                connectedDevices.remove(device);
                                break;
                        }

                    }
                }
        );

        BluetoothGattService service = new BluetoothGattService(CUSTOM_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        customCharacteristic = new BluetoothGattCharacteristic(CUSTOM_CHARACTERISTIC,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE);


        customCharacteristic.addDescriptor(getClientCharacteristicConfigurationDescriptor());
        customCharacteristic.addDescriptor(getCharacteristicUserDescriptionDescriptor("default value"));


        service.addCharacteristic(customCharacteristic);
        mServer.addService(service);
    }

    @OnClick(R.id.closeGattServer)
    public void closeGattServer() {
        if (mServer != null) {
            mServer.clearServices();
            connectedDevices.clear();
            mServer.close();
            mServer = null;
            customCharacteristic = null;
        }
    }

    public static BluetoothGattDescriptor getClientCharacteristicConfigurationDescriptor() {
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(
                CLIENT_CHARACTERISTIC_CONFIGURATION_UUID,
                (BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE));
        descriptor.setValue(new byte[]{0, 0});
        return descriptor;
    }

    public static BluetoothGattDescriptor getCharacteristicUserDescriptionDescriptor(String defaultValue) {
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(
                CHARACTERISTIC_USER_DESCRIPTION_UUID, (BluetoothGattDescriptor.PERMISSION_READ));
        try {
            descriptor.setValue(defaultValue.getBytes("UTF-8"));
        } finally {
            return descriptor;
        }
    }


    @OnClick(R.id.notify)
    public void notifyDataChanged() {
        Log.e(TAG, "notifyDataChanged");
        int value = mSeekbar.getProgress();
        customCharacteristic.setValue(value, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        customCharacteristic.getDescriptor(CHARACTERISTIC_USER_DESCRIPTION_UUID).setValue(new byte[]{0, 0});
        for (BluetoothDevice device : connectedDevices) {
            Log.e(TAG, "notifyCharacteristicChanged:" + device.getName());
            boolean result = mServer.notifyCharacteristicChanged(device, customCharacteristic, true);
            Log.e(TAG, "result:" + result);
        }
    }

}
