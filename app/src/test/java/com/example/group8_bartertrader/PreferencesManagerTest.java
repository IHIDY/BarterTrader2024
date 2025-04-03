package com.example.group8_bartertrader;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.group8_bartertrader.model.PreferencesManager;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class PreferencesManagerTest {

    @Mock
    private Context mockContext;
    @Mock
    private SharedPreferences mockSharedPreferences;
    @Mock
    private SharedPreferences.Editor mockEditor;

    private PreferencesManager preferencesManager;

    @Before
    public void setUp() {
        when(mockContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE))
                .thenReturn(mockSharedPreferences);

        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putStringSet(anyString(), anySet())).thenReturn(mockEditor);

        preferencesManager = new PreferencesManager(mockContext);
    }

    @Test
    public void testSaveAndGetCategories() {
        Set<String> testCategories = new HashSet<>(Arrays.asList("Electronics", "Books"));

        preferencesManager.savePreferredCategories(testCategories);
        verify(mockEditor).putStringSet("preferred_categories", testCategories);
        verify(mockEditor).apply();

        when(mockSharedPreferences.getStringSet("preferred_categories", new HashSet<>()))
                .thenReturn(testCategories);
        Set<String> result = preferencesManager.getPreferredCategories();
        assertEquals(testCategories, result);
    }

    @Test
    public void testSaveAndGetLocations() {
        Set<String> testLocations = new HashSet<>(Arrays.asList("New York", "Boston"));

        preferencesManager.savePreferredLocations(testLocations);
        verify(mockEditor).putStringSet("preferred_locations", testLocations);
        verify(mockEditor).apply();

        when(mockSharedPreferences.getStringSet("preferred_locations", new HashSet<>()))
                .thenReturn(testLocations);
        Set<String> result = preferencesManager.getPreferredLocations();
        assertEquals(testLocations, result);
    }

    @Test
    public void testSaveEmptyCategories() {
        Set<String> emptySet = new HashSet<>();
        preferencesManager.savePreferredCategories(emptySet);
        verify(mockEditor, never()).putStringSet(anyString(), anySet());
    }
}