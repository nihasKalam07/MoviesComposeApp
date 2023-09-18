package com.nihaskalam.movies.feature_movies.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextComponent(
    text: String,
    textSize: TextUnit,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Light
) {

    Text(
        text = text,
        fontSize = textSize,
        color = color,
        fontWeight = fontWeight
    )

}

@Preview(showBackground = true)
@Composable
fun TextComponentPreview() {

    TextComponent(
        text = "Text",
        textSize = 24.sp
    )
}