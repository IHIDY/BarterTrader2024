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

        /**
         * returns the root
         * @return
         */
        @Override
        public Matcher<View> getConstraints() {
            return isRoot();
        }

        /**
         * description getter
         * @return
         */
        @Override
        public String getDescription() {
            return "Wait for " + millis + " milliseconds.";
        }

        /**
         * performs controller
         * @param uiController the controller to use to interact with the UI.
         * @param view the view to act upon. never null.
         */
        @Override
        public void perform(UiController uiController, View view) {
            uiController.loopMainThreadForAtLeast(millis);
        }
    }
}
