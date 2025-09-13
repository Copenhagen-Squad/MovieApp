@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.karrar.movieapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestionDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)
