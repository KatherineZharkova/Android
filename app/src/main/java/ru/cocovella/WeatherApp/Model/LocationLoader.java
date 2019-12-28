package ru.cocovella.WeatherApp.Model;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.google.android.material.textfield.TextInputEditText;

import java.io.Closeable;

import ru.cocovella.WeatherApp.R;

import static ru.cocovella.WeatherApp.Model.Keys.CITY_KEY;
import static ru.cocovella.WeatherApp.Model.Keys.LOG_TAG;
import static ru.cocovella.WeatherApp.Model.Keys.PERMISSION_REQUEST_CODE;


public class LocationLoader implements Closeable {
    private Activity activity;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String latitude;
    private String longitude;
    private String coordinates;
    private String provider;
    private boolean isUpdated;
    private TextInputEditText city;


    public LocationLoader(Activity activity) {
        this.activity = activity;
        isUpdated = false;
        city = activity.findViewById(R.id.cityInput);
        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                    return;
        }

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        provider = locationManager.getBestProvider(criteria, true);     // Получим наиболее подходящий провайдер геолокации по критериям
        if (provider != null) {


            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (!isUpdated) {
                        Log.e(LOG_TAG, "onLocationChanged");
                        setCoordinates(location);
                        isUpdated = true;
                    }
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
            };

            locationManager.requestLocationUpdates(provider, 10000, 100, locationListener);

            if (!isUpdated) {
                Log.d(LOG_TAG, "getLastKnownLocation");
                Location location = locationManager.getLastKnownLocation(provider);
                setCoordinates(location);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setCoordinates(Location location) {
        Log.e(LOG_TAG, "setCoordinates");
        if (location != null) {
            Log.d(LOG_TAG, "location != null");
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            latitude = Double.toString(lat);
            longitude = Double.toString(lon);
            coordinates = CITY_KEY + (int) lat + " : " + (int) lon;
        } else {
            latitude = "55.7522";
            longitude = "37.6155";
            coordinates = CITY_KEY + "55" + " : " + "37";
        }

        if (city.getVisibility() == View.VISIBLE) { city.setText(coordinates); }
        Log.e(LOG_TAG, coordinates);
    }

    private void requestPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public void close() {
        locationManager.removeUpdates(locationListener);
        locationListener = null;
        locationManager = null;
    }
}
