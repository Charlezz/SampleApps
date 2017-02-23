package charlezz.bluetoothtest.ble.peripheral;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import charlezz.bluetoothtest.R;
import charlezz.bluetoothtest.ble.BaseBluetoothActivity;


public class PeripheralActivity extends BaseBluetoothActivity {

    public static final String TAG = PeripheralActivity.class.getSimpleName();

    public static final UUID UUID_BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB");
    public static final UUID BATTERY_LEVEL_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_IMMEDIATE_ALERT = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_ALERT_LEVEL = UUID.fromString("00002A06-0000-1000-8000-00805f9b34fb");

    public static final int DISCONNECTED = 0;
    public static final int INCOMING = 1;

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.connection_state)
    ImageView connectionState;

    @BindView(R.id.rssi)
    TextView rssiView;

    @BindView(R.id.call_state)
    ImageView callState;

    @BindView(R.id.advertising)
    Switch advertising;

    private Unbinder unbinder;


    private ArrayList<BluetoothDevice> connectedDevice = new ArrayList<>();

    private BluetoothGattCharacteristic batteryCharacteristic;
    private BluetoothGattCharacteristic alertStatusCharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAdvertising();
        unbinder.unbind();
    }

    @OnCheckedChanged(R.id.advertising)
    public void OnItemSelected(boolean check) {
        if (check) {
            startAdvertising();
        } else {
            stopAdvertising();
        }
    }

    private BluetoothGattServer mServer;

    private void startAdvertising() {

        mServer = mBluetoothManager.openGattServer(PeripheralActivity.this, new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    if (!connectedDevice.contains(device)) {
                        connectedDevice.add(device);
                    }
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    connectedDevice.remove(device);
                }
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                Log.e(TAG, "onCharacteristicWriteRequest:"+value[0]);

                int call_state = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);

                switch (call_state) {
                    case DISCONNECTED:
                        Toast.makeText(PeripheralActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                        break;
                    case INCOMING:
                        Toast.makeText(PeripheralActivity.this, "Incoming", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNotificationSent(BluetoothDevice device, int status) {
                super.onNotificationSent(device, status);
                Log.e(TAG, "onNotificationSent");
            }

            @Override
            public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
                super.onExecuteWrite(device, requestId, execute);
                Log.e(TAG, "onExecuteWrite");
            }
        });


        BluetoothGattService batteryService = new BluetoothGattService(UUID_BATTERY_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        batteryCharacteristic = new BluetoothGattCharacteristic(
                BATTERY_LEVEL_UUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY | BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE
        );

        batteryService.addCharacteristic(batteryCharacteristic);
        mServer.addService(batteryService);

        BluetoothGattService immediateService = new BluetoothGattService(UUID_IMMEDIATE_ALERT, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        alertStatusCharacteristic = new BluetoothGattCharacteristic(
                UUID_ALERT_LEVEL,
                BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
                BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ
        );

        immediateService.addCharacteristic(alertStatusCharacteristic);
        mServer.addService(immediateService);


        AdvertiseSettings mAdvertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        AdvertiseData mAdvertiseData = new AdvertiseData.Builder()
//                .addServiceUuid(new ParcelUuid(UUID_BATTERY_SERVICE))
//                .addServiceData(new ParcelUuid(UUID_BATTERY_SERVICE), new byte[]{0x00, 0x02})
                .setIncludeDeviceName(true)
                .build();

        mBluetoothAdapter.getBluetoothLeAdvertiser().startAdvertising(mAdvertiseSettings, mAdvertiseData, mAdvertiseCallback);
    }

    private void stopAdvertising() {
        mBluetoothAdapter.getBluetoothLeAdvertiser().stopAdvertising(mAdvertiseCallback);
    }

    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Toast.makeText(PeripheralActivity.this, "advertising started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Toast.makeText(PeripheralActivity.this, "advertising failed:" + errorCode, Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.notify)
    public void notifyToClient() {
        int progress = seekBar.getProgress();
        batteryCharacteristic.setValue(progress, BluetoothGattCharacteristic.FORMAT_UINT8, 0);

        for (BluetoothDevice device : connectedDevice) {
            mServer.notifyCharacteristicChanged(device, batteryCharacteristic, true);
        }

    }

}
