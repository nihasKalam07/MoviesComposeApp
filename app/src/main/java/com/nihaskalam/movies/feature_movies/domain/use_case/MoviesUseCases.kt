package com.nihaskalam.movies.feature_movies.domain.use_case

data class MoviesUseCases(
    val getAllMovies: GetAllMovies,
    val getAllFavourites: GetAllFavourites,
    val getMoviesByTitle: GetMoviesByTitle,
    val updateMovie: UpdateMovie,
    val getMovie: GetMovie
)
