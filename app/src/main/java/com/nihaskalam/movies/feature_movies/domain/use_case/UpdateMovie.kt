package com.nihaskalam.movies.feature_movies.domain.use_case

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class UpdateMovie(
    private val repository: MovieRepository
) {
    operator fun invoke(isFavourite: Boolean, movieId: String): Flow<Resource<Movie>> {
        return repository.updateMovie(isFavourite, movieId)
    }

}