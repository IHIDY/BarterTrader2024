package com.example.group8_bartertrader;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import android.content.Context;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationHelperTest {

    @Mock
    private Context mockContext;

    @Mock
    private FusedLocationProviderClient mockFusedLocationClient;

    @Mock
    private Task<Location> mockLocationTask;

    @Mock
    private Location mockLocation;

    private LocationHelper locationHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        locationHelper = new LocationHelper(mockContext);
        locationHelper.fusedLocationClient = mockFusedLocationClient;
    }

    @Test
    public void testGetCurrentLocation_WithPermissionGranted() {
        try (MockedStatic<ContextCompat> mockedStatic = Mockito.mockStatic(ContextCompat.class)) {
            mockedStatic.when(() -> ContextCompat.checkSelfPermission(mockContext, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    .thenReturn(android.content.pm.PackageManager.PERMISSION_GRANTED);

            when(mockFusedLocationClient.getLastLocation()).thenReturn(mockLocationTask);
            when(mockLocationTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                OnSuccessListener<Location> listener = invocation.getArgument(0);
                listener.onSuccess(mockLocation);
                return mockLocationTask;
            });

            locationHelper.getCurrentLocation(mockListener);

            verify(mockFusedLocationClient).getLastLocation();
            verifyNoMoreInteractions(mockListener);

        }
    }

    @Test
    public void testFetchLocation_Success() {
        when(mockFusedLocationClient.getLastLocation()).thenReturn(mockLocationTask);
        when(mockLocationTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            OnSuccessListener<Location> listener = invocation.getArgument(0);
            listener.onSuccess(mockLocation);
            return mockLocationTask;
        });

        when(mockLocation.getLatitude()).thenReturn(10.0);
        when(mockLocation.getLongitude()).thenReturn(20.0);
        locationHelper.fetchLocation();

        verify(mockListener).onLocationFetched(10.0, 20.0);
    }

}
