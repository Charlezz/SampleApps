package charlezz.bluetoothtest.ble.central;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import charlezz.bluetoothtest.R;
import charlezz.bluetoothtest.ble.BaseBluetoothActivity;

public class CentralActivity extends BaseBluetoothActivity {

    public static final String TAG = CentralActivity.class.getSimpleName();

    private static final int MENU_DEFAULT_GROUP = 0;
    private static final int MENU_SCAN = 0;

    @BindView(R.id.listView)
    ListView listView;

    private Unbinder unbinder;
    private LeDeviceListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        unbinder = ButterKnife.bind(this);
        mAdapter = new LeDeviceListAdapter(this);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem scanItem = menu.add(MENU_DEFAULT_GROUP, MENU_SCAN, MENU_SCAN, "scan");
        scanItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SCAN:
                startScan();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @OnItemClick(R.id.listView)
    void onItemClicked(int position) {
        BluetoothDevice device = mAdapter.getItem(position);

        String name = device.getName();
        String address = device.getAddress();

        Intent intent = new Intent(CentralActivity.this, DeviceControlActivity.class);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, name);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, address);

        startActivity(intent);
    }

    public void startScan() {
        Log.e(TAG, "startScan");
        mAdapter.clear();
        mBluetoothAdapter.getBluetoothLeScanner().startScan(mScanCallback);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
            }
        }, 3000);
    }

    public void stopScan() {
        Log.e(TAG, "stopScan");
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            mAdapter.addDevice(device);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e(TAG, "onBatchScanResults");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "onScanFailed");
        }
    };
}
