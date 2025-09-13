@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.karrar.movieapp.data.remote.response.genre

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("genres") val genres: List<GenreDto>
)

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int,
    @SerialName("name")  val name: String
)
