package com.nihaskalam.movies.feature_movies.presentation.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.use_case.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel() {

    private val _movieListState = MutableStateFlow(MoviesState())
    val movieListState = _movieListState.asStateFlow()

    private val _favouritesState = MutableStateFlow(FavouritesState())
    val favouritesState = _favouritesState.asStateFlow()

//    private val _selectedTabIndexState = MutableStateFlow(0)
//    val selectedTabIndexState = _selectedTabIndexState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: MovieListEvent) {
        when (event) {
            is MovieListEvent.ShowAllMovies -> getAllMovies()
            is MovieListEvent.ShowAllFavourites -> getAllFavourites()
//            is MovieListEvent.UpdateSelectedTabIndex -> {
//                println("============in vm event ${event.index}")
//                _selectedTabIndexState.value = event.index
//                println("============in vm ${selectedTabIndexState.value}")
//            }
        }
    }

    fun getAllMovies() {
        viewModelScope.launch {
            moviesUseCases.getAllMovies()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _movieListState.value = _movieListState.value.copy(
                                movies = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _movieListState.value = _movieListState.value.copy(
                                movies = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(
                                    result.message ?: "Unknown error"
                                )
                            )
                        }

                        is Resource.Loading -> {
                            _movieListState.value = _movieListState.value.copy(
                                movies = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getAllFavourites() {
        viewModelScope.launch {
            moviesUseCases.getAllFavourites()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _favouritesState.value = _favouritesState.value.copy(
                                favourites = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        else -> {}
                    }
                }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}