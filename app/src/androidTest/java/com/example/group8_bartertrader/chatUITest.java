package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

public class chatUITest {
    //When the offer has not been accepted yet, the chat button should not be visable
    @Test
    public void chatDisabledTest(){
        String testOfferId = "TestNotAccepted";

        //set offerAccepted to false before the activity launches
        FirebaseDatabase.getInstance().getReference("chats").child(testOfferId).child("offerAccepted").setValue(false);

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra("offerId", testOfferId);
        ActivityScenario<ChatActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.sendButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.messageEditText)).check(matches(not(isEnabled())));
    }

    //When the offer is accepted, the chat button should appear
    @Test
    public void chatEnabledTest(){
        String testOfferId = "TestAccepted";

        //set offerAccepted to true before the activity launches
        FirebaseDatabase.getInstance().getReference("chats").child(testOfferId).child("offerAccepted").setValue(true);

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra("offerId", testOfferId);
        ActivityScenario<ChatActivity> scenario = ActivityScenario.launch(intent);

        onView(withId(R.id.sendButton)).check(matches(isEnabled()));
        onView(withId(R.id.messageEditText)).check(matches(isEnabled()));
    }
}
