package com.nihaskalam.movies.feature_movies.domain.model

data class Movie(
    val actors: String = "",
    val director: String = "",
    val genre: String = "",
    val language: String = "",
    val plot: String = "",
    val poster: String = "",
    val runtime: String = "",
    val title: String = "",
    val year: String = "",
    val imdbID: String = "",
    val imdbRating: String = "",
    val isFavourite: Boolean = false
)