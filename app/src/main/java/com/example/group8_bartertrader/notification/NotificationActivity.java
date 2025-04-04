package com.example.group8_bartertrader.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.group8_bartertrader.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.auth.oauth2.GoogleCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "BARTER_TRADER_CHANNEL";
    private static final String CREDENTIALS_FILE_PATH = "key.json";
    private static final String PUSH_NOTIFICATION_ENDPOINT = "https://fcm.googleapis.com/v1/projects/group-8-barter-trader/messages:send";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    private Button sendNotificationBtn;
    private RequestQueue requestQueue;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rootView = findViewById(android.R.id.content);

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel();

        // Check and request notification permission (required for Android 13+)
        checkNotificationPermission();

        init();
        setListeners();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Barter Trader Notifications";
            String description = "Channel for trade notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH; // This ensures pop-up notifications

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    private void init() {
        sendNotificationBtn = findViewById(R.id.sendNotificationBtn);
        requestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");
    }

    private void setListeners() {
        sendNotificationBtn.setOnClickListener(view -> {
            getAccessToken(this, new AccessTokenListener() {
                @Override
                public void onAccessTokenReceived(String token) {
                    sendNotification(token);
                    // Also show a local notification to verify it works
                    showLocalNotification();
                }

                @Override
                public void onAccessTokenError(Exception exception) {
                    Toast.makeText(NotificationActivity.this,
                            "Error getting access token: " + exception.getMessage(),
                            Toast.LENGTH_LONG).show();
                    exception.printStackTrace();
                }
            });
        });
    }

    private void showLocalNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Make sure you have this icon
                .setContentTitle("Local Test Notification")
                .setContentText("This is a local notification test")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
            Log.d("Notification", "Local notification shown");
        }
    }

    private void getAccessToken(Context context, AccessTokenListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                InputStream serviceAccountStream = context.getAssets().open(CREDENTIALS_FILE_PATH);
                GoogleCredentials googleCredentials = GoogleCredentials
                        .fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

                googleCredentials.refreshIfExpired();
                String token = googleCredentials.getAccessToken().getTokenValue();
                listener.onAccessTokenReceived(token);
                Log.d("Token", "Token: " + token);
            } catch (IOException e) {
                listener.onAccessTokenError(e);
            }
        });
        executorService.shutdown();
    }

    private void sendNotification(String authToken) {
        try {
            JSONObject notificationJSONBody = new JSONObject();
            notificationJSONBody.put("title", "New Job Created");
            notificationJSONBody.put("body", "A new job is created in your city.");

            JSONObject dataJSONBody = new JSONObject();
            dataJSONBody.put("productLocation", "Halifax");
            dataJSONBody.put("product_id", "HF-128369");

            JSONObject messageJSONBody = new JSONObject();
            messageJSONBody.put("topic", "jobs");
            messageJSONBody.put("notification", notificationJSONBody);
            messageJSONBody.put("data", dataJSONBody);

            JSONObject pushNotificationJSONBody = new JSONObject();
            pushNotificationJSONBody.put("message", messageJSONBody);

            Log.d("NotificationBody", "JSON Body: " + pushNotificationJSONBody.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response -> {
                        Log.d("NotificationResponse", "Response: " + response.toString());
                        Snackbar.make(rootView, "Notification Sent Successfully", Snackbar.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.e("NotificationError", "Error Response: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("NotificationError", "Status Code: " + error.networkResponse.statusCode);
                            Log.e("NotificationError", "Error Data: " + new String(error.networkResponse.data));
                        }
                        Toast.makeText(this, "Failed to Send Notification", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + authToken);
                    Log.d("NotificationHeaders", "Headers: " + headers.toString());
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("NotificationJSONException", "Error creating notification JSON: " + e.getMessage());
            Toast.makeText(this, "Error creating notification payload", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    interface AccessTokenListener {
        void onAccessTokenReceived(String token);
        void onAccessTokenError(Exception exception);
    }
}