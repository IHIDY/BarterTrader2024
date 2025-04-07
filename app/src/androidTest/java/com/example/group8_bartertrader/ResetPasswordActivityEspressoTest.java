package com.example.group8_bartertrader;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityEspressoTest {
    public ActivityScenario<SettingsActivity> scenario;

    /**
     * before test
     */
    @Before
    public void setup() {
        scenario = ActivityScenario.launch(SettingsActivity.class);
    }

    /**
     * check display page
     */
    @Test
    public void checkIfPageDisplayed() {
        onView(withId(R.id.resetPasswordButton)).perform(click());
        onView(withId(R.id.submitEmailButton)).check(matches(isDisplayed()));
    }

    /**
     * check empty email
     */
    @Test
    public void checkEmptyEmail() {
        onView(withId(R.id.resetPasswordButton)).perform(click());
        onView(withId(R.id.emailEditText)).perform(typeText(""));
        onView(withId(R.id.submitEmailButton)).perform(click());
        onView(withText("Please enter an email")).check(matches(isDisplayed()));
    }

    /**
     * check invalid email
     */
    @Test
    public void checkInValidEmail() {
        onView(withId(R.id.resetPasswordButton)).perform(click());
        onView(withId(R.id.emailEditText)).perform(typeText("akweuhfcbfuy"));
        onView(withId(R.id.submitEmailButton)).perform(click());
        onView(withText("Invalid email format")).check(matches(isDisplayed()));
    }

    /**
     * check not registered email
     */
    @Test
    public void checkNotRegisteredEmail() {
        onView(withId(R.id.resetPasswordButton)).perform(click());
        onView(withId(R.id.emailEditText)).perform(typeText("DO_NOT_Register@dal.ca"));
        onView(withId(R.id.submitEmailButton)).perform(click());
//        onView(withId(R.id.statusLabel)).check(matches(withText("Email not registered")));
        onView(withText("Email not registered")).check(matches(isDisplayed()));
    }

    /**
     * check valid email
     */
    @Test
    public void checkValidEmail() {
        onView(withId(R.id.resetPasswordButton)).perform(click());
        onView(withId(R.id.emailEditText)).perform(typeText("testReset@gmail.com"));
        onView(withId(R.id.submitEmailButton)).perform(click());
        onView(withId(R.id.resetPasswordButton)).check(matches(isDisplayed()));
    }


}