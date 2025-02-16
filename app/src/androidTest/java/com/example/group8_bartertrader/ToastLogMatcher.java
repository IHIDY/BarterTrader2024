package com.example.group8_bartertrader;

import android.view.WindowManager;
import androidx.test.espresso.Root;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ToastLogMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is a Toast message");
    }

    @Override
    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;

        // Support both TYPE_TOAST (pre-Android 11) and TYPE_APPLICATION_OVERLAY (Android 11+)
        if (type == WindowManager.LayoutParams.TYPE_TOAST ||
                type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {

            // Ensure it's a standalone Toast window
            return root.getDecorView().getWindowToken() == root.getDecorView().getApplicationWindowToken();
        }
        return false;
    }
}
