@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.karrar.movieapp.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingRequestDto(
    @SerialName("value") val value: Float
)