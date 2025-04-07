// NotificationHelper.java
package com.example.group8_bartertrader.notification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

public class NotificationHelper {
    private static final String CREDENTIALS_FILE_PATH = "key.json";
    private static final String PUSH_NOTIFICATION_ENDPOINT = "https://fcm.googleapis.com/v1/projects/group-8-barter-trader/messages:send";

    private final RequestQueue requestQueue;
    private final Context context;

    /**
     * helper for notification contructor
     * @param context
     */
    public NotificationHelper(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * sends the notification to the user
     * @param message
     */
    public void sendNotificationToTopic(String message) {
        getAccessToken(new AccessTokenListener() {
            @Override
            public void onAccessTokenReceived(String token) {
                sendNotification(token, message);
            }

            @Override
            public void onAccessTokenError(Exception exception) {
                Log.e("NotificationHelper", "Access token error: " + exception.getMessage());
                Toast.makeText(context, "Failed to get access token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * gets the access token
     * @param listener
     */
    private void getAccessToken(AccessTokenListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try (InputStream serviceAccountStream = context.getAssets().open(CREDENTIALS_FILE_PATH)) {
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
                credentials.refreshIfExpired();
                String token = credentials.getAccessToken().getTokenValue();
                listener.onAccessTokenReceived(token);
            } catch (IOException e) {
                listener.onAccessTokenError(e);
            }
        });
        executorService.shutdown();
    }

    /**
     * sends the notification
     * @param authToken
     * @param notificationMessage
     */
    private void sendNotification(String authToken, String notificationMessage) {
        try {
            JSONObject notification = new JSONObject();
            notification.put("title", "Alert!");
            notification.put("body", notificationMessage);

            JSONObject data = new JSONObject();
            data.put("productLocation", "Halifax");
            data.put("product_id", "HF-128369");

            JSONObject message = new JSONObject();
            message.put("topic", "jobs");
            message.put("notification", notification);
            message.put("data", data);

            JSONObject body = new JSONObject();
            body.put("message", message);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, PUSH_NOTIFICATION_ENDPOINT, body,
                    response -> {
                        Log.d("NotificationHelper", "Notification sent: " + response);
                        Toast.makeText(context, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.e("NotificationHelper", "Notification failed: " + error.toString());
                        Toast.makeText(context, "Failed to send notification", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + authToken);
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("NotificationHelper", "JSON Error: " + e.getMessage());
            Toast.makeText(context, "Failed to build notification", Toast.LENGTH_SHORT).show();
        }
    }

    // Interface for access token callback

    /**
     * access token listener
     */
    interface AccessTokenListener {
        void onAccessTokenReceived(String token);
        void onAccessTokenError(Exception exception);
    }
}
