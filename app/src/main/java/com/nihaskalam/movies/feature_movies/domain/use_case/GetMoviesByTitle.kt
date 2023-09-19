package com.nihaskalam.movies.feature_movies.domain.use_case

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMoviesByTitle(
    private val repository: MovieRepository
) {
    operator fun invoke(title: String): Flow<Resource<List<Movie>>> {
        if (title.isBlank()) return flow { emit(Resource.Success(data = emptyList())) }
        return repository.getMoviesByTitle(title)
    }

}