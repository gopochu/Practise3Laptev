package com.vadlap.practise3.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

data class ProfileData(
    val name: String,
    val avatarUri: String,
    val resumeUrl: String,
    val classTime: String
)

class ProfileStorage(private val context: Context) {

    companion object {
        val KEY_NAME = stringPreferencesKey("profile_name")
        val KEY_AVATAR_URI = stringPreferencesKey("profile_avatar_uri")
        val KEY_RESUME_URL = stringPreferencesKey("profile_resume_url")
        val KEY_CLASS_TIME = stringPreferencesKey("profile_class_time") // Ключ для времени
    }

    val profileFlow: Flow<ProfileData> = context.profileDataStore.data
        .map { preferences ->
            ProfileData(
                name = preferences[KEY_NAME] ?: "Иван Иванов",
                avatarUri = preferences[KEY_AVATAR_URI] ?: "",
                resumeUrl = preferences[KEY_RESUME_URL] ?: "",
                classTime = preferences[KEY_CLASS_TIME] ?: ""
            )
        }

    suspend fun saveProfile(profile: ProfileData) {
        context.profileDataStore.edit { preferences ->
            preferences[KEY_NAME] = profile.name
            preferences[KEY_AVATAR_URI] = profile.avatarUri
            preferences[KEY_RESUME_URL] = profile.resumeUrl
            preferences[KEY_CLASS_TIME] = profile.classTime
        }
    }
}
