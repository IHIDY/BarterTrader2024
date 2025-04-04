package com.example.group8_bartertrader;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group8_bartertrader.adapter.ProductAdapter;
import com.example.group8_bartertrader.model.PreferencesManager;
import com.example.group8_bartertrader.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.group8_bartertrader.notification.NotificationHelper;
import com.example.group8_bartertrader.utils.LocationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.group8_bartertrader.notification.NotificationActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReceiverDash extends AppCompatActivity {

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
    private int distance;
    private final int RADIUS = 10;
    private final int Radius_E = 6371; // Radius of the Earth in km
    private String selectedCategory;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private double latitude, longitude;
    private Button savePreferencesButton;

    private boolean notificationsChecked = false;
    private boolean isRestarting = false; // Flag to track activity restart

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

        locationHelper = new LocationHelper(this);
        notificationHelper = new NotificationHelper(this);

        savePreferencesButton = findViewById(R.id.savePreferencesButton);
        savePreferencesButton.setOnClickListener(v -> saveCurrentSearchAsPreferences());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Not logged in, redirect to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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

    private void checkNotificationsIfNeeded() {
        if (!notificationsChecked) {
            checkAndSendNotifications(getCurrentUserEmail());
            notificationsChecked = true;
        }
    }

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            return currentUser.getEmail();
        } else {
            Log.w("UserEmailCheck", "No user is signed in or email is null");
            return null;
        }
    }

    public void checkAndSendNotifications(String currentUserEmail) {
        if (currentUserEmail == null || notificationsChecked) {
            return;
        }

        notificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String receiverEmail = childSnapshot.child("receiverEmail").getValue(String.class);
                    String message = childSnapshot.child("message").getValue(String.class);

                    if (receiverEmail != null && receiverEmail.equalsIgnoreCase(currentUserEmail)) {
                        // Send the notification
                        notificationHelper.sendNotificationToTopic(message);

                        // Delete the notification after sending it
                        childSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("NotificationCleanup", "Notification deleted after sending.");
                            } else {
                                Log.e("NotificationCleanup", "Failed to delete notification: " + task.getException());
                            }
                        });
                    }
                }
                notificationsChecked = true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error reading notifications: " + error.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Only restart if we're not already in the process of restarting
        if (!isRestarting) {
            isRestarting = true;

            // Check if permissions were granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNotificationsIfNeeded();
            }

            // Restart the activity only once
            Intent intent = getIntent();
            finish();
            startActivity(intent);
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


// Call this in onCreate and onResume
}