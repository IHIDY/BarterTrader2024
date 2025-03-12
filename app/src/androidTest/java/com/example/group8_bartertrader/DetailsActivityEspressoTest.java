package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;
import static java.util.regex.Pattern.matches;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.example.group8_bartertrader.CRUD.ProductCRUD;
import com.example.group8_bartertrader.model.Product;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DetailsActivityEspressoTest {
    ProductCRUD crud;
    ArrayList<Product> products;
    ActivityScenario<DetailsActivity> activityScenario;
    private UiDevice device;
    final String launcherPackage = "com.example.group8_bartertrader";
    private static final int LAUNCH_TIMEOUT = 5000;

    @Before
    public void setup() {
        crud = new ProductCRUD();
        products = crud.collectProducts();

        device = UiDevice.getInstance(getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackage);
        assert launchIntent != null;
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launchIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testProductViewOne() {
        activityScenario = ActivityScenario.launch(DetailsActivity.class);
        activityScenario.onActivity(activity -> {
            Product product = crud.deliverTopProduct(products);
            activity.selectedProduct = product;
            activity.showProductDetails(product);
        });
        onView(withText("Pink Vase")).check(matches(isDisplayed()));
        onView(withText("Good Condition")).check(matches(isDisplayed()));
        onView(withText("Home Decor")).check(matches(isDisplayed()));
        //test for isAvailable when isAvailable completed
        onView(withText("Halifax")).check(matches(isDisplayed()));
    }

    @Test
    public void testOfferExchange() throws UiObjectNotFoundException {
        UiObject offerExchangeButton = device.findObject(new UiSelector().text("Offer Exchange"));
        offerExchangeButton.clickAndWaitForNewWindow();

        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        UiObject exchangeActivity = device.findObject(new UiSelector().textContains("Get Current Location"));

        assertTrue(exchangeActivity.exists());
    }

    @Test
    public void testExitButton() throws UiObjectNotFoundException {
        UiObject exitButton = device.findObject(new UiSelector().text("Exit"));
        exitButton.clickAndWaitForNewWindow();

        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        UiObject productActivity = device.findObject(new UiSelector().resourceId("com.example.group8_bartertrader:id/receiverDashActivity"));

        assertTrue(productActivity.exists());
    }
}
