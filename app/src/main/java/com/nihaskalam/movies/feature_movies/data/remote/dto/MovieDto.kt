package com.nihaskalam.movies.feature_movies.data.remote.dto

import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.domain.model.Movie

/*DTO class to convert the Json response to proper format*/
data class MovieDto(
    val Actors: String,
    val Awards: String,
    val BoxOffice: String,
    val Country: String,
    val DVD: String,
    val Director: String,
    val Genre: String,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Production: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Website: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
) {
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            actors = Actors,
            director = Director,
            genre = Genre,
            language = Language,
            plot = Plot,
            poster = Poster,
            runtime = Runtime,
            title = Title,
            year = Year,
            imdbID = imdbID,
            imdbRating = imdbRating,
        )
    }

    fun toMovie(): Movie {
        return Movie(
            actors = Actors,
            director = Director,
            genre = Genre,
            language = Language,
            plot = Plot,
            poster = Poster,
            runtime = Runtime,
            title = Title,
            year = Year,
            imdbID = imdbID,
            imdbRating = imdbRating,
        )
    }
}