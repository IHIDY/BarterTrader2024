package com.example.group8_bartertrader;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

public class GoogleMapUITest {
    public String launcherPackage = "com.example.group8_bartertrader";
    public int TIME_OUT = 5000;
    UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        assert launcherIntent != null;
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIME_OUT);
    }

    @Test
    public void testGoogleMapDisplayed() throws UiObjectNotFoundException {
        UiObject loginButtonOnRegister = device.findObject(new UiSelector().text("Login"));
        assertTrue("Login button on register page should be visible", loginButtonOnRegister.exists());
        loginButtonOnRegister.clickAndWaitForNewWindow();

        // Step 2: Enter Email and Password
        UiObject emailField = device.findObject(new UiSelector().text("Enter your Email"));
        UiObject passwordField = device.findObject(new UiSelector().text("Enter your Password"));
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));

        assertTrue("Email field should be visible", emailField.exists());
        assertTrue("Password field should be visible", passwordField.exists());
        assertTrue("Login button should be visible", loginButton.exists());

        emailField.setText("testreset@gmail.com");
        passwordField.setText("Password1!");
        loginButton.clickAndWaitForNewWindow();

        UiObject allowButton = device.findObject(new UiSelector().textMatches("(?i)While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }

        // Step 3: Verify Main Page Loaded
        UiObject searchButton = device.findObject(new UiSelector().text("Search"));
        assertTrue("Search button should be visible, meaning main page loaded", searchButton.exists());

        // Step 4: Scroll to "View On Map" button
        UiScrollable recyclerView = new UiScrollable(new UiSelector().scrollable(true));
        assertTrue("RecyclerView should be visible", recyclerView.exists());

        recyclerView.scrollIntoView(new UiSelector().text("View On Map"));

        // Step 5: Click "View On Map" button in the first item
        UiObject viewOnMapButton = device.findObject(new UiSelector().text("View On Map"));
        assertTrue("View On Map button should be visible", viewOnMapButton.exists());
        viewOnMapButton.clickAndWaitForNewWindow();

        // Step 6: Verify Google Maps is displayed
        UiObject googleMap = device.findObject(new UiSelector().descriptionContains("Google Map"));
        boolean isMapLoaded = googleMap.waitForExists(5000);
        assertTrue("Google Maps should be displayed", isMapLoaded);
    }

}
