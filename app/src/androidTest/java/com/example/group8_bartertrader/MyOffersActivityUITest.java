package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.example.group8_bartertrader.EspressoUtils.waitFor;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static org.hamcrest.Matchers.not;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.group8_bartertrader.MyOffersActivity;
import com.example.group8_bartertrader.R;
import com.google.firebase.auth.FirebaseAuth;

@RunWith(AndroidJUnit4.class)
public class MyOffersActivityUITest {

    @Rule
    public ActivityScenarioRule<MyOffersActivity> activityRule =
            new ActivityScenarioRule<>(MyOffersActivity.class);

    /**
     * before test
     */
    @Before
    public void setup() {
        init();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("testreset@gmail.com", "Password1!")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseTest", "Test user signed in: " + auth.getCurrentUser().getEmail());
                    } else {
                        Log.e("FirebaseTest", "Test sign-in failed: " + task.getException().getMessage());
                    }
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ActivityScenario.launch(MyOffersActivity.class);
    }

    /**
     * after test
     */
    @After
    public void tearDown() {
        release(); // Intents 释放
    }

    /**
     * test settings button
     */
    @Test
    public void testGoToSettingsButton() {
        // Make sure the button exist
        onView(withId(R.id.recsettingbutton)).check(matches(isDisplayed()));

        // CLick the Button
        onView(withId(R.id.recsettingbutton)).perform(click());

        onView(isRoot()).perform(waitFor(3000));

        // Make sure SettingsActivity is displayed
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.activity_settings))
                .check(matches(isDisplayed()));
    }

    /**
     * test product item list display
     */
    @Test
    public void testProductItemListDisplayed() {
        // Make sure the REcyclerView is displayed
        onView(withId(R.id.productRecyclerView))
                .check(matches(isDisplayed()));
    }

}



