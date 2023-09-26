package com.nihaskalam.movies.feature_movies.data.repository

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieRepository : MovieRepository {

    private val movies = mutableListOf<Movie>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun insertMovie(movie: Movie) {
        movies.add(movie)
    }

    fun deleteMovies() {
        movies.clear()
    }

    override fun getAllMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Loading(data = movies))
        if (shouldReturnNetworkError) {
            emit(
                Resource.Error(
                    message = API_ERROR_MESSAGE,
                    data = movies
                )
            )
        } else {
            emit(Resource.Success(movies))
        }
    }

    override fun getAllFavourites(): Flow<Resource<List<Movie>>> = flow {
        val movies = movies.filter { it.isFavourite }
        emit(Resource.Success(data = movies))
    }

    override fun getMovie(imdbId: String): Flow<Resource<Movie>> = flow {
        val movie = movies.find { it.imdbID == imdbId }
        emit(Resource.Success(data = movie))
    }

    override fun updateMovie(isFavourite: Boolean, imdbId: String): Flow<Resource<Movie>> = flow {
        val movie = movies.find { it.imdbID == imdbId }?.copy(
            isFavourite = isFavourite
        )
        emit(Resource.Success(data = movie))
    }

    override fun getMoviesByTitle(title: String): Flow<Resource<List<Movie>>> = flow {
        val movies = movies.filter { it.title.contains(title) }
        emit(Resource.Success(data = movies))
    }

    companion object {
        const val API_ERROR_MESSAGE = "Oops, something went wrong!"
    }
}