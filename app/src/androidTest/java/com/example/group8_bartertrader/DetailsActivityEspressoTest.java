package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.example.group8_bartertrader.model.Product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DetailsActivityEspressoTest {
    private UiDevice device;
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String launcherPackage = "com.example.group8_bartertrader";
    private Product testProduct;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        assert launchIntent != null;
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launchIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Create a test product
        testProduct = new Product("3", "ellaemail@gmail.com", "Black Sunglasses",
                "Fashionable Sunglasses", "Accesories", true, "Halifax", "Halifax", "Good", 2023-05-01);
    }

    @Test
    public void testOfferExchangeButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DetailsActivity.class);
        intent.putExtra("Product", testProduct);
        ActivityScenario.launch(intent);

        onView(withId(R.id.OfferExchangeButton)).perform(click());
        device.wait(Until.hasObject(By.textContains("Get Current Location")), LAUNCH_TIMEOUT);
    }

    @Test
    public void testExitButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DetailsActivity.class);
        intent.putExtra("Product", testProduct);
        ActivityScenario.launch(intent);

        onView(withId(R.id.ExitButton)).perform(click());
    }
}
