package com.example.group8_bartertrader;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.group8_bartertrader.RegistrationActivity;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityEspressoTest {
    public ActivityScenario<ResetPasswordActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(ResetPasswordActivity.class);
    }

    @Test
    public void checkIfEmailIsEmpty() {

    }
}