package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.group8_bartertrader.model.PreferencesManager;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SetPreferencesTest {

    @Rule
    public ActivityScenarioRule<SettingsActivity> activityRule =
            new ActivityScenarioRule<>(SettingsActivity.class);

    @Before
    public void setUp() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword("testemail@email.com", "Test@123")
                .addOnCompleteListener(task -> {

                });

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PreferencesManager.getInstance(context)
                .savePreferredCategories(new HashSet<>());
    }

    @Test
    public void testPreferencesDisplay() {
        onView(withId(R.id.preferencesSummary))
                .check(matches(withText("No preferences set")));

        onView(withId(R.id.editPreferencesBtn)).perform(click());

        onView(withId(R.id.categoriesInput))
                .perform(typeText("Electronics, Furniture"), closeSoftKeyboard());
        onView(withId(R.id.locationsInput))
                .perform(typeText("New York, 10 km"), closeSoftKeyboard());

        onView(withText("Save")).perform(click());

        onView(withId(R.id.preferencesSummary))
                .check(matches(withText(containsString("Electronics"))))
                .check(matches(withText(containsString("Furniture"))))
                .check(matches(withText(containsString("New York"))))
                .check(matches(withText(containsString("10 km"))));
    }
}
