"�g�g
�4
com.google.testing.platform�PLUGIN_ERROR"TEST*�Exception thrown during onBeforeAll invocation of plugin AndroidTestApkInstallerPlugin: ErrorName: UNKNOWN
NameSpace: DdmlibAndroidDeviceController
ErrorCode: 1
ErrorType: TEST
Message: Failed to install split APK(s): [/Users/jae/Desktop/W25/3130/group8/app/build/intermediates/apk/debug/app-debug.apk]:�1com.google.testing.platform.core.error.UtpException: ErrorName: PLUGIN_ERROR
NameSpace: com.google.testing.platform
ErrorCode: 2002
ErrorType: TEST
Message: Exception thrown during onBeforeAll invocation of plugin AndroidTestApkInstallerPlugin: ErrorName: UNKNOWN
NameSpace: DdmlibAndroidDeviceController
ErrorCode: 1
ErrorType: TEST
Message: Failed to install split APK(s): [/Users/jae/Desktop/W25/3130/group8/app/build/intermediates/apk/debug/app-debug.apk]
	at com.google.testing.platform.plugin.PluginLifecycleKt.invokeOrThrow(PluginLifecycle.kt:547)
	at com.google.testing.platform.plugin.PluginLifecycleKt.invokeOrThrow$default(PluginLifecycle.kt:517)
	at com.google.testing.platform.plugin.PluginLifecycle$onBeforeAll$1$1.invoke(PluginLifecycle.kt:205)
	at com.google.testing.platform.plugin.PluginLifecycle$onBeforeAll$1$1.invoke(PluginLifecycle.kt:199)
	at com.google.testing.platform.core.telemetry.common.noop.NoopDiagnosticsScope.recordEvent(NoopDiagnosticsScope.kt:35)
	at com.google.testing.platform.core.telemetry.SequentialEventRecordRequest.record$java_com_google_testing_platform_core_telemetry_telemetry_api(EventRecordRequest.kt:71)
	at com.google.testing.platform.core.telemetry.DiagnosticsExtKt.record(DiagnosticsExt.kt:27)
	at com.google.testing.platform.core.telemetry.TelemetryKt.createEvent(Telemetry.kt:60)
	at com.google.testing.platform.plugin.PluginLifecycle.onBeforeAll(PluginLifecycle.kt:197)
	at com.google.testing.platform.executor.SingleDeviceExecutor$execute$5.invoke(SingleDeviceExecutor.kt:136)
	at com.google.testing.platform.executor.SingleDeviceExecutor$execute$5.invoke(SingleDeviceExecutor.kt:136)
	at com.google.testing.platform.lib.cancellation.ProcessCancellationContext.runUnlessCancelled(ProcessCancellationContext.kt:136)
	at com.google.testing.platform.executor.SingleDeviceExecutor.execute(SingleDeviceExecutor.kt:136)
	at com.google.testing.platform.RunnerImpl.run(RunnerImpl.kt:121)
	at com.google.testing.platform.server.strategy.NonInteractiveServerStrategy$run$4$2.invoke(NonInteractiveServerStrategy.kt:98)
	at com.google.testing.platform.server.strategy.NonInteractiveServerStrategy$run$4$2.invoke(NonInteractiveServerStrategy.kt:98)
	at com.google.testing.platform.core.telemetry.common.noop.NoopDiagnosticsScope.recordEvent(NoopDiagnosticsScope.kt:35)
	at com.google.testing.platform.core.telemetry.SequentialEventRecordRequest.record$java_com_google_testing_platform_core_telemetry_telemetry_api(EventRecordRequest.kt:71)
	at com.google.testing.platform.core.telemetry.DiagnosticsExtKt.record(DiagnosticsExt.kt:27)
	at com.google.testing.platform.core.telemetry.TelemetryKt.createEvent(Telemetry.kt:60)
	at com.google.testing.platform.server.strategy.NonInteractiveServerStrategy.run(NonInteractiveServerStrategy.kt:95)
	at com.google.testing.platform.main.MainKt$main$4.invokeSuspend(Main.kt:75)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:284)
	at kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:85)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:59)
	at kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:38)
	at kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
	at com.google.testing.platform.main.MainKt.main(Main.kt:73)
	at com.google.testing.platform.main.MainKt.main$default(Main.kt:35)
	at com.google.testing.platform.main.MainKt.main(Main.kt)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(Unknown Source)
	at java.base/java.lang.reflect.Method.invoke(Unknown Source)
	at com.google.testing.platform.launcher.Launcher.main(Launcher.java:154)
Caused by: com.google.testing.platform.core.error.UtpException: ErrorName: UNKNOWN
NameSpace: DdmlibAndroidDeviceController
ErrorCode: 1
ErrorType: TEST
Message: Failed to install split APK(s): [/Users/jae/Desktop/W25/3130/group8/app/build/intermediates/apk/debug/app-debug.apk]
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDeviceController$executeAsync$deferred$1.invokeSuspend(DdmlibAndroidDeviceController.kt:290)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:570)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:749)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:677)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:664)
Caused by: com.android.ddmlib.InstallException
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:94)
	at com.android.ddmlib.IDeviceSharedImpl.installPackages(IDeviceSharedImpl.java:395)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$34(DeviceImpl.java:1491)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1488)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$35(DeviceImpl.java:1503)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1499)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDevice.installPackages(DdmlibAndroidDevice.kt:74)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDeviceController$executeAsync$deferred$1.invokeSuspend(DdmlibAndroidDeviceController.kt:259)
	... 6 more
Caused by: com.android.ddmlib.ShellCommandUnresponsiveException
	at com.android.ddmlib.internal.DeviceImpl.lambda$executeRemoteCommand$18(DeviceImpl.java:891)
	at com.android.ddmlib.internal.DeviceImpl.logRun1(DeviceImpl.java:1801)
	at com.android.ddmlib.internal.DeviceImpl.executeRemoteCommand(DeviceImpl.java:755)
	at com.android.ddmlib.SplitApkInstallerBase.installCommit(SplitApkInstallerBase.java:154)
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:85)
	... 15 more
�2
�
DdmlibAndroidDeviceControllerUNKNOWN"TEST*tFailed to install split APK(s): [/Users/jae/Desktop/W25/3130/group8/app/build/intermediates/apk/debug/app-debug.apk]:�com.google.testing.platform.core.error.UtpException: ErrorName: UNKNOWN
NameSpace: DdmlibAndroidDeviceController
ErrorCode: 1
ErrorType: TEST
Message: Failed to install split APK(s): [/Users/jae/Desktop/W25/3130/group8/app/build/intermediates/apk/debug/app-debug.apk]
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDeviceController$executeAsync$deferred$1.invokeSuspend(DdmlibAndroidDeviceController.kt:290)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:570)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:749)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:677)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:664)
Caused by: com.android.ddmlib.InstallException
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:94)
	at com.android.ddmlib.IDeviceSharedImpl.installPackages(IDeviceSharedImpl.java:395)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$34(DeviceImpl.java:1491)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1488)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$35(DeviceImpl.java:1503)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1499)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDevice.installPackages(DdmlibAndroidDevice.kt:74)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDeviceController$executeAsync$deferred$1.invokeSuspend(DdmlibAndroidDeviceController.kt:259)
	... 6 more
Caused by: com.android.ddmlib.ShellCommandUnresponsiveException
	at com.android.ddmlib.internal.DeviceImpl.lambda$executeRemoteCommand$18(DeviceImpl.java:891)
	at com.android.ddmlib.internal.DeviceImpl.logRun1(DeviceImpl.java:1801)
	at com.android.ddmlib.internal.DeviceImpl.executeRemoteCommand(DeviceImpl.java:755)
	at com.android.ddmlib.SplitApkInstallerBase.installCommit(SplitApkInstallerBase.java:154)
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:85)
	... 15 more
�
�:�com.android.ddmlib.InstallException
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:94)
	at com.android.ddmlib.IDeviceSharedImpl.installPackages(IDeviceSharedImpl.java:395)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$34(DeviceImpl.java:1491)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1488)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$35(DeviceImpl.java:1503)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1499)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDevice.installPackages(DdmlibAndroidDevice.kt:74)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDeviceController$executeAsync$deferred$1.invokeSuspend(DdmlibAndroidDeviceController.kt:259)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:570)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:749)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:677)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:664)
Caused by: com.android.ddmlib.ShellCommandUnresponsiveException
	at com.android.ddmlib.internal.DeviceImpl.lambda$executeRemoteCommand$18(DeviceImpl.java:891)
	at com.android.ddmlib.internal.DeviceImpl.logRun1(DeviceImpl.java:1801)
	at com.android.ddmlib.internal.DeviceImpl.executeRemoteCommand(DeviceImpl.java:755)
	at com.android.ddmlib.SplitApkInstallerBase.installCommit(SplitApkInstallerBase.java:154)
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:85)
	... 15 more
�
�:�com.android.ddmlib.ShellCommandUnresponsiveException
	at com.android.ddmlib.internal.DeviceImpl.lambda$executeRemoteCommand$18(DeviceImpl.java:891)
	at com.android.ddmlib.internal.DeviceImpl.logRun1(DeviceImpl.java:1801)
	at com.android.ddmlib.internal.DeviceImpl.executeRemoteCommand(DeviceImpl.java:755)
	at com.android.ddmlib.SplitApkInstallerBase.installCommit(SplitApkInstallerBase.java:154)
	at com.android.ddmlib.SplitApkInstaller.install(SplitApkInstaller.java:85)
	at com.android.ddmlib.IDeviceSharedImpl.installPackages(IDeviceSharedImpl.java:395)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$34(DeviceImpl.java:1491)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1488)
	at com.android.ddmlib.internal.DeviceImpl.lambda$installPackages$35(DeviceImpl.java:1503)
	at com.android.ddmlib.internal.DeviceImpl.logRun3(DeviceImpl.java:1833)
	at com.android.ddmlib.internal.DeviceImpl.installPackages(DeviceImpl.java:1499)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDevice.installPackages(DdmlibAndroidDevice.kt:74)
	at com.android.tools.utp.plugins.deviceprovider.ddmlib.DdmlibAndroidDeviceController$executeAsync$deferred$1.invokeSuspend(DdmlibAndroidDeviceController.kt:259)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:570)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:749)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:677)
	at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:664)
