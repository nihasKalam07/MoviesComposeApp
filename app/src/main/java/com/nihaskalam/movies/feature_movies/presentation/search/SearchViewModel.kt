package com.nihaskalam.movies.feature_movies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nihaskalam.movies.core.util.Resource
import com.nihaskalam.movies.feature_movies.domain.use_case.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchItemsState = MutableStateFlow(SearchState())
    val searchItemsState = _searchItemsState.asStateFlow()

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchMovie -> onSearch(event.query)
            is SearchEvent.ChangeSearchFocus -> {
                _searchItemsState.value = _searchItemsState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _searchItemsState.value.query.isBlank()
                )
            }
        }
    }

    private fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            moviesUseCases.getMoviesByTitle(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _searchItemsState.value = _searchItemsState.value.copy(
                                movies = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        else -> {}
                    }
                }.launchIn(this)
        }
    }
}