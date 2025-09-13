@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.karrar.movieapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountryDto(
    @SerialName("iso_3166_1") val iso: String?,
    @SerialName("name") val name: String?,
)
