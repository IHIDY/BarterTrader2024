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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.PreferencesManager;
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
import com.example.group8_bartertrader.notification.NotificationActivity;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
/**
 * ReceiverDash is the main activity that handles the receiver's dashboard functionality.
 * It allows users to view their location, search products, manage notifications,
 * and navigate to settings or their offers.
 * <p>
 * This activity integrates with Firebase to fetch product data and send notifications.
 * It also handles location permissions and manages notification channels for in-app updates.
 * </p>
 */

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
    private int searchRadius;
    private final int RADIUS = 10;
    private final int Radius_E = 6371; // Radius of the Earth in km
    private String selectedCategory;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private double latitude, longitude;
    private boolean notificationsChecked = false;
    private Button savePreferencesButton;
    private boolean isRestarting = false; // Flag to track activity restart

    /**
     * Initializes the ReceiverDash activity. Sets up UI elements, permissions,
     * Firebase database references, and notification handling.
     * <p>
     * It checks and requests necessary permissions, fetches the current location,
     * and sets up the notification channel. It also initializes the search button
     * to navigate to the product search activity.
     * </p>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiver_dash);

        receiverSettingBtn = findViewById(R.id.recsettingbutton);
        myOffersBtn = findViewById(R.id.myOffersButton);
        locationTextView = findViewById(R.id.locationTextView);
        searchButton = findViewById(R.id.search_button);
//        productRecyclerView = findViewById(R.id.productRecyclerView);
//        category = findViewById(R.id.filterSpinner);
//        keyword = findViewById(R.id.inputEditText);
//        Distance = findViewById(R.id.distanceEditText);
//        searchButton = findViewById(R.id.filterButton);

//        productList = new ArrayList<>();
//        productAdapter = new ProductAdapter(productList);
//        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        productRecyclerView.setAdapter(productAdapter);
//

        productsRef = FirebaseDatabase.getInstance().getReference("Products");
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications");

        createNotificationChannel();

        locationHelper = new LocationHelper(this);
        notificationHelper = new NotificationHelper(this);

        checkAndRequestPermissions();

//        savePreferencesButton = findViewById(R.id.savePreferencesButton);
//        savePreferencesButton.setOnClickListener(v -> saveCurrentSearchAsPreferences());

        // Check and request location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
//            locationHelper.getCurrentLocation(this);
            checkNotificationsIfNeeded();
        }

//        fetchProductsFromFirebase(RADIUS,null,"");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupClickListeners();

        receiverSettingBtn.setOnClickListener(v -> {
            Log.d("TEST", "GO TO SETTINGS BUTTON CLICKED");
            startActivity(new Intent(ReceiverDash.this, SettingsActivity.class));
        });

        myOffersBtn.setOnClickListener(v -> {
            Log.d("TEST", "MY OFFERS BUTTON CLICKED");
            startActivity(new Intent(ReceiverDash.this, MyOffersActivity.class));
        });

        searchButton.setOnClickListener(v -> {
            Log.d("TEST", "SEARCH BUTTON CLICKED");
            startActivity(new Intent(ReceiverDash.this, SearchProductActivity.class));
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 2);
            }
        }

        locationHelper.getCurrentLocation(new LocationHelper.OnLocationFetchListener() {
            @Override
            public void onLocationFetched(double latitude, double longitude) {
                // Get the city name from latitude and longitude
                String cityName = LocationHelper.getCityName(latitude, longitude);
                Log.d("<< Current location >>", cityName);

                // Update the TextView with the city name
                locationTextView.setText("Location: " +cityName);
            }

            @Override
            public void onLocationFetchFailed(String errorMessage) {
                // Handle location fetch failure (e.g., show a default message or error message)
                locationTextView.setText("Failed to fetch location: " + errorMessage);
            }
        });
    }
    /**
     * Creates a notification channel to handle notifications in the app.
     * This method is called only if the Android version is Oreo (API 26) or higher.
     * <p>
     * It ensures that trade-related notifications will appear as high-priority notifications.
     * </p>
     */
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
    /**
     * Sets up click listeners for various buttons on the receiver dashboard.
     * It includes listeners for settings, offers, and search buttons.
     */
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
    /**
     * Fetches the current location of the user and updates the UI with the city name.
     * If the location fetch fails, it displays an error message.
     */
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
    /**
     * Handles the result of permission requests. It processes both location and notification permissions.
     */
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

    private void checkAndRequestPermissions() {
        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }
    }
    /**
     * Checks whether notifications should be sent to the user based on their preferences.
     * If notifications are not yet checked, it initiates the notification check process.
     */
    private void checkNotificationsIfNeeded() {
        if (!notificationsChecked) {
            checkAndSendNotifications(getCurrentUserEmail());
            notificationsChecked = true;
        }
    }

    /**
     * Retrieves the current user's email address from Firebase Authentication.
     *
     * @return The email of the current user, or null if no user is logged in.
     */
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getEmail() : null;
    }

    /**
     * Checks the notifications in Firebase for the current user and sends them if applicable.
     * Notifications are sent as high-priority local notifications.
     *
     * @param currentUserEmail The email address of the current user.
     */
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
    /**
     * Sends a high-priority notification with the provided message.
     *
     * @param message The message to display in the notification.
     */
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRestarting = false; // Reset flag when activity is destroyed
    }

    private void saveCurrentSearchAsPreferences() {
        // Get current values with null checks
        String currentCategory = selectedCategory != null ? selectedCategory : "";
        String currentKeyword = keyword.getText() != null ? keyword.getText().toString().trim() : "";
        String currentDistance = Distance.getText() != null ? Distance.getText().toString().trim() : "";
        String currentLocation = locationTextView.getText() != null ?
                locationTextView.getText().toString() : "Location: Not available";

        Set<String> categories = new HashSet<>();
        Set<String> locations = new HashSet<>();

        // Add category if valid
        if (!currentCategory.isEmpty() && !currentCategory.equals("Select Category")) {
            categories.add(currentCategory);
        }

        // Add keyword if not empty
        if (!currentKeyword.isEmpty()) {
            categories.add(currentKeyword);
        }

        // Add location if available
        if (!currentLocation.equals("Location: Not available")) {
            String cleanLocation = currentLocation.replace("Location: ", "").trim();
            if (!cleanLocation.isEmpty()) {
                locations.add(cleanLocation);
            }
        }

        // Add distance if valid
        if (!currentDistance.isEmpty()) {
            try {
                int distance = Integer.parseInt(currentDistance);
                if (distance > 0) {
                    locations.add(currentDistance + " km");
                }
            } catch (NumberFormatException e) {
                Log.e("Preferences", "Invalid distance format", e);
            }
        }

        // Only save if we have at least one preference
        if (!categories.isEmpty() || !locations.isEmpty()) {
            PreferencesManager preferencesManager = PreferencesManager.getInstance(this);
            preferencesManager.savePreferredCategories(categories);
            preferencesManager.savePreferredLocations(locations);

            Toast.makeText(this, "Search preferences saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No preferences to save", Toast.LENGTH_SHORT).show();
        }
    }
}
