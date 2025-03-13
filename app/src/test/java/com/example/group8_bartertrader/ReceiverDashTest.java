package com.example.group8_bartertrader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceiverDashTest {

    @Mock
    private Context context;

    @Mock
    private Button receiverSettingBtn;

    @Mock
    private TextView locationTextView;

    @Mock
    private RecyclerView productRecyclerView;

    @Mock
    private LocationHelper locationHelper;

    @Mock
    private DatabaseReference productsRef;

    @Mock
    private FirebaseDatabase firebaseDatabase;

    private ReceiverDash receiverDash;

    @Before
    public void setUp() {
        receiverDash = new ReceiverDash();
        receiverDash.locationHelper = locationHelper;
        receiverDash.productsRef = productsRef;
        receiverDash.receiverSettingBtn = receiverSettingBtn;
        receiverDash.locationTextView = locationTextView;
        receiverDash.productRecyclerView = productRecyclerView;

        when(firebaseDatabase.getReference("Products")).thenReturn(productsRef);
    }

    @Test
    public void testOnCreate_LocationPermissionGranted() {
        // Mock permission granted
        when(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        receiverDash.onCreate(new Bundle());

        // Verify that getCurrentLocation is called
        verify(locationHelper).getCurrentLocation(receiverDash);
    }

    @Test
    public void testOnCreate_LocationPermissionNotGranted() {
        // Mock permission not granted
        when(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        receiverDash.onCreate(new Bundle());

        // Verify that requestPermissions is called
        verify((Activity) context).requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Test
    public void testOnRequestPermissionsResult_PermissionGranted() {
        // Mock permission granted
        int[] grantResults = {PackageManager.PERMISSION_GRANTED};
        receiverDash.onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, grantResults);

        // Verify that getCurrentLocation is called
        verify(locationHelper).getCurrentLocation(receiverDash);
    }

    @Test
    public void testOnRequestPermissionsResult_PermissionDenied() {
        // Mock permission denied
        int[] grantResults = {PackageManager.PERMISSION_DENIED};
        receiverDash.onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, grantResults);

        // Verify that onLocationFetchFailed is called
        verify(locationHelper).onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, grantResults);
    }

    @Test
    public void testOnLocationFetched() {
        double latitude = 37.7749;
        double longitude = -122.4194;

        receiverDash.onLocationFetched(latitude, longitude);

        // Verify that locationTextView is updated
        verify(locationTextView).setText("Lat: " + latitude + ", Long: " + longitude);
        // Verify that fetchProductsFromFirebase is called
        verify(productsRef).addValueEventListener(any());
    }

    @Test
    public void testReceiverSettingBtnClick() {
        receiverDash.onCreate(new Bundle());

        // Simulate button click
        receiverSettingBtn.performClick();

        // Verify that SettingsActivity is started
        verify(context).startActivity(any(Intent.class));
    }
}
