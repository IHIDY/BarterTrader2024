package com.example.group8_bartertrader;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReceiverDashUITest {

    private UiDevice device;

    /**
     * before test
     */
    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    /**
     * test location is displayed
     * @throws UiObjectNotFoundException
     */
    @Test
    public void testLocationTextView_InitialState() throws UiObjectNotFoundException {
        // Grant permission
        UiObject allowButton = device.findObject(new UiSelector()
                .textMatches("(?i)While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
    }

    /**
     * test title text view is displayed
     * @throws UiObjectNotFoundException
     */
    @Test
        public void testTitleTextView_Displayed () throws UiObjectNotFoundException {
            // Launch the activity
            ActivityScenario.launch(ReceiverDash.class);
            UiObject allowButton = device.findObject(new UiSelector()
                    .textMatches("(?i)While using the app"));
            if (allowButton.exists()) {
                allowButton.click();
            }

            Espresso.onView(ViewMatchers.withId(R.id.textView2))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .check(ViewAssertions.matches(ViewMatchers.withText("Receiver Dashboard")));
        }

    /**
     * test recycler view is displayed
     * @throws UiObjectNotFoundException
     */
    @Test
        public void testRecyclerView_Displayed () throws UiObjectNotFoundException {
            // Launch the activity
            ActivityScenario.launch(ReceiverDash.class);
            UiObject allowButton = device.findObject(new UiSelector()
                    .textMatches("(?i)While using the app"));
            if (allowButton.exists()) {
                allowButton.click();
            }

            Espresso.onView(ViewMatchers.withId(R.id.productRecyclerView))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }

    /**
     * test settings button click
     * @throws UiObjectNotFoundException
     */
    @Test
        public void testSettingsButton_Click () throws UiObjectNotFoundException {
            // Launch the activity
            ActivityScenario.launch(ReceiverDash.class);
            UiObject allowButton = device.findObject(new UiSelector()
                    .textMatches("(?i)While using the app"));
            if (allowButton.exists()) {
                allowButton.click();
            }

            Espresso.onView(ViewMatchers.withId(R.id.recsettingbutton))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .check(ViewAssertions.matches(ViewMatchers.isClickable()));

            Espresso.onView(ViewMatchers.withId(R.id.recsettingbutton))
                    .perform(ViewActions.click());

        }

    /**
     * test search by category
     * @throws UiObjectNotFoundException
     */
    @Test
    public void testSearchByCategory() throws UiObjectNotFoundException {
        ActivityScenario.launch(ReceiverDash.class);
        UiObject allowButton = device.findObject(new UiSelector().textMatches("(?i)While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
        Espresso.onView(ViewMatchers.withId(R.id.filterSpinner)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.filterButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.productRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * test search by distance
     * @throws UiObjectNotFoundException
     */
    @Test
    public void testSearchByDistance() throws UiObjectNotFoundException {
        ActivityScenario.launch(ReceiverDash.class);
        UiObject allowButton = device.findObject(new UiSelector().textMatches("(?i)While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
        Espresso.onView(ViewMatchers.withId(R.id.distanceEditText)).perform(ViewActions.typeText("10"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.filterButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.productRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * test search by keyword
     * @throws UiObjectNotFoundException
     */
    @Test
    public void testSearchByKeyword() throws UiObjectNotFoundException {
        ActivityScenario.launch(ReceiverDash.class);
        UiObject allowButton = device.findObject(new UiSelector().textMatches("(?i)While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
        Espresso.onView(ViewMatchers.withId(R.id.inputEditText)).perform(ViewActions.typeText("Bottle"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.filterButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.productRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
