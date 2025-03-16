package com.example.group8_bartertrader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.example.group8_bartertrader.EspressoUtils.waitFor;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.group8_bartertrader.MyOffersActivity;
import com.example.group8_bartertrader.R;
import com.google.firebase.auth.FirebaseAuth;

@RunWith(AndroidJUnit4.class)
public class MyOffersActivityUITest {

    @Rule
    public ActivityScenarioRule<MyOffersActivity> activityRule =
            new ActivityScenarioRule<>(MyOffersActivity.class);

    @Before
    public void setup() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("testreset@gmail.com", "Password1!")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseTest", "Test user signed in: " + auth.getCurrentUser().getEmail());
                    } else {
                        Log.e("FirebaseTest", "Test sign-in failed: " + task.getException().getMessage());
                    }
                });

        // 确保 Firebase 登录完成
        try {
            Thread.sleep(3000); // 给 Firebase 认证一点时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 启动 MyOffersActivity
        ActivityScenario.launch(MyOffersActivity.class);
    }

    @Test
    public void testGoToSettingsButton() {
        // 确保 "Go to Settings" 按钮存在
        onView(withId(R.id.recsettingbutton)).check(matches(isDisplayed()));

        // 点击 "Go to Settings" 按钮
        onView(withId(R.id.recsettingbutton)).perform(click());

        onView(isRoot()).perform(waitFor(3000));

        // 确保跳转到了 SettingsActivity
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        onView(withId(R.id.activity_settings)) // 假设 SettingsActivity 里有个主布局 ID
                .check(matches(isDisplayed()));
    }

    @Test
    public void testProductItemListDisplayed() {
        // 确保 RecyclerView 存在
        onView(withId(R.id.productRecyclerView))
                .check(matches(isDisplayed()));
    }
}
