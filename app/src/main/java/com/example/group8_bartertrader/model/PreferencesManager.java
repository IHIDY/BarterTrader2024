package com.example.group8_bartertrader.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.group8_bartertrader.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreferencesManager {
    private static PreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final DatabaseReference databaseReference;

    private static final String KEY_CATEGORIES = "preferred_categories";
    private static final String KEY_LOCATIONS = "preferred_locations";

    public PreferencesManager(Context context) {
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
    private void savePreferencesToFirebase(Set<String> preferences, String preferenceType) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.e("Preferences", "User not authenticated");
            return;
        }

        String userId = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);

        Map<String, Boolean> preferencesMap = new HashMap<>();
        for (String item : preferences) {
            if (item != null && !item.trim().isEmpty()) {
                preferencesMap.put(item.trim(), true);
            }
        }

        String path = preferenceType.equals(KEY_CATEGORIES) ?
                "preferredCategories" : "preferredLocations";

        userRef.child(path).setValue(preferencesMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Preferences", preferenceType + " saved successfully");
                    } else {
                        Log.e("Preferences", "Failed to save " + preferenceType, task.getException());
                    }
                });
    }

    public void savePreferredLocations(Set<String> locations) {
        // Save to SharedPreferences
        sharedPreferences.edit()
                .putStringSet(KEY_LOCATIONS, locations)
                .apply();

        // Save to Firebase
        savePreferencesToFirebase(locations, KEY_LOCATIONS);
    }

    public Set<String> getPreferredCategories() {
        return sharedPreferences.getStringSet(KEY_CATEGORIES, new HashSet<>());
    }

    public Set<String> getPreferredLocations() {
        return sharedPreferences.getStringSet(KEY_LOCATIONS, new HashSet<>());
    }
    public void loadPreferencesFromFirebase(OnPreferencesLoadedListener listener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Log.e("Preferences", "User not authenticated");
            return;
        }

        String userId = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    // Save to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet(KEY_CATEGORIES, user.getPreferredCategoriesSet());
                    editor.putStringSet(KEY_LOCATIONS, user.getPreferredLocationsSet());
                    editor.apply();

                    if (listener != null) {
                        listener.onPreferencesLoaded(
                                user.getPreferredCategoriesSet(),
                                user.getPreferredLocationsSet()
                        );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Preferences", "Failed to load preferences", error.toException());
            }
        });
    }

    public interface OnPreferencesLoadedListener {
        void onPreferencesLoaded(Set<String> categories, Set<String> locations);
    }
}
