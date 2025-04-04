package com.example.group8_bartertrader.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.group8_bartertrader.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
//class is for showing the notifications on the very top
//firebase messaging service from the build.gradle file
//runs in the background
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //creating a token, registering with the firebase messaging service
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM Token", "Token: " + token);
    }

    //main method
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("message received","received"+message);
        // If the notification message received is null, return. safety check
        if (message.getNotification() == null) {
            return;
        }

        // Extract fields from the notification message.
        final String title = message.getNotification().getTitle();
        final String body = message.getNotification().getBody();

        //getting the data
        final Map<String, String> data = message.getData();
        Log.d("NotificationReceived", "Title: " + title + ", Body: " + body + ", Data: " + data);

        final String productId = data.get("product_id");
        final String productLocation = data.get("productLocation");

        // Create an intent to start activity when the notification is clicked.
        Intent intent = new Intent(this, ViewPushNotificationActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("product_id", productId);
        intent.putExtra("productLocation", productLocation);
        //based on the flag, the notification will be displayed
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 10, intent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        // Create a notification that will be displayed in the notification tray.
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "jobs")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Add the intent to the notification.
        notificationBuilder.setContentIntent(pendingIntent);

        // Notification manager to display the notification.
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        if (notificationManager == null) {
            Log.e("NotificationError", "NotificationManager is null.");
            return;
        }

        // If the build version is greater than, put the notification in a channel.
        //grouping the notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("jobs", "jobs", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Display the push notification.
        notificationManager.notify(id, notificationBuilder.build());
    }
}
