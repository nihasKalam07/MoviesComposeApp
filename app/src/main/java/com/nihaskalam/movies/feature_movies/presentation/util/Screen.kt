package com.nihaskalam.movies.feature_movies.presentation.util

sealed class Screen(val route: String) {
    object MovieListScreen: Screen("movie_list_screen")
    object MovieDetailsScreen: Screen("movie_details_screen")
    object SearchScreen: Screen("search_screen")
    object SplashScreen: Screen("splash_screen")
}
