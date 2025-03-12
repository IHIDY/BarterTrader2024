package com.example.group8_bartertrader;

import android.view.View;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

public class EspressoUtils {

    public static ViewAction waitFor(final long millis) {
        return new EspressoWaitAction(millis);
    }

    private static class EspressoWaitAction implements ViewAction {
        private final long millis;

        EspressoWaitAction(long millis) {
            this.millis = millis;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isRoot();
        }

        @Override
        public String getDescription() {
            return "Wait for " + millis + " milliseconds.";
        }

        @Override
        public void perform(UiController uiController, View view) {
            uiController.loopMainThreadForAtLeast(millis);
        }
    }
}
