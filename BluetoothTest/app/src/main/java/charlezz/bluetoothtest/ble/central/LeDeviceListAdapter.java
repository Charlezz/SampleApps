package charlezz.bluetoothtest.ble.central;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import charlezz.bluetoothtest.R;

/**
 * Created by Charles on 2/22/17.
 */

public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();
    private Context context;

    public LeDeviceListAdapter(Context context) {
        this.context = context;
    }

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            notifyDataSetChanged();
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_device, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.setDevice(mLeDevices.get(i));

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.device_name)
        TextView deviceName;
        @BindView(R.id.device_address)
        TextView deviceAddress;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setDevice(BluetoothDevice device) {
            if (device.getName() != null && device.getName().length() > 0) {
                deviceName.setText(device.getName());
            } else {
                deviceName.setText("unknown");
            }
            deviceAddress.setText(device.getAddress());
        }
    }
}
