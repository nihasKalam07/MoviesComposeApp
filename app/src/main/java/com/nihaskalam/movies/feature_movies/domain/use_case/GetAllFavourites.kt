package com.nihaskalam.movies.feature_movies.domain.use_case

import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.model.Movie
import com.nihaskalam.movies.feature_movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavourites(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = repository.getAllFavourites()
}