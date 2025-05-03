package com.example.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.domain.model.Movie

@Preview
@Composable
fun CommonMovieListScreen(
    paddingValues: PaddingValues = PaddingValues(),
    title: String = "Title",
    moviesList: List<Movie> = emptyList(),
    onBackClicked: () -> Unit = {},
    onMovieClicked: (Movie) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(top = 16.dp, bottom = 8.dp),
                maxLines = 1
            )
            TextButton(
                onClick = {
                    onBackClicked.invoke()
                }
            ) {
                Text(
                    text = "Назад",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.accent),
                    maxLines = 1
                )
            }
        }
        LazyColumn {
            items(moviesList) {
                CommonMovieItem(
                    movie = it,
                    onMovieClicked = onMovieClicked
                )
            }
        }
    }
}