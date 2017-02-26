package charlezz.bluetoothtest.ble.central;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;
import charlezz.bluetoothtest.R;
import charlezz.bluetoothtest.ble.peripheral.PeripheralActivity;


public class DeviceControlActivity extends AppCompatActivity {

    public static final String TAG = DeviceControlActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    @BindView(R.id.device_address)
    TextView deviceAddress;
    @BindView(R.id.gatt_services_list)
    ExpandableListView mGattServicesList;
    @BindView(R.id.connection_state)
    TextView mConnectionStateView;
    @BindView(R.id.data_value)
    TextView mDataField;


    private Unbinder unbinder;

    private String mDeviceName;
    private String mDeviceAddress;

    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothManager mBluetoothManager;

    private BluetoothDevice device;
    private BluetoothGatt mBluetoothGatt;

    private int mConnectionState = BluetoothGatt.STATE_DISCONNECTED;
    private SimpleExpandableListAdapter mAdapter;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);

        unbinder = ButterKnife.bind(this);

        mDeviceName = getIntent().getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);

        if (!TextUtils.isEmpty(mDeviceName)) {
            getSupportActionBar().setTitle(mDeviceName);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        device = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);

        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);


        mGattServicesList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                if (mGattCharacteristics != null) {
                    final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                    final int charaProp = characteristic.getProperties();
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGattCallback = null;
        unbinder.unbind();
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (newState == BluetoothGatt.STATE_CONNECTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectionState = BluetoothGatt.STATE_CONNECTED;
                        mConnectionStateView.setText("Connected");
                        deviceAddress.setText(gatt.getDevice().getAddress());
                        mBluetoothGatt.discoverServices();
                    }
                });
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectionState = BluetoothGatt.STATE_DISCONNECTED;
                        mConnectionStateView.setText("Disconnected");
                        deviceAddress.setText("");
                    }
                });
            }
        }


        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.e(TAG, "onServicesDiscovered");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
                    ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
                    List<BluetoothGattService> services = gatt.getServices();
                    for (BluetoothGattService gattService : services) {
                        Log.e(TAG, "service:" + gattService.getUuid());
                        HashMap<String, String> currentServiceData = new HashMap<>();
                        currentServiceData.put(LIST_UUID, gattService.getUuid().toString());
                        gattServiceData.add(currentServiceData);

                        ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
                        List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                        ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();

                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            Log.e(TAG, "characteristic:" + gattCharacteristic.getUuid());
                            charas.add(gattCharacteristic);
                            HashMap<String, String> currentCharaData = new HashMap<>();
                            currentCharaData.put(LIST_UUID, gattCharacteristic.getUuid().toString());
                            gattCharacteristicGroupData.add(currentCharaData);
                            gatt.setCharacteristicNotification(gattCharacteristic, true);
                        }
                        mGattCharacteristics.add(charas);
                        gattCharacteristicData.add(gattCharacteristicGroupData);
                    }

                    mAdapter = new SimpleExpandableListAdapter(
                            DeviceControlActivity.this,
                            gattServiceData,//group data
                            android.R.layout.simple_expandable_list_item_1, //list groupitem view
                            new String[]{LIST_UUID}, //hash key for data
                            new int[]{android.R.id.text1}, // display data on view
                            gattCharacteristicData,
                            android.R.layout.simple_expandable_list_item_2,
                            new String[]{LIST_UUID},
                            new int[]{android.R.id.text2}
                    );
                    mGattServicesList.setAdapter(mAdapter);
                }
            });


        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.e(TAG, "onCharacteristicChanged");

            if (PeripheralActivity.BATTERY_LEVEL_UUID.equals(characteristic.getUuid())) {
                final int batteryLevel = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DeviceControlActivity.this, "battery:" + batteryLevel, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    };

    @OnCheckedChanged(R.id.call)
    public void OnCallChanged(boolean state) {
        Log.e(TAG, "OnCallChanged");
//        for (ArrayList<BluetoothGattCharacteristic> serviceList : mGattCharacteristics) {
//            for (BluetoothGattCharacteristic characteristic : serviceList) {
//                if (characteristic.getUuid().equals(PeripheralActivity.UUID_ALERT_LEVEL)) {
//                    Log.e(TAG, "OnCallChanged1");
//                    if (state) {
//                        characteristic.setValue(1, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
//                    } else {
//                        characteristic.setValue(0, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
//                    }
//                    mBluetoothGatt.writeCharacteristic(characteristic);
////                    mBluetoothGatt.setCharacteristicNotification(characteristic, true);
//                    Log.e(TAG, "OnCallChanged2");
//                }
//            }
//        }
        BluetoothGattCharacteristic ch = mBluetoothGatt.getService(PeripheralActivity.UUID_IMMEDIATE_ALERT)
                .getCharacteristic(PeripheralActivity.UUID_ALERT_LEVEL);
        if (state) {
//            Log.e(TAG, "check1:" + );
            ch.setValue(1, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        } else {
//            Log.e(TAG, "check2:" + ch.setValue(0, BluetoothGattCharacteristic.FORMAT_UINT8, 0));
            ch.setValue(0, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        }

        mBluetoothGatt.writeCharacteristic(ch);
    }
}
