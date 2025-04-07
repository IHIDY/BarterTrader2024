package com.example.group8_bartertrader;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.notification.ViewPushNotificationActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class ViewPushNotificationActivityTest {

    /**
     * test
     */
    @Test
    public void testNotificationDataDisplayedCorrectly() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewPushNotificationActivity.class);
        intent.putExtra("title", "Job Alert");
        intent.putExtra("body", "You have a new job offer!");
        intent.putExtra("job_id", "JOB123");
        intent.putExtra("jobLocation", "New York");

        try (ActivityScenario<ViewPushNotificationActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.titleTV)).check(matches(withText("Job Alert")));
            onView(withId(R.id.bodyTV)).check(matches(withText("You have a new job offer!")));
            onView(withId(R.id.productIdTV)).check(matches(withText("JOB123")));
            onView(withId(R.id.productLocationTV)).check(matches(withText("New York")));
        }
    }

    /**
     * test
     */
    @Test
    public void testMissingExtrasHandledGracefully() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ViewPushNotificationActivity.class);

        try (ActivityScenario<ViewPushNotificationActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.titleTV)).check(matches(withText("")));
            onView(withId(R.id.bodyTV)).check(matches(withText("")));
            onView(withId(R.id.productIdTV)).check(matches(withText("")));
            onView(withId(R.id.productLocationTV)).check(matches(withText("")));
        }
    }
}
