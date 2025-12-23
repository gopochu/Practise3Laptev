package com.vadlap.practise3.presentation.news

import kotlinx.serialization.Serializable

@Serializable
data class KinoDataStruct(
    val id: Int,
    val title: String,
    val text: String?,
    val imageUrl: String?,
)