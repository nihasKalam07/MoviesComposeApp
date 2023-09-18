package com.nihaskalam.movies.feature_movies.presentation.movie_details

import com.nihaskalam.movies.feature_movies.domain.model.Movie

data class MovieState(
    val movie: Movie? = Movie(),
    val isLoading: Boolean = false
)
