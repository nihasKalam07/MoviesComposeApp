package com.nihaskalam.movies.feature_movies.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.use_case.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel() {

    private val _movieState = MutableStateFlow(MovieState())
    val movieState = _movieState.asStateFlow()

    fun onEvent(event: MovieDetailsEvent) {
        when (event) {
            is MovieDetailsEvent.UpdateMovie -> updateMovie(event.isFavourite)
            is MovieDetailsEvent.FetchMovieDetails -> getMovie(event.movieId)
        }
    }

    private fun updateMovie(isFavourite: Boolean) {
        movieState.value.movie?.imdbID?.let {
            viewModelScope.launch {
                moviesUseCases.updateMovie(isFavourite, it)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _movieState.value = _movieState.value.copy(
                                    movie = result.data,
                                    isLoading = false
                                )
                            }

                            else -> {}
                        }
                    }.launchIn(this)
            }
        }
    }

    private fun getMovie(movieId: String) {
        viewModelScope.launch {
            moviesUseCases.getMovie(movieId)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _movieState.value = _movieState.value.copy(
                                movie = result.data,
                                isLoading = false
                            )
                        }

                        else -> {}
                    }
                }.launchIn(this)
        }
    }
}