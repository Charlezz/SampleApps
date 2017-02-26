package charlezz.gpstest;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = MainActivity.class.getSimpleName();

    private LocationManager locationManager;
    private static final int RC_LOCATION = 0;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void requireLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Require location permission", RC_LOCATION, perms);
        }
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

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

//            location.getLatitude();
//            location.getLongitude();
            Log.e(TAG, location.toString() + "|" + location.getProvider());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled");
        }

    };

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
        finish();
    }


    @OnClick(R.id.request_on)
    public void requstLocationUpdateOn() {
        Log.e(TAG, "requstLocationUpdateOn");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @OnClick(R.id.request_off)
    public void requstLocationUpdateOff() {
        Log.e(TAG, "requstLocationUpdateOff");
        locationManager.removeUpdates(locationListener);
    }

    @OnClick(R.id.btn_permission)
    public void requirePermission() {
        Log.e(TAG, "requirePermission");
        requireLocationPermission();
    }

    @OnClick(R.id.lastKnownLocation)
    public void getLastKnownLocation() {
        Log.e(TAG, "LAST gps_provider:" + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString());
        Log.e(TAG, "LAST network_provider:" + locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).toString());
        Log.e(TAG, "LAST passive_provider:" + locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).toString());
    }


    @OnClick(R.id.single_network_provider)
    public void getLocationWithNetworkProvider() {
        Log.e(TAG, "network provider enable:" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
    }

    @OnClick(R.id.single_passive_provider)
    public void getLocationWithPassiveProvider() {
        locationManager.requestSingleUpdate(LocationManager.PASSIVE_PROVIDER, locationListener, getMainLooper());
    }

    @OnClick(R.id.single_gps_provider)
    public void getLocationWithGPS() {
        Log.e(TAG, "gps provider enable:" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
    }

    @OnClick(R.id.single_any_provider)
    public void getLocationWithAnyProvider() {
//        Log.e(TAG, "gps provider enable:" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
//        Log.e(TAG, "network provider enable:" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//        locationManager.requestSingleUpdate(criteria, listener, null);
        getLocationWithGPS();
        getLocationWithNetworkProvider();
    }
}
