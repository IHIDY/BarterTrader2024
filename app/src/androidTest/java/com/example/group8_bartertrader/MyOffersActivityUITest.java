package com.example.group8_bartertrader;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class MyOffersActivityUITest {

    @Before
    public void setup() throws InterruptedException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        auth.signInWithEmailAndPassword("testreset@gmail.com", "Password1!")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseTest", "Test user signed in: " + auth.getCurrentUser().getEmail());
                    } else {
                        Log.e("FirebaseTest", "Test sign-in failed: " + task.getException().getMessage());
                    }
                    latch.countDown();
                });

        latch.await();

        activityRule = new ActivityScenarioRule<>(MyOffersActivity.class);
    }
    @Rule
    public ActivityScenarioRule<MyOffersActivity> activityRule =
            new ActivityScenarioRule<>(MyOffersActivity.class);

    @Test
    public void testRecyclerViewLoadsData() throws InterruptedException {
        Espresso.onView(withId(R.id.productRecyclerView))
                .check(matches(isDisplayed()));

        Thread.sleep(3000);

        Espresso.onView(withId(R.id.productRecyclerView))
                .check(ViewAssertions.matches(hasMinimumChildCount(1)));
    }

    @Test
    public void testSettingsButtonClick() {
        Espresso.onView(withId(R.id.recsettingbutton)).perform(click());

        Espresso.onView(withId(R.id.activity_settings))
                .check(matches(isDisplayed()));
    }
}
