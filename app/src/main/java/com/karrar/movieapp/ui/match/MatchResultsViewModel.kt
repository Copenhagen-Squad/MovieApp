package com.karrar.movieapp.ui.match

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.data.MatchParams
import com.karrar.movieapp.domain.usecases.GetMatchRecommendationsUseCase
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.MovieDetailsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMatchRecommendations: GetMatchRecommendationsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MatchResultsUiState())
    val uiState = _uiState.asStateFlow()

    private val _items = MutableStateFlow<List<MovieDetailsUIState>>(emptyList())
    val items = _items.asStateFlow()

    private val args = MatchParams(
        genres = savedStateHandle.get<String>("genres")?.takeIf { it.isNotBlank() },
        runtimeGte = savedStateHandle.get<Int>("runtimeGte")?.takeIf { it >= 0 },
        runtimeLte = savedStateHandle.get<Int>("runtimeLte")?.takeIf { it >= 0 },
        releaseDateGte = savedStateHandle.get<String>("releaseDateGte")?.takeIf { it.isNotBlank() },
        releaseDateLte = savedStateHandle.get<String>("releaseDateLte")?.takeIf { it.isNotBlank() },
    )

    init {
        fetch()
    }

    private fun fetch() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val result = getMatchRecommendations.getEnrichedRecommendations(args)
                result.onSuccess { movieList ->
                    Log.d("MatchViewModel_Debug", "=== RAW DATA FROM API ===")
                    Log.d("MatchViewModel_Debug", "Number of movies received: ${movieList.size}")

                    movieList.forEachIndexed { index, movie ->
                        Log.d("MatchViewModel_Debug", "--- Movie $index ---")
                        Log.d("MatchViewModel_Debug", "Raw movie object: $movie")
                        Log.d(
                            "MatchViewModel_Debug",
                            "Movie class: ${movie::class.java.simpleName}"
                        )

                        logAllFields(movie)
                    }

                    val mapped = movieList.mapIndexed { index, movie ->
                        Log.d("MatchViewModel_Debug", "=== MAPPING Movie $index ===")

                        createMovieDetailsUIState(movie)
                    }

                    Log.d("MatchViewModel_Debug", "=== MAPPING COMPLETE ===")
                    Log.d("MatchViewModel_Debug", "Total mapped items: ${mapped.size}")

                    _items.value = mapped
                    _uiState.update { it.copy(isLoading = false, error = null) }

                }.onFailure { exception ->
                    Log.e("MatchViewModel_Debug", "Failed to fetch recommendations", exception)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            } catch (exception: Exception) {
                Log.e("MatchViewModel_Debug", "Error in fetch", exception)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun createMovieDetailsUIState(movie: Any): MovieDetailsUIState {
        val genres = tryGetGenres(movie) ?: "Unknown Genre"
        val runtimeMinutes = tryGetRuntimeMinutes(movie) ?: 0
        val (hours, minutes) = convertMinutesToHoursAndMinutes(runtimeMinutes)
        val voteAverage = formatVoteAverage(tryGetVoteAverage(movie))

        val movieDetailsUIState = MovieDetailsUIState(
            id = tryGetId(movie),
            name = tryGetTitle(movie),
            image = tryGetPosterUrl(movie) ?: "",
            genres = genres,
            voteAverage = voteAverage,
            hours = hours,
            minutes = minutes,
            releaseDate = tryGetReleaseDate(movie) ?: "",
            review = 0,
            specialNumber = 0,
            overview = ""
        )

        Log.d("MatchViewModel_Debug", "=== FINAL MAPPED ITEM ===")
        Log.d("MatchViewModel_Debug", "MovieDetailsUIState: $movieDetailsUIState")

        return movieDetailsUIState
    }

    private fun logAllFields(movie: Any) {
        try {
            val fields = movie::class.java.declaredFields
            Log.d("MatchViewModel_Debug", "Available fields in ${movie::class.java.simpleName}:")
            fields.forEach { field ->
                field.isAccessible = true
                val value = field.get(movie)
                Log.d(
                    "MatchViewModel_Debug",
                    "  Field '${field.name}': $value (type: ${value?.let { it::class.java.simpleName }})"
                )
            }
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error accessing fields via reflection", e)
        }
    }

    private fun tryGetGenres(movie: Any): String? {
        return try {
            val clazz = movie::class.java
            val possibleFields =
                listOf("genres", "genre", "genreIds", "category", "categories", "genreNames")

            for (fieldName in possibleFields) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(movie)

                    Log.d(
                        "MatchViewModel_Debug",
                        "Found genre field '$fieldName': $value (type: ${value?.javaClass?.simpleName})"
                    )

                    if (value != null) {
                        return when (value) {
                            is List<*> -> {
                                val genreNames = value.mapNotNull { genreItem ->
                                    when (genreItem) {
                                        is String -> genreItem
                                        else -> {
                                            // Try to extract name field from genre objects
                                            try {
                                                val nameField =
                                                    genreItem?.javaClass?.getDeclaredField("name")
                                                nameField?.isAccessible = true
                                                nameField?.get(genreItem) as? String
                                            } catch (e: Exception) {
                                                // Try id field and map to genre name
                                                try {
                                                    val idField =
                                                        genreItem?.javaClass?.getDeclaredField("id")
                                                    idField?.isAccessible = true
                                                    val genreId = idField?.get(genreItem) as? Int
                                                    mapGenreIdToName(genreId)
                                                } catch (e2: Exception) {
                                                    genreItem?.toString()
                                                }
                                            }
                                        }
                                    }
                                }.filter { it.isNotBlank() }

                                if (genreNames.isNotEmpty()) genreNames.joinToString(", ") else null
                            }

                            is String -> if (value.isNotBlank()) value else null
                            else -> value.toString().takeIf { it.isNotBlank() && it != "null" }
                        }
                    }
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }

            Log.w("MatchViewModel_Debug", "No genre field found in movie object")
            null
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting genres", e)
            null
        }
    }

    private fun tryGetRuntimeMinutes(movie: Any): Int? {
        return try {
            val clazz = movie::class.java
            val possibleFields = listOf("runtime", "duration", "runtimeMinutes", "length")

            for (fieldName in possibleFields) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(movie)

                    Log.d("MatchViewModel_Debug", "Found runtime field '$fieldName': $value")

                    if (value != null) {
                        return when (value) {
                            is Int -> if (value > 0) value else null
                            is String -> value.toIntOrNull()?.takeIf { it > 0 }
                            else -> value.toString().toIntOrNull()?.takeIf { it > 0 }
                        }
                    }
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }

            Log.w("MatchViewModel_Debug", "No runtime field found in movie object")
            null
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting runtime", e)
            null
        }
    }

    private fun convertMinutesToHoursAndMinutes(totalMinutes: Int): Pair<Int, Int> {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        Log.d("MatchViewModel_Debug", "Converted $totalMinutes minutes to ${hours}h ${minutes}m")
        return Pair(hours, minutes)
    }

    private fun formatVoteAverage(voteAverage: Double?): String {
        return voteAverage?.let {
            String.format("%.1f", it)
        } ?: "0.0"
    }

    private fun mapGenreIdToName(genreId: Int?): String? {
        return when (genreId) {
            28 -> "Action"
            12 -> "Adventure"
            16 -> "Animation"
            35 -> "Comedy"
            80 -> "Crime"
            99 -> "Documentary"
            18 -> "Drama"
            10751 -> "Family"
            14 -> "Fantasy"
            36 -> "History"
            27 -> "Horror"
            10402 -> "Music"
            9648 -> "Mystery"
            10749 -> "Romance"
            878 -> "Science Fiction"
            10770 -> "TV Movie"
            53 -> "Thriller"
            10752 -> "War"
            37 -> "Western"
            else -> null
        }
    }

    private fun tryGetId(movie: Any): Int {
        return try {
            val clazz = movie::class.java
            val field = clazz.getDeclaredField("id")
            field.isAccessible = true
            val value = field.get(movie)

            when (value) {
                is Int -> value
                is Long -> value.toInt()
                is String -> value.toIntOrNull() ?: 0
                else -> 0
            }
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting id", e)
            0
        }
    }

    private fun tryGetTitle(movie: Any): String {
        return try {
            val clazz = movie::class.java
            val possibleFields = listOf("title", "name", "originalTitle")

            for (fieldName in possibleFields) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(movie) as? String
                    if (!value.isNullOrBlank()) return value
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }
            "Unknown Title"
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting title", e)
            "Unknown Title"
        }
    }

    private fun tryGetPosterUrl(movie: Any): String? {
        return try {
            val clazz = movie::class.java
            val possibleFields = listOf("posterPath", "posterUrl", "poster")

            for (fieldName in possibleFields) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(movie) as? String
                    if (!value.isNullOrBlank()) {
                        return if (value.startsWith("http")) value
                        else "https://image.tmdb.org/t/p/w500$value"
                    }
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }
            null
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting poster URL", e)
            null
        }
    }

    private fun tryGetVoteAverage(movie: Any): Double? {
        return try {
            val clazz = movie::class.java
            val possibleFields = listOf("voteAverage", "rating", "score")

            for (fieldName in possibleFields) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(movie)

                    return when (value) {
                        is Double -> value
                        is Float -> value.toDouble()
                        is Int -> value.toDouble()
                        is String -> value.toDoubleOrNull()
                        else -> null
                    }
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }
            null
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting vote average", e)
            null
        }
    }

    private fun tryGetReleaseDate(movie: Any): String? {
        return try {
            val clazz = movie::class.java
            val possibleFields = listOf("releaseDate", "releaseDateShort", "date")

            for (fieldName in possibleFields) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(movie) as? String
                    if (!value.isNullOrBlank()) {
                        return formatReleaseDate(value)
                    }
                } catch (e: NoSuchFieldException) {
                    continue
                }
            }
            null
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error getting release date", e)
            null
        }
    }

    private fun formatReleaseDate(dateString: String): String {
        return try {
            when {
                dateString.contains("GMT") -> {
                    val parts = dateString.split(" ")
                    parts.lastOrNull()?.takeIf { it.length == 4 } ?: dateString
                }

                dateString.contains("-") -> {
                    dateString.split("-").firstOrNull() ?: dateString
                }

                else -> dateString
            }
        } catch (e: Exception) {
            Log.e("MatchViewModel_Debug", "Error formatting date: $dateString", e)
            dateString
        }
    }
}

data class MatchResultsUiState(
    val isLoading: Boolean = true,
    val selectedItem: MovieDetailsUIState? = null,
    val error: String? = null
)