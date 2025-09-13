package com.karrar.movieapp.data.data_source.local

interface DetailsLocalDataSource {
    suspend fun insertFavouriteGenre(genreId: Int)
    suspend fun incrementGenreCount(genreId: Int)
    suspend fun getFavouriteGenres(): kotlinx.coroutines.flow.Flow<List<com.karrar.movieapp.data.local.database.entity.FavouriteGenreEntity>>
}