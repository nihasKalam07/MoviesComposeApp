package com.nihaskalam.movies.feature_movies.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nihaskalam.movies.feature_movies.presentation.movie_details.MovieDetailsScreen
import com.nihaskalam.movies.feature_movies.presentation.movie_list.MovieListScreen
import com.nihaskalam.movies.feature_movies.presentation.search.SearchScreen

@Composable
fun MoviesNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "${Screen.MovieListScreen.route}/{${MOVIE_LIST_ALL}}"
    ) {
        composable("${Screen.MovieListScreen.route}/{${MOVIE_LIST_ALL}}",
            arguments = listOf(
                navArgument(name = MOVIE_LIST_ALL) {
                    type = NavType.StringType
                    defaultValue = MOVIE_LIST_ALL
                }
            )) {
            val listType = it.arguments?.getString(MOVIE_LIST_ALL) ?: ""
            MovieListScreen(navController, listType)
        }

        composable("${Screen.MovieDetailsScreen.route}/{${MOVIE_ID}}",
            arguments = listOf(
                navArgument(name = MOVIE_ID) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val movieId = it.arguments?.getString(MOVIE_ID) ?: ""
            MovieDetailsScreen(navController, movieId)
        }

        composable(Screen.SearchScreen.route) {
            SearchScreen(navController)
        }
    }
}