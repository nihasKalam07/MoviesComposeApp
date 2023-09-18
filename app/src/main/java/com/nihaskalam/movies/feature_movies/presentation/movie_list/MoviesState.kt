package com.nihaskalam.movies.feature_movies.presentation.movie_list

import com.nihaskalam.movies.feature_movies.domain.model.Movie

data class MoviesState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)
