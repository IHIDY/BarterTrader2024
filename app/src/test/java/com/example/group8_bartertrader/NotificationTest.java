package com.example.group8_bartertrader;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group8_bartertrader.model.PreferencesManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

@RunWith(AndroidJUnit4.class)

public class NotificationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testReceiveMatchingNotification() {
        // Set up test preferences
        Set<String> testCategories = new HashSet<>(Collections.singletonList("Electronics"));
        Set<String> testLocations = new HashSet<>(Collections.singletonList("New York"));

        PreferencesManager.getInstance(InstrumentationRegistry.getTargetContext())
                .savePreferredCategories(testCategories)
                .savePreferredLocations(testLocations);

        // Simulate a new product posting
        Product testProduct = new Product("MacBook Pro", "Electronics", "New York");
        NotificationService notificationService = new NotificationService(
                PreferencesManager.getInstance(InstrumentationRegistry.getTargetContext()),
                new TestNotificationManager()
        );

        notificationService.checkProductForNotification(testProduct);

        // Verify notification appears
        onView(withText("New product matching your preferences")).check(matches(isDisplayed()));
        onView(withText("MacBook Pro in New York")).check(matches(isDisplayed()));
    }

    @Test
    public void testNoNotificationForNonMatchingProduct() {
        // Set up test preferences
        Set<String> testCategories = new HashSet<>(Collections.singletonList("Electronics"));
        Set<String> testLocations = new HashSet<>(Collections.singletonList("New York"));

        PreferencesManager.getInstance(InstrumentationRegistry.getTargetContext())
                .savePreferredCategories(testCategories)
                .savePreferredLocations(testLocations);

        // Simulate a non-matching product posting
        Product testProduct = new Product("Sofa", "Furniture", "Chicago");
        TestNotificationManager testNotificationManager = new TestNotificationManager();
        NotificationService notificationService = new NotificationService(
                PreferencesManager.getInstance(InstrumentationRegistry.getTargetContext()),
                testNotificationManager
        );

        notificationService.checkProductForNotification(testProduct);

        // Verify no notification was sent
        assertEquals(0, testNotificationManager.getNotificationCount());
    }

    private static class TestNotificationManager {
        private int notificationCount = 0;

        @Override
        public void sendNotification(Notification notification) {
            notificationCount++;
        }

        public int getNotificationCount() {
            return notificationCount;
        }
    }
}
