package com.vadlap.practise3.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlap.practise3.data.ProfileData
import com.vadlap.practise3.data.ProfileStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileStorage: ProfileStorage) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileData("", "", ""))
    val profileState = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            profileStorage.profileFlow.collectLatest { profile ->
                _profileState.value = profile
            }
        }
    }

    fun saveProfile(name: String, avatarUri: String, resumeUrl: String) {
        viewModelScope.launch {
            val newProfile = ProfileData(name, avatarUri, resumeUrl)
            profileStorage.saveProfile(newProfile)
        }
    }
}
