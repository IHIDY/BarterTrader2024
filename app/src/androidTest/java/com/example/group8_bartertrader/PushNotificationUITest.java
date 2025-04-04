package com.example.group8_bartertrader.notification;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group8_bartertrader.R;
import com.google.android.material.snackbar.Snackbar;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PushNotificationUITest {

    @Rule
    public ActivityScenarioRule<NotificationActivity> activityScenarioRule =
            new ActivityScenarioRule<>(NotificationActivity.class);

    @Test
    public void testSendNotificationButton() throws InterruptedException {
        // Click on the Send Notification button
        onView(withId(R.id.sendNotificationBtn)).perform(click());

        // Wait for 2 seconds to allow Snackbar to be displayed
        Thread.sleep(2000);

        // Verify that a Snackbar appears with the success message
        onView(Matchers.allOf(withText("Notification Sent Successfully"), isDisplayed()))
                .check(matches(isDisplayed()));
    }

}
