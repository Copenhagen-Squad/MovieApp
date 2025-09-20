package com.karrar.movieapp.domain


enum class QuestionType(val titleResource: String) {
    MOOD("how are you feeling today?" ),
    GENRE("pick your favorite genre"),
    TIME("how much time do you have?"),
    TYPE("what type of movie are you in the mood for?");
}