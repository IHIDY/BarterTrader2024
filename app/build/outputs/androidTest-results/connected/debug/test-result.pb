
ÜB
h
SetPreferencesTestcom.example.group8_bartertradertestPreferencesDisplay2¡Õºø¿œ†•:∆ÕºøÄê°ò;
∂androidx.test.espresso.base.AssertionErrorHandler$AssertionFailedWithCauseError: 'an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"' doesn't match the selected view.
Expected: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"
Got: view.getText() was "Loading preferences..." transformed text was "Loading preferences..."
View Details: MaterialTextView{id=2131296665, res-name=preferencesSummary, visibility=VISIBLE, width=996, height=57, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=androidx.constraintlayout.widget.ConstraintLayout$LayoutParams@YYYYYY, tag=null, root-is-layout-requested=false, has-input-connection=false, x=42.0, y=252.0, text=Loading preferences..., input-type=0, ime-target=false, has-links=false}

at dalvik.system.VMStack.getThreadStackTrace(Native Method)
at java.lang.Thread.getStackTrace(Thread.java:1841)
at androidx.test.espresso.base.AssertionErrorHandler.handleSafely(AssertionErrorHandler.java:35)
at androidx.test.espresso.base.AssertionErrorHandler.handleSafely(AssertionErrorHandler.java:26)
at androidx.test.espresso.base.DefaultFailureHandler$TypedFailureHandler.handle(DefaultFailureHandler.java:158)
at androidx.test.espresso.base.DefaultFailureHandler.handle(DefaultFailureHandler.java:120)
at androidx.test.espresso.ViewInteraction.waitForAndHandleInteractionResults(ViewInteraction.java:387)
at androidx.test.espresso.ViewInteraction.check(ViewInteraction.java:366)
at com.example.group8_bartertrader.SetPreferencesTest.testPreferencesDisplay(SetPreferencesTest.java:53)
... 34 trimmed
Caused by: junit.framework.AssertionFailedError: 'an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"' doesn't match the selected view.
Expected: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"
Got: view.getText() was "Loading preferences..." transformed text was "Loading preferences..."
View Details: MaterialTextView{id=2131296665, res-name=preferencesSummary, visibility=VISIBLE, width=996, height=57, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=androidx.constraintlayout.widget.ConstraintLayout$LayoutParams@YYYYYY, tag=null, root-is-layout-requested=false, has-input-connection=false, x=42.0, y=252.0, text=Loading preferences..., input-type=0, ime-target=false, has-links=false}

at androidx.test.espresso.matcher.ViewMatchers.assertThat(ViewMatchers.java:620)
at androidx.test.espresso.assertion.ViewAssertions$MatchesViewAssertion.check(ViewAssertions.java:97)
at androidx.test.espresso.ViewInteraction$SingleExecutionViewAssertion.check(ViewInteraction.java:488)
at androidx.test.espresso.ViewInteraction$2.call(ViewInteraction.java:346)
at androidx.test.espresso.ViewInteraction$2.call(ViewInteraction.java:319)
at java.util.concurrent.FutureTask.run(FutureTask.java:264)
at android.os.Handler.handleCallback(Handler.java:959)
at android.os.Handler.dispatchMessage(Handler.java:100)
at android.os.Looper.loopOnce(Looper.java:232)
at android.os.Looper.loop(Looper.java:317)
at android.app.ActivityThread.main(ActivityThread.java:8705)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
$junit.framework.AssertionFailedError∂androidx.test.espresso.base.AssertionErrorHandler$AssertionFailedWithCauseError: 'an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"' doesn't match the selected view.
Expected: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"
Got: view.getText() was "Loading preferences..." transformed text was "Loading preferences..."
View Details: MaterialTextView{id=2131296665, res-name=preferencesSummary, visibility=VISIBLE, width=996, height=57, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=androidx.constraintlayout.widget.ConstraintLayout$LayoutParams@YYYYYY, tag=null, root-is-layout-requested=false, has-input-connection=false, x=42.0, y=252.0, text=Loading preferences..., input-type=0, ime-target=false, has-links=false}

at dalvik.system.VMStack.getThreadStackTrace(Native Method)
at java.lang.Thread.getStackTrace(Thread.java:1841)
at androidx.test.espresso.base.AssertionErrorHandler.handleSafely(AssertionErrorHandler.java:35)
at androidx.test.espresso.base.AssertionErrorHandler.handleSafely(AssertionErrorHandler.java:26)
at androidx.test.espresso.base.DefaultFailureHandler$TypedFailureHandler.handle(DefaultFailureHandler.java:158)
at androidx.test.espresso.base.DefaultFailureHandler.handle(DefaultFailureHandler.java:120)
at androidx.test.espresso.ViewInteraction.waitForAndHandleInteractionResults(ViewInteraction.java:387)
at androidx.test.espresso.ViewInteraction.check(ViewInteraction.java:366)
at com.example.group8_bartertrader.SetPreferencesTest.testPreferencesDisplay(SetPreferencesTest.java:53)
... 34 trimmed
Caused by: junit.framework.AssertionFailedError: 'an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"' doesn't match the selected view.
Expected: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "No preferences set"
Got: view.getText() was "Loading preferences..." transformed text was "Loading preferences..."
View Details: MaterialTextView{id=2131296665, res-name=preferencesSummary, visibility=VISIBLE, width=996, height=57, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=androidx.constraintlayout.widget.ConstraintLayout$LayoutParams@YYYYYY, tag=null, root-is-layout-requested=false, has-input-connection=false, x=42.0, y=252.0, text=Loading preferences..., input-type=0, ime-target=false, has-links=false}

at androidx.test.espresso.matcher.ViewMatchers.assertThat(ViewMatchers.java:620)
at androidx.test.espresso.assertion.ViewAssertions$MatchesViewAssertion.check(ViewAssertions.java:97)
at androidx.test.espresso.ViewInteraction$SingleExecutionViewAssertion.check(ViewInteraction.java:488)
at androidx.test.espresso.ViewInteraction$2.call(ViewInteraction.java:346)
at androidx.test.espresso.ViewInteraction$2.call(ViewInteraction.java:319)
at java.util.concurrent.FutureTask.run(FutureTask.java:264)
at android.os.Handler.handleCallback(Handler.java:959)
at android.os.Handler.dispatchMessage(Handler.java:100)
at android.os.Looper.loopOnce(Looper.java:232)
at android.os.Looper.loop(Looper.java:317)
at android.app.ActivityThread.main(ActivityThread.java:8705)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
"Ì

logcatandroid◊
‘C:\Users\ellaj\CSCI3130\group8_bartertrader\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\logcat-com.example.group8_bartertrader.SetPreferencesTest-testPreferencesDisplay.txt"¨

device-infoandroidë
éC:\Users\ellaj\CSCI3130\group8_bartertrader\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\device-info.pb"≠

device-info.meminfoandroidä
áC:\Users\ellaj\CSCI3130\group8_bartertrader\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\meminfo"≠

device-info.cpuinfoandroidä
áC:\Users\ellaj\CSCI3130\group8_bartertrader\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\cpuinfo" *ë
c
test-results.logOcom.google.testing.platform.runtime.android.driver.AndroidInstrumentationDriverõ
òC:\Users\ellaj\CSCI3130\group8_bartertrader\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\testlog\test-results.log 2
text/plain