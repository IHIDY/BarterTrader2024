package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.app.Application;
import android.content.Intent;
import android.text.method.Touch;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

public class chatUITest {
    //When the offer is accepted, the chat button should appear

    /**
     * test chat enabled
     */
    @Test
    public void chatEnabledTest(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra("offerId", "-OMX3e7jHiipVy4FnViz"); //OfferId is accepted (true)
        ActivityScenario<ChatActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.sendButton)).check(matches(isEnabled()));
        onView(withId(R.id.messageEditText)).check(matches(isEnabled()));
    }

    /**
     * test message sent
     */
    @Test
    public void sendMessageTest(){
        String testMessage = "Test Message sent";
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra("offerId", "-OMX3e7jHiipVy4FnViz");
        ActivityScenario<ChatActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.messageEditText)).perform(typeText(testMessage));
        onView(withId(R.id.sendButton)).perform(click());
        onView(withText(testMessage)).check(matches(isDisplayed()));
    }
}
