package com.vadlap.practise3.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vadlap.practise3.data.FilterStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SettingsState(
    val title: String = "",
    val type: String = "",
    val year: String = ""
)

class SettingsViewModel(private val filterStorage: FilterStorage) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val currentSettings = filterStorage.filterFlow.first()
            _state.value = SettingsState(
                title = currentSettings.title,
                type = currentSettings.type,
                year = currentSettings.year
            )
        }
    }

    fun onTitleChanged(newTitle: String) {
        _state.value = _state.value.copy(title = newTitle)
    }

    fun onTypeChanged(newType: String) {
        _state.value = _state.value.copy(type = newType)
    }

    fun onYearChanged(newYear: String) {
        _state.value = _state.value.copy(year = newYear)
    }

    fun saveSettings() {
        viewModelScope.launch {
            filterStorage.saveFilters(
                title = _state.value.title.ifEmpty { "Batman" },
                type = _state.value.type,
                year = _state.value.year
            )
        }
    }
}
