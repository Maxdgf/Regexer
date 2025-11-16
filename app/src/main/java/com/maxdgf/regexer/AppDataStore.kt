package com.maxdgf.regexer

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "Regexer_preferences")

val currentSelectionMatchesColor = longPreferencesKey(name = "current_selection_matches_color")