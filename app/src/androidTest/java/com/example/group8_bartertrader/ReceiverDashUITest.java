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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReceiverDashUITest {

    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testLocationTextView_InitialState() throws UiObjectNotFoundException {
        // Grant permission
        UiObject allowButton = device.findObject(new UiSelector()
                .textMatches("(?i)While using the app"));
        if (allowButton.exists()) {
            allowButton.click();
        }
    }

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
    }
