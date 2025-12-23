package com.vadlap.practise3.presentation.utils

import com.vadlap.practise3.data.FilterSettings

class BadgeCache {
    
    fun shouldShowBadge(settings: FilterSettings): Boolean {
        return settings.title != "Batman" || settings.type.isNotEmpty() || settings.year.isNotEmpty()
    }
}
