package ru.cocovella.WeatherApp.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Closeable;

import ru.cocovella.WeatherApp.R;

import static ru.cocovella.WeatherApp.Model.Keys.CITY_KEY;
import static ru.cocovella.WeatherApp.Model.Keys.LOG_TAG;
import static ru.cocovella.WeatherApp.Model.Keys.PERMISSION_REQUEST_CODE;


public class LocationLoader implements LocationListener, Closeable {
    private static LocationLoader instance;
    private static final long MIN_TIME = 5000;
    private static final float MIN_DISTANCE = 0;
    private Settings settings;
    private Fragment fragment;
    private Activity activity;
    private LocationManager locationManager;
    private TextInputEditText city;


    public static LocationLoader getInstance() {
        if (instance == null) {
            instance = new LocationLoader();
        }
        return instance;
    }


    public void load(Fragment fragment) {
        this.fragment = fragment;
        settings = Settings.getInstance();
        initView();

        Context context = fragment.getContext();
        if (context != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions();
            } else {
                Log.d(LOG_TAG, "checkPermissions = TRUE, context != null");
            }
        } else { Log.e(LOG_TAG, "context == null");}


        String provider = getProvider();
        if(provider == null) return;
        Log.d(LOG_TAG, "provider = " + provider);

        Log.d(LOG_TAG, "getLastKnownLocation");
        Location location = locationManager.getLastKnownLocation(provider);
        showCoordinates(location);

        locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
    }

    private void initView() {
        Activity activity = fragment.getActivity();
        if (activity != null) {
            Log.d(LOG_TAG, "fragment.getActivity()=true");
            this.activity = activity;
            city = activity.findViewById(R.id.cityInput);
            locationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        } else {
            Log.e(LOG_TAG, "fragment.getActivity()=false, locationManager == null");
        }
    }

    private String getProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        return locationManager.getBestProvider(criteria, true);
    }

    private void showCoordinates(Location location) {
        double lat = -33.87605;
        double lon = 151.20936;

        if (location != null) {
            Log.d(LOG_TAG, "location != null");
            lat = location.getLatitude();
            lon = location.getLongitude();
        } else {
            Log.e(LOG_TAG, "location == null");
        }

        settings.setLatitude(Double.toString(lat));
        settings.setLongitude(Double.toString(lon));
        String coordinates = CITY_KEY + lat + " : " + lon;

        if (city.getVisibility() == View.VISIBLE) {
            city.setText(coordinates);
            this.close();
        }

        Log.d(LOG_TAG, coordinates);

    }

    private void requestPermissions() {
        if (!fragment.shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
            Log.d(LOG_TAG, "requestPermissions() requested");
        } else {
            Log.e(LOG_TAG, "requestPermissions() failed");
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e(LOG_TAG, "onLocationChanged");
        showCoordinates(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(LOG_TAG, "Status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(LOG_TAG, "onProviderEnabled: GPS: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) +
                ", NETWORK: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(LOG_TAG, "onProviderDisabled: GPS: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) +
                ", NETWORK: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    @Override
    public void close() {
        if (locationManager != null)
        locationManager.removeUpdates(this);
    }
}
