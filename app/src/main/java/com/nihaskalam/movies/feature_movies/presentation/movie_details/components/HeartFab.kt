package com.nihaskalam.movies.feature_movies.presentation.movie_details.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.nihaskalam.movies.feature_movies.presentation.movie_details.MovieState

@Composable
fun HeartFAB(
    state: MovieState,
    onClick: (Boolean) -> Unit
) {
    var isLiked = state.movie?.isFavourite ?: false

    FloatingActionButton(
        onClick = {
            isLiked = !isLiked
            onClick(isLiked)
        },
        shape = CircleShape
    ) {
        Icon(
            imageVector = if (state.movie?.isFavourite
                    ?: isLiked
            ) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite"
        )
    }
}