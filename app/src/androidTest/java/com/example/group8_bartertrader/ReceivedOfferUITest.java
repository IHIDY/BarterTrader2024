package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static java.util.regex.Pattern.matches;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReceivedOfferUITest {
    private ActivityScenario<ReceivedOfferActivity> scenario;


    @Before
    public void setup() throws InterruptedException {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword("provider@test.com", "Password1!")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseTest", "Test user signed in: " + auth.getCurrentUser().getEmail());
                    } else {
                        Log.e("FirebaseTest", "Test sign-in failed: " + task.getException().getMessage());
                    }
                });

        Thread.sleep(3000);

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ReceivedOfferActivity.class);
        scenario = ActivityScenario.launch(intent);
    }

    @Rule
    public ActivityScenarioRule<ReceivedOfferActivity> activityRule = new ActivityScenarioRule<>(ReceivedOfferActivity.class);

    @Test
    public void RecyclerViewDisplayTest(){
    onView(withId(R.id.receivedOffersRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void backButtonTest(){
        onView(withId(R.id.backButton)).perform(click());
    }

    @Test
    public void spinnerTest() {
        onView(withContentDescription("OfferSpinner-0")).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Accepted"))).perform(click());
        onView(withContentDescription("OfferSpinner-0")).check(matches(withSpinnerText(containsString("Accepted"))));

    }
}
