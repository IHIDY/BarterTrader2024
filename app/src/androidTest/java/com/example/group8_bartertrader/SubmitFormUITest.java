package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.util.Log;

import static com.example.group8_bartertrader.EspressoUtils.waitFor;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class SubmitFormUITest {
    private ActivityScenario<SubmitForm> scenario;

    @Before
    public void setup() throws InterruptedException {
        // Set up the Mock user
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword("testreset@gmail.com", "Password1!")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseTest", "Test user signed in: " + auth.getCurrentUser().getEmail());
                    } else {
                        Log.e("FirebaseTest", "Test sign-in failed: " + task.getException().getMessage());
                    }
                });

        Thread.sleep(3000);

        // Set up the Mock Intent
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SubmitForm.class);
        intent.putExtra("productId", "test456");
        intent.putExtra("productName", "Test Product");
        intent.putExtra("productCategory", "Electronics");
        intent.putExtra("productLocation", "New York");
        intent.putExtra("productDescription", "Test Description");

        scenario = ActivityScenario.launch(intent);

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "settings put global window_animation_scale 0");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "settings put global transition_animation_scale 0");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "settings put global animator_duration_scale 0");
    }

    @Rule
    public ActivityScenarioRule<SubmitForm> activityRule =
            new ActivityScenarioRule<>(SubmitForm.class);

    @Test
    public void testSubmitOffer() throws InterruptedException {
        // Make sure the intent is passed correctly
        scenario.onActivity(activity -> {
            assertNotNull(activity.getIntent().getStringExtra("productId"));
            assertNotNull(activity.getIntent().getStringExtra("productName"));
            assertNotNull(activity.getIntent().getStringExtra("productCategory"));
            assertNotNull(activity.getIntent().getStringExtra("productLocation"));
            assertNotNull(activity.getIntent().getStringExtra("productDescription"));
        });

        // Enter the test info
        onView(withId(R.id.productName)).perform(replaceText("Test Offer Item 2"), closeSoftKeyboard());
        onView(withId(R.id.productDescription)).perform(replaceText("Test Offer Description 2"), closeSoftKeyboard());

        onView(withId(R.id.productCategory)).perform(click());
        onView(withText("Electronics")).perform(click());
        onView(withId(R.id.productLocation)).perform(replaceText("Dalhousie University"), closeSoftKeyboard());


        // Click the submit button
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.submitProduct)).perform(click());

        onView(isRoot()).perform(waitFor(1000));
        // Make sure the snackbar message about the submission result is displayed
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSubmitDuplicateOffer() throws InterruptedException {
        // Make sure the intent is passed correctly
        scenario.onActivity(activity -> {
            assertNotNull(activity.getIntent().getStringExtra("productId"));
            assertNotNull(activity.getIntent().getStringExtra("productName"));
            assertNotNull(activity.getIntent().getStringExtra("productCategory"));
            assertNotNull(activity.getIntent().getStringExtra("productLocation"));
            assertNotNull(activity.getIntent().getStringExtra("productDescription"));
        });

        // Enter the test info
        onView(withId(R.id.productName)).perform(replaceText("Test Offer Item 2"), closeSoftKeyboard());
        onView(withId(R.id.productDescription)).perform(replaceText("Test Offer Description 2"), closeSoftKeyboard());

        onView(withId(R.id.productCategory)).perform(click());
        onView(withText("Electronics")).perform(click());

        // Click the submit button
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.submitProduct)).perform(click());
        onView(withId(R.id.productLocation)).perform(replaceText("Dalhousie University"), closeSoftKeyboard());

        onView(isRoot()).perform(waitFor(1000));
        // Make sure the snackbar message about the submission result is displayed
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Failed to submit offer!")));
    }
}
