package com.nihaskalam.movies.feature_movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nihaskalam.movies.feature_movies.domain.model.Movie

@Entity
data class MovieEntity(
    val actors: String,
    val director: String,
    val genre: String,
    val language: String,
    val plot: String,
    val poster: String,
    val runtime: String,
    val title: String,
    val year: String,
    val imdbID: String,
    val imdbRating: String,
    var isFavourite: Boolean = false,
    @PrimaryKey val id: Int? = null
) {
    fun toMovie(): Movie {
        return Movie(
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
            isFavourite = isFavourite
        )
    }
}