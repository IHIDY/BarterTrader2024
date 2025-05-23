package com.example.group8_bartertrader;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.checkerframework.checker.guieffect.qual.UI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityUITest {
    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackage = "com.example.group8_bartertrader";
    private UiDevice device;

    /**
     * before test
     */
    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        assert launchIntent != null;
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launchIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    /**
     * check if page displayed
     * @throws UiObjectNotFoundException
     */
    @Test
    public void checkIfPageIsDisplayed() throws UiObjectNotFoundException {
        enterResetPasswordPage();
        UiObject emailSubmitButton = device.findObject(new UiSelector().text("Submit"));
        assertTrue(emailSubmitButton.exists());
    }

    /**
     * check move to password form
     * @throws UiObjectNotFoundException
     */
    @Test
    public void checkIfMoveToResetPasswordForm() throws UiObjectNotFoundException {
        enterResetPasswordPage();
        UiObject emailBox = device.findObject(new UiSelector().text("Enter your registered email"));
        emailBox.setText("testReset@gmail.com");                                             //valid email check later
        UiObject emailSubmitButton = device.findObject(new UiSelector().text("Submit"));
        emailSubmitButton.clickAndWaitForNewWindow();
        UiObject PasswordSubmitButton = device.findObject(new UiSelector().text("Reset Password"));
        assertTrue(PasswordSubmitButton.exists());
    }

    /**
     * check reset password
     * @throws UiObjectNotFoundException
     */
    @Test
    public void checkResetDifferentPassword() throws UiObjectNotFoundException {
        enterResetPasswordPage();
        UiObject emailBox = device.findObject(new UiSelector().text("Enter your registered email"));
        emailBox.setText("testReset@gmail.com");                                             //valid email check later
        UiObject emailSubmitButton = device.findObject(new UiSelector().text("Submit"));
        emailSubmitButton.clickAndWaitForNewWindow();
        UiObject passwordBox = device.findObject(new UiSelector().text("Enter your registered email"));
        passwordBox.setText("Password1!");
        UiObject confirmBox = device.findObject(new UiSelector().text("Enter your registered email"));
        confirmBox.setText("NotPassword");
        UiObject PasswordSubmitButton = device.findObject(new UiSelector().text("Reset Password"));
        PasswordSubmitButton.click();
        UiObject toast = device.findObject(new UiSelector().className("android.widget.TextView").textStartsWith("Passwords do not match"));
        boolean exist = toast.waitForExists(5000);
        assertTrue(exist);
    }

    /**
     * check reset invalid password
     * @throws UiObjectNotFoundException
     */
    @Test
    public void checkResetInvalidPassword() throws UiObjectNotFoundException {
        enterResetPasswordPage();
        UiObject emailBox = device.findObject(new UiSelector().text("Enter your registered email"));
        emailBox.setText("testReset@gmail.com");                                             //valid email check later
        UiObject emailSubmitButton = device.findObject(new UiSelector().text("Submit"));
        emailSubmitButton.clickAndWaitForNewWindow();
        UiObject passwordBox = device.findObject(new UiSelector().text("Enter your registered email"));
        passwordBox.setText("12345");
        UiObject confirmBox = device.findObject(new UiSelector().text("Enter your registered email"));
        confirmBox.setText("12345");
        UiObject PasswordSubmitButton = device.findObject(new UiSelector().text("Reset Password"));
        PasswordSubmitButton.click();
        UiObject toast = device.findObject(new UiSelector().className("android.widget.TextView").textStartsWith("Password must be at least 6 characters"));
        boolean exist = toast.waitForExists(5000);
        assertTrue(exist);
    }

    /**
     * check password reset
     * @throws UiObjectNotFoundException
     */
    @Test
    public void checkPasswordReset() throws UiObjectNotFoundException {
        enterResetPasswordPage();
        UiObject emailBox = device.findObject(new UiSelector().text("Enter your registered email"));
        emailBox.setText("testReset@gmail.com");                                             //valid email check later
        UiObject emailSubmitButton = device.findObject(new UiSelector().text("Submit"));
        emailSubmitButton.clickAndWaitForNewWindow();
        UiObject passwordBox = device.findObject(new UiSelector().text("Enter your registered email"));
        passwordBox.setText("Password2!");
        UiObject confirmBox = device.findObject(new UiSelector().text("Enter your registered email"));
        confirmBox.setText("Password2!");
        UiObject PasswordSubmitButton = device.findObject(new UiSelector().text("Reset Password"));
        PasswordSubmitButton.clickAndWaitForNewWindow();
        UiObject emailBox1 = device.findObject(new UiSelector().text("Enter your Email"));
        emailBox1.setText("test@email.com");
        UiObject passwordBox1 = device.findObject(new UiSelector().text("Enter your Password"));
        passwordBox1.setText("Password2!");
        UiObject Login = device.findObject(new UiSelector().text("Login"));
        Login.clickAndWaitForNewWindow();
        UiObject SettingsButton = device.findObject(new UiSelector().text("Settings"));
        assertTrue(SettingsButton.exists());
    }

    /**
     * enter reset password page
     * @throws UiObjectNotFoundException
     */
    private void enterResetPasswordPage() throws UiObjectNotFoundException {
        UiObject LoginButton = device.findObject(new UiSelector().text("Login"));
        LoginButton.clickAndWaitForNewWindow();
        UiObject emailBox = device.findObject(new UiSelector().text("Enter your Email"));
        emailBox.setText("testReset@gmail.com");
        UiObject passwordBox = device.findObject(new UiSelector().text("Enter your Password"));
        passwordBox.setText("Password1!");
        UiObject Login = device.findObject(new UiSelector().text("Login"));
        Login.clickAndWaitForNewWindow();
        UiObject SettingsButton = device.findObject(new UiSelector().text("Settings"));
        SettingsButton.clickAndWaitForNewWindow();
        UiObject ResetPasswordButton = device.findObject(new UiSelector().text("Reset Password"));
        ResetPasswordButton.clickAndWaitForNewWindow();
    }
}
