package com.example.group8_bartertrader;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiWatcher;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Author: Junfa Li
 *
 * UI Automator test for SettingsActivity
 * Make Sure you set the laucher as the settings page before running the test
 *
 * Result: All passed
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityUITest {

    private UiDevice device;
    private static final String PACKAGE_NAME = "com.example.group8_bartertrader";
    private static final int TIMEOUT = 3000; // 3秒超时

    @Before
    public void setUp() throws Exception {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 确保清理之前的 Activity
            context.startActivity(intent);
        }

        Thread.sleep(15000);

    }

    @Test
    public void testLogoutFlow() throws UiObjectNotFoundException {
        // ========== AT-2:  ==========
        UiObject logoutButton = device.findObject(
                new UiSelector().resourceId(PACKAGE_NAME + ":id/LogOutButton")
        );
        assertTrue("Logout Button Not Found", logoutButton.exists());

        // ========== AT-3:  ==========
        logoutButton.click();

        // ========== AT-4:  ==========
        boolean isLoginScreen = device.wait(Until.hasObject(By.res(PACKAGE_NAME, "loginBtn")), TIMEOUT);
        assertTrue("Login Page Not Redirected to", isLoginScreen);
    }

    @After
    public void tearDown() throws Exception {
        // if needed
    }
}
