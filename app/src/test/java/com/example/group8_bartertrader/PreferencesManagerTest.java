package com.example.group8_bartertrader;

import static com.google.common.base.Verify.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
    private SharedPreferences mockSharedPreferences;
    @Mock
    private SharedPreferences.Editor mockEditor;

    private PreferencesManager preferencesManager;

    @Before
    public void setUp() {
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putStringSet(anyString(), anySet())).thenReturn(mockEditor);

        preferencesManager = new PreferencesManager(mockSharedPreferences);
    }

    @Test
    public void testSaveCategories() {
        Set<String> categories = new HashSet<>(Arrays.asList("Electronics", "Furniture"));
        preferencesManager.savePreferredCategories(categories);

        verify(mockEditor).putStringSet("preferred_categories", categories);
        verify(mockEditor).apply();
    }

    @Test
    public void testGetCategories() {
        Set<String> expected = new HashSet<>(Arrays.asList("Electronics", "Furniture"));
        when(mockSharedPreferences.getStringSet("preferred_categories", new HashSet<>()))
                .thenReturn(expected);

        Set<String> actual = preferencesManager.getPreferredCategories();
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveLocations() {
        Set<String> locations = new HashSet<>(Arrays.asList("New York", "Boston"));
        preferencesManager.savePreferredLocations(locations);

        verify(mockEditor).putStringSet("preferred_locations", locations);
        verify(mockEditor).apply();
    }
}
