package com.example.group8_bartertrader.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.group8_bartertrader.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

public class PreferencesManager {
    private static PreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final DatabaseReference databaseReference;

    private static final String KEY_CATEGORIES = "preferred_categories";
    private static final String KEY_LOCATIONS = "preferred_locations";

    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    public void savePreferredCategories(Set<String> categories) {
        // Save to SharedPreferences
        sharedPreferences.edit()
                .putStringSet(KEY_CATEGORIES, categories)
                .apply();

        // Save to Firebase
        savePreferencesToFirebase(categories, KEY_CATEGORIES);
    }
    public void saveFromSearchCriteria(String category, String keyword, String distance, String location) {
        Set<String> categories = new HashSet<>();
        Set<String> locations = new HashSet<>();

        if (category != null && !category.isEmpty()) {
            categories.add(category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            categories.add(keyword);
        }

        if (distance != null && !distance.isEmpty()) {
            locations.add(distance + " km");
        }

        if (location != null && !location.isEmpty()) {
            locations.add(location);
        }

        savePreferredCategories(categories);
        savePreferredLocations(locations);
    }

    public void savePreferredLocations(Set<String> locations) {
        // Save to SharedPreferences
        sharedPreferences.edit()
                .putStringSet(KEY_LOCATIONS, locations)
                .apply();

        // Save to Firebase
        savePreferencesToFirebase(locations, KEY_LOCATIONS);
    }

    private void savePreferencesToFirebase(Set<String> preferences, String preferenceType) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            if (preferenceType.equals(KEY_CATEGORIES)) {
                databaseReference.child(userId).child("preferredCategories").setValue(preferences);
            } else if (preferenceType.equals(KEY_LOCATIONS)) {
                databaseReference.child(userId).child("preferredLocations").setValue(preferences);
            }
        }
    }

    public Set<String> getPreferredCategories() {
        return sharedPreferences.getStringSet(KEY_CATEGORIES, new HashSet<>());
    }

    public Set<String> getPreferredLocations() {
        return sharedPreferences.getStringSet(KEY_LOCATIONS, new HashSet<>());
    }
}
