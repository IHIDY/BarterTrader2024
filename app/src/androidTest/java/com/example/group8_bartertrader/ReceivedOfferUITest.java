package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static java.util.regex.Pattern.matches;
import static androidx.test.espresso.Espresso.onData;
import static com.example.group8_bartertrader.EspressoUtils.waitFor;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReceivedOfferUITest {
    private ActivityScenario<ReceivedOfferActivity> scenario;


    @Before
    public void setup() throws InterruptedException {
        init();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword("innis@gmail.com", "Password1!")
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

    @After
    public void tearDown() {
        release();
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

    @Test
    public void testChatButtonLaunchesChatActivity_whenOfferAccepted_withActionOnItem() {
        onView(isRoot()).perform(waitFor(3000));

        // 1. Select "Accepted" in the spinner and click it
        onView(withId(R.id.receivedOffersRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.respondToOfferSpinner)));

        onView(withText("Accepted")).perform(click());

        // 2. Click the chatButton
        onView(withId(R.id.receivedOffersRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.chatButton)));

        // 3. Check if we're redirected to the ChatActivity
        intended(hasComponent(ChatActivity.class.getName()));
    }

    @Test
    public void testChatButtonDisabled_whenOfferNotAccepted() {
        onView(isRoot()).perform(waitFor(3000));

        // 1. Select "Pending" in the spinner and click it
        onView(withId(R.id.receivedOffersRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.respondToOfferSpinner)));

        onView(withText("Pending")).perform(click());

        onView(isRoot()).perform(waitFor(1000));

        // 2. Click the chatButton
        onView(withId(R.id.receivedOffersRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.chatButton)));

        // 3. Check if we stay in the ReceivedOfferActivity
        intended(hasComponent(ReceivedOfferActivity.class.getName()));
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                if (childView != null && childView.isClickable()) {
                    childView.performClick();
                }
            }
        };
    }
}
