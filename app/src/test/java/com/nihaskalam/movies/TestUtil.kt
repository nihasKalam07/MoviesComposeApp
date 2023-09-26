package com.nihaskalam.movies

import com.nihaskalam.movies.feature_movies.domain.model.Movie

object TestUtil {
    fun getMovieList(): List<Movie> {
        val movie1 =
            Movie(title = "avatar", imdbID = "1", director = "James Cameron", isFavourite = true)
        val movie2 = Movie(title = "Inception", imdbID = "2", director = "Nolan")
        val movie3 = Movie(title = "Pulp fiction", imdbID = "3", director = "Quentin Tarantino")
        return listOf(movie1, movie2, movie3)
    }
}
