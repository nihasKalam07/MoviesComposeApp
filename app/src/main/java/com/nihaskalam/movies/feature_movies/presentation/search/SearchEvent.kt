package com.nihaskalam.movies.feature_movies.presentation.search

import androidx.compose.ui.focus.FocusState

sealed class SearchEvent {
    data class SearchMovie(val query: String) : SearchEvent()
    data class ChangeSearchFocus(val focusState: FocusState) : SearchEvent()
}
