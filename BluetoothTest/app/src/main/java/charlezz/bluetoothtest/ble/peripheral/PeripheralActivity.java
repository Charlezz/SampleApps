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
import butterknife.Unbinder;
import charlezz.bluetoothtest.R;
import charlezz.bluetoothtest.ble.BaseBluetoothActivity;


public class PeripheralActivity extends BaseBluetoothActivity {

    public static final String TAG = PeripheralActivity.class.getSimpleName();

//    public static final UUID UUID_BATTERY_SERVICE = UUID.fromString("00000329-0000-1000-8000-00805F9B34FB");
    public static final UUID UUID_BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB");
//    public static final UUID CUSTOM_SERVICE = UUID.fromString("00000329-0000-1000-8000-00805F9B34FB");

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        });

        BluetoothGattService service = new BluetoothGattService(UUID_BATTERY_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        batteryCharacteristic = new BluetoothGattCharacteristic(
                UUID_BATTERY_SERVICE,
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ
        );
        service.addCharacteristic(batteryCharacteristic);
        mServer.addService(service);

        AdvertiseSettings mAdvertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        AdvertiseData mAdvertiseData = new AdvertiseData.Builder()
//                .addServiceUuid(new ParcelUuid(UUID_BATTERY_SERVICE))
//                .addServiceData(new ParcelUuid(UUID_BATTERY_SERVICE), new byte[]{0x00, 0x00})
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


}
