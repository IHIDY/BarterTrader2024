package com.example.group8_bartertrader;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.onIdle;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class RegistrationActivityUITest {

    public ActivityScenario<RegistrationActivity> scenario;

    @Before
    public void setup(){
        scenario = ActivityScenario.launch(RegistrationActivity.class);
        scenario.onActivity(activity ->{
            activity.loadRoleSpin();
            activity.setRegBtn();
        });
    }

    @Test
    public void isEmptyEmailTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText(""));
        onView(withId(R.id.pass)).perform(replaceText("Password123!"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Provider"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_EMAIL_ADDRESS)));

    }
    @Test
    public void isNotValidEmailTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText("email"));
        onView(withId(R.id.pass)).perform(replaceText("Password123!"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Receiver"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INVALID_EMAIL_ADDRESS)));

    }
    @Test
    public void isEmptyFirstNameTest(){
        onView(withId(R.id.fName)).perform(replaceText(""));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText("user@email.com"));
        onView(withId(R.id.pass)).perform(replaceText("Password123!"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Provider"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_FNAME)));

    }
    @Test
    public void isEmptyLastNameTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText(""));
        onView(withId(R.id.email)).perform(replaceText("user@email.com"));
        onView(withId(R.id.pass)).perform(replaceText("Password123!"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Receiver"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_LNAME)));

    }
    @Test
    public void isEmptyPasswordTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText("user@email.com"));
        onView(withId(R.id.pass)).perform(replaceText(""));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Provider"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_PASSWORD)));

    }

    @Test
    public void isEmptyRoleTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText("user@email.com"));
        onView(withId(R.id.pass)).perform(replaceText("Password123!"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Select your Role"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INVALID_ROLE)));

    }

    @Test
    public void isNotValidPasswordTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText("user@email.com"));
        onView(withId(R.id.pass)).perform(replaceText("password"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Provider"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INVALID_PASSWORD)));

    }
    //Test will fail since the user has already been created and is in the database
    @Test
    public void registrationTest(){
        onView(withId(R.id.fName)).perform(replaceText("First"));
        onView(withId(R.id.lName)).perform(replaceText("Last"));
        onView(withId(R.id.email)).perform(replaceText("user@email.com"));
        onView(withId(R.id.pass)).perform(replaceText("Password123!"));
        onView(withId(R.id.roleSelect)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Provider"))).perform(click());
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.REGISTRATION_SUCCESSFUL)));

    }
}
