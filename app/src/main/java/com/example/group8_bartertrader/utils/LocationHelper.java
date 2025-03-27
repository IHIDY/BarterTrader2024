package com.example.group8_bartertrader.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private static Context context;
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

    // Convert Latitude and Longitude to City Name
    static String getCityName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String cityName = address.getAdminArea(); // Get the administrative area (e.g., state)
                if (cityName == null) {
                    cityName = address.getLocality(); // Get the locality (e.g., city)
                    if (cityName == null) {
                        cityName = address.getSubAdminArea(); // Get the sub-administrative area
                    }
                }
                return cityName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Location: Not available";
    }

    public static boolean isWithinRange(double userLat, double userLong, double productLat, double productLong) {
        final int RADIUS = 10; // Radius in kilometers
        double distance = calculateDistance(userLat, userLong, productLat, productLong);
        return distance <= RADIUS;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public interface OnLocationFetchListener {
        void onLocationFetched(double latitude, double longitude);
        void onLocationFetchFailed(String errorMessage);
    }
}


