package com.nihaskalam.movies.feature_movies.data.remote.dto

import com.nihaskalam.movies.feature_movies.domain.model.Movies

/*DTO class to convert the Json response to proper format*/
data class MoviesDto(
    val movies: List<MovieDto>
) {
    fun toMovies(): Movies = Movies(movies = movies.map { it.toMovie() })
}