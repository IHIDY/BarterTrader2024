package com.example.group8_bartertrader;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.Product;
import com.example.group8_bartertrader.notification.NotificationHelper;
import com.example.group8_bartertrader.utils.LocationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReceiverDash extends AppCompatActivity {
    private static final String CHANNEL_ID = "BARTER_TRADER_CHANNEL";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 2;

    private Button receiverSettingBtn;
    private Button myOffersBtn;
    private TextView locationTextView;
    private LocationHelper locationHelper;
    private NotificationHelper notificationHelper;
    private DatabaseReference productsRef, notificationsRef;
    private List<Product> productList;
    private Spinner category;
    private Button searchButton;
    private EditText keyword,Distance;
    private boolean notificationsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiver_dash);

        // Initialize UI components
        receiverSettingBtn = findViewById(R.id.recsettingbutton);
        myOffersBtn = findViewById(R.id.myOffersButton);
        locationTextView = findViewById(R.id.locationTextView);
        searchButton = findViewById(R.id.search_button);

        // Initialize Firebase references
        productsRef = FirebaseDatabase.getInstance().getReference("Products");
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications");

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel();

        // Initialize helpers
        locationHelper = new LocationHelper(this);
        notificationHelper = new NotificationHelper(this);

        // Check and request permissions
        checkAndRequestPermissions();

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listeners
        setupClickListeners();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Barter Trader Notifications";
            String description = "Channel for trade notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Ensures pop-up notifications

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkAndRequestPermissions() {
        // Check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            getCurrentLocation();
        }

        // Check notification permission for Android 13+
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

    private void setupClickListeners() {
        receiverSettingBtn.setOnClickListener(v -> {
            startActivity(new Intent(ReceiverDash.this, SettingsActivity.class));
        });

        myOffersBtn.setOnClickListener(v -> {
            startActivity(new Intent(ReceiverDash.this, MyOffersActivity.class));
        });

        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(ReceiverDash.this, SearchProductActivity.class));
        });
    }

    private void getCurrentLocation() {
        locationHelper.getCurrentLocation(new LocationHelper.OnLocationFetchListener() {
            @Override
            public void onLocationFetched(double latitude, double longitude) {
                String cityName = LocationHelper.getCityName(latitude, longitude);
                locationTextView.setText("Location: " + cityName);
                checkNotificationsIfNeeded();
            }

            @Override
            public void onLocationFetchFailed(String errorMessage) {
                locationTextView.setText("Location unavailable");
                Log.e("LocationError", errorMessage);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNotificationsIfNeeded();
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkNotificationsIfNeeded() {
        if (!notificationsChecked) {
            checkAndSendNotifications(getCurrentUserEmail());
            notificationsChecked = true;
        }
    }

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getEmail() : null;
    }

    private void checkAndSendNotifications(String currentUserEmail) {
        if (currentUserEmail == null) return;

        notificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String receiverEmail = childSnapshot.child("receiverEmail").getValue(String.class);
                    String message = childSnapshot.child("message").getValue(String.class);

                    if (receiverEmail != null && receiverEmail.equalsIgnoreCase(currentUserEmail)) {
                        sendHighPriorityNotification(message);
                        childSnapshot.getRef().removeValue(); // Clean up after sending
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

    private void sendHighPriorityNotification(String message) {
        // First try sending via NotificationHelper
//        notificationHelper.sendNotificationToTopic(message);

        // Then send a local high-priority notification to ensure it appears
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New Product Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any resources if needed
    }
}