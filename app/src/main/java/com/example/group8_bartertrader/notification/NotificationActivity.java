package com.example.group8_bartertrader.notification;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.group8_bartertrader.R;
import com.google.firebase.database.Logger;
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

    //path
    private static final String CREDENTIALS_FILE_PATH = "key.json";
    //provided by google - for sending the notification
    //new endpoint
    private static final String PUSH_NOTIFICATION_ENDPOINT ="https://fcm.googleapis.com/v1/projects/group-8-barter-trader/messages:send";

    // for the send notification button
    private Button sendNotificationBtn;

    //provided by volley library to make a network request
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize components and set listeners
        init();
        setListeners();
    }

    private void getAccessToken(Context context, AccessTokenListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                InputStream serviceAccountStream = context.getAssets().open(CREDENTIALS_FILE_PATH);
                GoogleCredentials googleCredentials = GoogleCredentials
                        .fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

                googleCredentials.refreshIfExpired(); // This will refresh the token if it's expired
                String token = googleCredentials.getAccessToken().getTokenValue();
                listener.onAccessTokenReceived(token);
                Log.d("token","token"+token);
            } catch (IOException e) {
                listener.onAccessTokenError(e);
            }
        });
        executorService.shutdown();
    }



    private void init() {

        sendNotificationBtn = findViewById(R.id.sendNotificationBtn);

        //adding multiple network request to a queue, FIFO based, running it separate threads, cannot run network request on the main thread in android
        //volley creates a separate thread for the network request
        requestQueue = Volley.newRequestQueue(this);

        //jobs is the topic name,subscribing to the jobs notification tray
        FirebaseMessaging.getInstance().subscribeToTopic("jobs");
    }

    private void setListeners() {
        sendNotificationBtn.setOnClickListener(view -> {
            // Attempt to get the access token
            getAccessToken(this, new AccessTokenListener() {
                @Override
                public void onAccessTokenReceived(String token) {
                    // When the token is received, send the notification
                    sendNotification(token);
                }

                @Override
                public void onAccessTokenError(Exception exception) {
                    // Handle the error appropriately
                    Toast.makeText(NotificationActivity.this, "Error getting access token: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    exception.printStackTrace();
                }
            });
        });
    }




    private void sendNotification(String authToken) {
        try {
            // Build the notification payload
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

            // Log the complete JSON payload for debugging
            Log.d("NotificationBody", "JSON Body: " + pushNotificationJSONBody.toString());

            // Create the request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    PUSH_NOTIFICATION_ENDPOINT,
                    pushNotificationJSONBody,
                    response -> {
                        Log.d("NotificationResponse", "Response: " + response.toString());
                        Toast.makeText(this, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
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

            // Add the request to the queue
            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e("NotificationJSONException", "Error creating notification JSON: " + e.getMessage());
            Toast.makeText(this, "Error creating notification payload", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
