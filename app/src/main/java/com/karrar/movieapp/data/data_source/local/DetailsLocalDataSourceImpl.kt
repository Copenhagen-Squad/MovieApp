package com.karrar.movieapp.data.data_source.local


import com.karrar.movieapp.data.local.database.daos.FavouriteGenreDao
import com.karrar.movieapp.data.local.database.entity.FavouriteGenreEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailsLocalDataSourceImpl  @Inject constructor(
    private val favouriteGenreDao: FavouriteGenreDao
) : DetailsLocalDataSource {

    override suspend fun insertFavouriteGenre(genreId: Int) {
        val existingGenre = favouriteGenreDao.getGenreById(genreId)
        if (existingGenre != null) {
            favouriteGenreDao.incrementGenreCount(genreId)
        } else {
            favouriteGenreDao.insertOrUpdateFavouriteGenre(
                FavouriteGenreEntity(genreId = genreId, count = 1)
            )
        }
    }

    override suspend fun incrementGenreCount(genreId: Int) {
        insertFavouriteGenre(genreId)
    }

    override suspend fun getFavouriteGenres(): Flow<List<FavouriteGenreEntity>> {
        return favouriteGenreDao.getFavouriteGenres()
    }
}