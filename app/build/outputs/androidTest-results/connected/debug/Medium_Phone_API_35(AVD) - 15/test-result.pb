
ϊ 
u
!ResetPasswordActivityEspressoTestcom.example.group8_bartertradercheckIfPageDisplayed2ηΊ½€τP:ηΊ½€Ϊίχ’
¶java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.Button.setOnClickListener(android.view.View$OnClickListener)' on a null object reference
at com.example.group8_bartertrader.SettingsActivity.onCreate(SettingsActivity.java:47)
at android.app.Activity.performCreate(Activity.java:9002)
at android.app.Activity.performCreate(Activity.java:8980)
at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1526)
at androidx.test.runner.MonitoringInstrumentation.callActivityOnCreate(MonitoringInstrumentation.java:766)
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:4030)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:4235)
at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:112)
at android.app.servertransaction.TransactionExecutor.executeNonLifecycleItem(TransactionExecutor.java:174)
at android.app.servertransaction.TransactionExecutor.executeTransactionItems(TransactionExecutor.java:109)
at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:81)
at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2636)
at android.os.Handler.dispatchMessage(Handler.java:107)
at android.os.Looper.loopOnce(Looper.java:232)
at android.os.Looper.loop(Looper.java:317)
at android.app.ActivityThread.main(ActivityThread.java:8705)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
java.lang.NullPointerException¶java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.Button.setOnClickListener(android.view.View$OnClickListener)' on a null object reference
at com.example.group8_bartertrader.SettingsActivity.onCreate(SettingsActivity.java:47)
at android.app.Activity.performCreate(Activity.java:9002)
at android.app.Activity.performCreate(Activity.java:8980)
at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1526)
at androidx.test.runner.MonitoringInstrumentation.callActivityOnCreate(MonitoringInstrumentation.java:766)
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:4030)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:4235)
at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:112)
at android.app.servertransaction.TransactionExecutor.executeNonLifecycleItem(TransactionExecutor.java:174)
at android.app.servertransaction.TransactionExecutor.executeTransactionItems(TransactionExecutor.java:109)
at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:81)
at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2636)
at android.os.Handler.dispatchMessage(Handler.java:107)
at android.os.Looper.loopOnce(Looper.java:232)
at android.os.Looper.loop(Looper.java:317)
at android.app.ActivityThread.main(ActivityThread.java:8705)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
"’

logcatandroidό
ωC:\Users\William\Desktop\csci3130\project2\group_8_csci_3130_winter\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\logcat-com.example.group8_bartertrader.ResetPasswordActivityEspressoTest-checkIfPageDisplayed.txt"Δ

device-infoandroid©
¦C:\Users\William\Desktop\csci3130\project2\group_8_csci_3130_winter\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\device-info.pb"Ε

device-info.meminfoandroidΆ
C:\Users\William\Desktop\csci3130\project2\group_8_csci_3130_winter\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\meminfo"Ε

device-info.cpuinfoandroidΆ
C:\Users\William\Desktop\csci3130\project2\group_8_csci_3130_winter\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\cpuinfo*©
c
test-results.logOcom.google.testing.platform.runtime.android.driver.AndroidInstrumentationDriver³
°C:\Users\William\Desktop\csci3130\project2\group_8_csci_3130_winter\app\build\outputs\androidTest-results\connected\debug\Medium_Phone_API_35(AVD) - 15\testlog\test-results.log 2
text/plain2ΐ
QOcom.google.testing.platform.runtime.android.driver.AndroidInstrumentationDriver"INSTRUMENTATION_FAILED*OTest run failed to complete. Instrumentation run failed due to Process crashed.2ή*ΩLogcat of last crash: 
Process: com.example.group8_bartertrader, PID: 27876
java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.group8_bartertrader/com.example.group8_bartertrader.SettingsActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.Button.setOnClickListener(android.view.View$OnClickListener)' on a null object reference
	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:4048)
	at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:4235)
	at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:112)
	at android.app.servertransaction.TransactionExecutor.executeNonLifecycleItem(TransactionExecutor.java:174)
	at android.app.servertransaction.TransactionExecutor.executeTransactionItems(TransactionExecutor.java:109)
	at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:81)
	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2636)
	at android.os.Handler.dispatchMessage(Handler.java:107)
	at android.os.Looper.loopOnce(Looper.java:232)
	at android.os.Looper.loop(Looper.java:317)
	at android.app.ActivityThread.main(ActivityThread.java:8705)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.Button.setOnClickListener(android.view.View$OnClickListener)' on a null object reference
	at com.example.group8_bartertrader.SettingsActivity.onCreate(SettingsActivity.java:47)
	at android.app.Activity.performCreate(Activity.java:9002)
	at android.app.Activity.performCreate(Activity.java:8980)
	at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1526)
	at androidx.test.runner.MonitoringInstrumentation.callActivityOnCreate(MonitoringInstrumentation.java:766)
	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:4030)
	... 13 more
