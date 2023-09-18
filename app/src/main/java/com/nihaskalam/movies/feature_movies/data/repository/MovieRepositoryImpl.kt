package com.nihaskalam.movies.feature_movies.data.repository

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.data.local.MovieDao
import com.nihaskalam.movies.feature_movies.data.remote.MoviesApi
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val api: MoviesApi,
    private val dao: MovieDao
) : MovieRepository {

    override fun getAllMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        val movies = dao.getAllMovies().map { it.toMovie() }
        emit(Resource.Loading(data = movies))

        // This if block is temporary. Since we
        // we don't sync the favourites to server, This code is
        //  a fix to avoid refreshing database each time when comes to the home screen
        if (movies.isNotEmpty()) {
            val newMovieList = dao.getAllMovies().map { it.toMovie() }
            emit(Resource.Success(newMovieList))
            return@flow
        }

        try {
            val remoteMovies = api.getMovies()
            dao.deleteMovies(remoteMovies.movies.map { it.imdbID })
            dao.insertMovies(remoteMovies.movies.map { it.toMovieEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = movies
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = movies
                )
            )
        }

        val newMovieList = dao.getAllMovies().map { it.toMovie() }
        emit(Resource.Success(newMovieList))
    }

    override fun getAllFavourites(): Flow<Resource<List<Movie>>> = flow {
        val movies = dao.getAllFavourites().map { it.toMovie() }
        emit(Resource.Success(data = movies))
    }

    override fun getMovie(imdbId: String): Flow<Resource<Movie>> = flow {
        val movie = dao.getMovieById(imdbId).toMovie()
        emit(Resource.Success(data = movie))
    }

    override fun updateMovie(isFavourite: Boolean, imdbID: String): Flow<Resource<Movie>> = flow {
        dao.update(isFavourite, imdbID)
        val movie = dao.getMovieById(imdbID).toMovie()
        println("======= movie in repo ${movie.isFavourite}")
        emit(Resource.Success(data = movie))

    }

    override fun getMoviesByTitle(title: String): Flow<Resource<List<Movie>>> = flow {
        val movies = dao.getMoviesByTitle(title).map { it.toMovie() }
        emit(Resource.Success(data = movies))
    }
}