package com.karrar.movieapp.ui.profile.myratings

import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Rated
import javax.inject.Inject

class RatedUIStateMapper @Inject constructor() :Mapper<Rated,RatedUIState>  {
    override fun map(input: Rated): RatedUIState {
        return RatedUIState(
            id = input.id,
            title = input.title,
            rating = input.rating,
            posterPath = BuildConfig.IMAGE_BASE_PATH + input.posterPath,
            mediaType = input.mediaType,
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage,
            genres =  emptyList(),
            duration = input.duration
        )
    }
}