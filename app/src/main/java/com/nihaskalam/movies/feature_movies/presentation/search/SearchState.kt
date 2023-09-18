package com.nihaskalam.movies.feature_movies.presentation.search

import com.nihaskalam.movies.feature_movies.domain.model.Movie

data class SearchState(
    val movies: List<Movie> = emptyList(),
    val query: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
    val isLoading: Boolean = false
)
