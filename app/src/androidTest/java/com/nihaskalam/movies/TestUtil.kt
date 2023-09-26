package com.nihaskalam.movies

import com.nihaskalam.movies.feature_movies.data.local.entity.MovieEntity
import com.nihaskalam.movies.feature_movies.domain.model.Movie

object TestUtil {
    fun getMovieEntityList(): List<MovieEntity> {
        val movie1 = Movie(title = "avatar", imdbID = "1", director = "James Cameron")
        val movie2 = Movie(title = "Inception", imdbID = "2", director = "Nolan")
        val movie3 = Movie(title = "Pulp fiction", imdbID = "3", director = "Quentin Tarantino")
        return listOf(movie1, movie2, movie3).map { it.toMovieEntity() }
    }

    fun getFavouritesList(): List<MovieEntity> {
        val movie1 = Movie(title = "avatar", imdbID = "1", director = "James Cameron")
        val movie2 =
            Movie(title = "Inception", imdbID = "2", director = "Nolan", isFavourite = true)
        val movie3 = Movie(
            title = "Pulp fiction",
            imdbID = "3",
            director = "Quentin Tarantino",
            isFavourite = true
        )
        return listOf(movie1, movie2, movie3).map { it.toMovieEntity() }
    }
}
