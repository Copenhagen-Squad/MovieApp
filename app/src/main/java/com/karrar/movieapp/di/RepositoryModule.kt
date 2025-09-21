package com.karrar.movieapp.di

import com.karrar.movieapp.data.repository.AccountRepository
import com.karrar.movieapp.data.repository.AccountRepositoryImp
import com.karrar.movieapp.data.repository.MovieRepositoryImp
import com.karrar.movieapp.data.repository.SeriesRepository
import com.karrar.movieapp.data.repository.SeriesRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepositoryImp(
        movieRepositoryImp: MovieRepositoryImp
    ): com.karrar.movieapp.data.repository.MovieRepository

    @Binds
    @Singleton
    abstract fun bindSeriesRepository(
        seriesRepositoryImp: SeriesRepositoryImp
    ): SeriesRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        accountRepositoryImp: AccountRepositoryImp
    ): AccountRepository
}