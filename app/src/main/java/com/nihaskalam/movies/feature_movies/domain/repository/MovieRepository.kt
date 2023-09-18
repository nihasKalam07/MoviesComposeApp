package com.nihaskalam.movies.feature_movies.domain.repository

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getAllMovies(): Flow<Resource<List<Movie>>>

    fun getAllFavourites(): Flow<Resource<List<Movie>>>

    fun getMovie(imdbId: String): Flow<Resource<Movie>>

    fun updateMovie(isFavourite: Boolean, movieId: String): Flow<Resource<Movie>>

    fun getMoviesByTitle(title: String): Flow<Resource<List<Movie>>>

}