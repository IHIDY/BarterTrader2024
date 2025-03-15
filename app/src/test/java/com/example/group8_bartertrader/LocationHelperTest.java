package com.example.group8_bartertrader;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocationHelperTest {

    @Test
    public void testParseLatLngLocation_ValidInput() {
        String latLngLocation = "37.7749,-122.4194";
        double[] result = LocationHelper.parseLatLngLocation(latLngLocation);
        assertNotNull(result);
        assertEquals(37.7749, result[0], 0.0001); // Latitude
        assertEquals(-122.4194, result[1], 0.0001); // Longitude
    }

    @Test
    public void testParseLatLngLocation_InvalidInput_MissingComma() {
        String latLngLocation = "37.7749-122.4194";
        double[] result = LocationHelper.parseLatLngLocation(latLngLocation);
        assertNull(result);
    }

    @Test
    public void testParseLatLngLocation_InvalidInput_NonNumeric() {
        String latLngLocation = "invalid,input";
        double[] result = LocationHelper.parseLatLngLocation(latLngLocation);
        assertNull(result);
    }

    @Test
    public void testParseLatLngLocation_NullInput() {
        double[] result = LocationHelper.parseLatLngLocation(null);
        assertNull(result);
    }

    @Test
    public void testParseLatLngLocation_EmptyInput() {
        String latLngLocation = "";
        double[] result = LocationHelper.parseLatLngLocation(latLngLocation);
        assertNull(result);
    }

    @Test
    public void testIsWithinRange_WithinRange() {
        double userLat = 37.7749;
        double userLong = -122.4194;
        double productLat = 37.7849;
        double productLong = -122.4294;

        assertTrue(LocationHelper.isWithinRange(userLat, userLong, productLat, productLong));
    }

    @Test
    public void testIsWithinRange_OutsideRange() {
        double userLat = 37.7749;
        double userLong = -122.4194;
        double productLat = 38.7849;
        double productLong = -122.4294;

        assertFalse(LocationHelper.isWithinRange(userLat, userLong, productLat, productLong));
    }

    @Test
    public void testCalculateDistance_ValidCoordinates() {
        double lat1 = 37.7749;
        double lon1 = -122.4194;
        double lat2 = 37.7849;
        double lon2 = -122.4294;

        double distance = LocationHelper.calculateDistance(lat1, lon1, lat2, lon2);
        assertEquals(1.41, distance, 0.1); // Allow for minor floating-point differences
    }

    @Test
    public void testCalculateDistance_SameCoordinates() {
        double lat1 = 37.7749;
        double lon1 = -122.4194;
        double lat2 = 37.7749;
        double lon2 = -122.4194;

        double distance = LocationHelper.calculateDistance(lat1, lon1, lat2, lon2);
        assertEquals(0.0, distance, 0.0001); // Distance should be 0
    }

}
