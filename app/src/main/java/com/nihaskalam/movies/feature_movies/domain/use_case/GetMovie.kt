package com.nihaskalam.movies.feature_movies.domain.use_case

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovie(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: String): Flow<Resource<Movie>> {
        return repository.getMovie(movieId)
    }

}