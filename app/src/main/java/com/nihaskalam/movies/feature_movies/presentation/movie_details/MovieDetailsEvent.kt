package com.nihaskalam.movies.feature_movies.presentation.movie_details

import com.nihaskalam.movies.feature_movies.domain.model.Movie

sealed class MovieDetailsEvent {
    data class UpdateMovie(val isFavourite: Boolean) : MovieDetailsEvent()
    data class FetchMovieDetails(val movieId: String) : MovieDetailsEvent()
}
