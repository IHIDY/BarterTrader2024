package com.example.group8_bartertrader;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProductFormEspressoTest {

    @Test
    public void testSnackbarMessageForIncompleteFields() {
        // Launch the activity
        try (ActivityScenario<ProductForm> scenario = ActivityScenario.launch(ProductForm.class)) {

            // Click on the submit button without filling the fields
            onView(withId(R.id.submitProduct)).perform(ViewActions.click());

            // Check if the Snackbar is displayed with the message "All fields are required"
            onView(withText("All fields are required")).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testSnackbarMessageForSuccess() {
        // Launch the activity
        try (ActivityScenario<ProductForm> scenario = ActivityScenario.launch(ProductForm.class)) {

            // Fill in the product form with valid data
            onView(withId(R.id.productName)).perform(ViewActions.typeText("Sample Product"));
            onView(withId(R.id.productCategory)).perform(ViewActions.click());
            onView(withText("Electronics")).perform(ViewActions.click());
            onView(withId(R.id.getLocationButton)).perform(ViewActions.click());
            onView(withId(R.id.getLocationButton)).perform(ViewActions.click());
            onView(withId(R.id.productCondition)).perform(ViewActions.click());
            onView(withText("Good")).perform(ViewActions.click());
            onView(withId(R.id.productDescription)).perform(ViewActions.typeText("Product description"));

            // Click the submit button
            onView(withId(R.id.submitProduct)).perform(ViewActions.click());

            // Check if the Snackbar is displayed with the message "Product posted successfully"
            onView(withText("Product posted successfully")).check(matches(isDisplayed()));
        }
    }

}
