package com.example.group8_bartertrader.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PreferencesManager {
    private static PreferencesManager instance;
    private final SharedPreferences sharedPreferences;

    private static final String KEY_CATEGORIES = "preferred_categories";
    private static final String KEY_LOCATIONS = "preferred_locations";

    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    public void savePreferredCategories(Set<String> categories) {
        sharedPreferences.edit()
                .putStringSet(KEY_CATEGORIES, categories)
                .apply();
    }

    public void savePreferredLocations(Set<String> locations) {
        sharedPreferences.edit()
                .putStringSet(KEY_LOCATIONS, locations)
                .apply();
    }

    public Set<String> getPreferredCategories() {
        return sharedPreferences.getStringSet(KEY_CATEGORIES, new HashSet<>());
    }

    public Set<String> getPreferredLocations() {
        return sharedPreferences.getStringSet(KEY_LOCATIONS, new HashSet<>());
    }
}
