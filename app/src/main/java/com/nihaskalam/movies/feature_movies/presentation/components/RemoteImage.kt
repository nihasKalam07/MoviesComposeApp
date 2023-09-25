package com.nihaskalam.movies.feature_movies.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nihaskalam.movies.R

@Composable
fun NetworkImage(
    url: String?,
    height: Dp = 150.dp,
    isClip: Boolean = false,
    isClipTopOnly: Boolean = false,
    contentDescription: String? = stringResource(id = R.string.app_name)
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .let {
                if (isClipTopOnly) {
                    it.clip(
                        shape = RoundedCornerShape(
                            topEnd = 8.dp,
                            topStart = 8.dp
                        )
                    )
                } else if (isClip) {
                    it.clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                } else it
            }
            .background(MaterialTheme.colorScheme.primary)
//            .aspectRatio(1f)
        ,
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.poster_placeholder)
    )
}