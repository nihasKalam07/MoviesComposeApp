package com.nihaskalam.movies.feature_movies.domain.model

import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity

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
) {
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            actors = actors,
            director = director,
            genre = genre,
            language = language,
            plot = plot,
            poster = poster,
            runtime = runtime,
            title = title,
            year = year,
            imdbID = imdbID,
            imdbRating = imdbRating,
        )
    }
}