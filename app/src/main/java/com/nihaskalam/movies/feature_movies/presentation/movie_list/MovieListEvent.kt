package com.nihaskalam.movies.feature_movies.presentation.movie_list

sealed class MovieListEvent {
    object ShowAllMovies : MovieListEvent()
    object ShowAllFavourites : MovieListEvent()
//    data class UpdateSelectedTabIndex(val index: Int) : MovieListEvent()
}
