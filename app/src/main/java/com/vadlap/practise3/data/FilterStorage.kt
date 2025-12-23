package com.vadlap.practise3.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Расширение для создания DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_settings")

class FilterStorage(private val context: Context) {

    // Ключи для сохранения
    companion object {
        val KEY_TITLE = stringPreferencesKey("filter_title")
        val KEY_TYPE = stringPreferencesKey("filter_type")
        val KEY_YEAR = stringPreferencesKey("filter_year")
    }

    // Чтение настроек как Flow (поток данных)
    val filterFlow: Flow<FilterSettings> = context.dataStore.data
        .map { preferences ->
            FilterSettings(
                title = preferences[KEY_TITLE] ?: "Batman", // Значение по умолчанию
                type = preferences[KEY_TYPE] ?: "",
                year = preferences[KEY_YEAR] ?: ""
            )
        }

    // Сохранение настроек
    suspend fun saveFilters(title: String, type: String, year: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TITLE] = title
            preferences[KEY_TYPE] = type
            preferences[KEY_YEAR] = year
        }
    }
}

// Простая модель для настроек
data class FilterSettings(
    val title: String,
    val type: String,
    val year: String
)
