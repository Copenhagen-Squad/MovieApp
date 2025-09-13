@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.karrar.movieapp.data.remote.response.tvShow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyDto(
    @SerialName("id") val id: Int?,
    @SerialName("logo_path") val logoPath: String? = null,
    @SerialName("name") val name: String?,
    @SerialName("origin_country") val originCountry: String?,
)
