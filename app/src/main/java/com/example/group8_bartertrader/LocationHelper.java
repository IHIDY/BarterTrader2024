package com.example.group8_bartertrader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {

    private Context context;
    FusedLocationProviderClient fusedLocationClient;
    private OnLocationFetchListener listener;

    public LocationHelper(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getCurrentLocation(OnLocationFetchListener listener) {
        this.listener = listener;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    //separate latlng value
    public static double[] parseLatLngLocation(String latLngLocation) {
        if (latLngLocation == null || latLngLocation.isEmpty()) {
            return null;
        }
        String[] parts = latLngLocation.split(",");
        if (parts.length != 2) {
            return null;
        }
        try {
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            return new double[]{latitude, longitude};
        } catch (NumberFormatException e) {
            return null;
        }
    }


    //Handles the result of the permission request.
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                if (listener != null) {
                    listener.onLocationFetchFailed("Location permission denied");
                }
            }
        }
    }

    //Fetches the location using FusedLocationProviderClient.

    void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onLocationFetchFailed("Location permission not granted");
            }
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            if (listener != null) {
                                listener.onLocationFetched(location.getLatitude(), location.getLongitude());
                            }
                        } else {
                            if (listener != null) {
                                listener.onLocationFetchFailed("Unable to fetch location");
                            }
                        }
                    }
                });
    }

    public interface OnLocationFetchListener {
        void onLocationFetched(double latitude, double longitude);
        void onLocationFetchFailed(String errorMessage);
    }
}


