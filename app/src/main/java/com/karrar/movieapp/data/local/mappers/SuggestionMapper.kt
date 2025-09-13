package com.karrar.movieapp.data.local.mappers

import com.karrar.movieapp.data.remote.SuggestionDto


fun SuggestionDto.toModel(): String {
    return this.name ?: ""
}