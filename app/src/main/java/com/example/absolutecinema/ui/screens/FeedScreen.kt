package com.example.absolutecinema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absolutecinema.R

private const val POSTER_WIDTH = 133
private const val POSTER_HEIGHT = 200


@Composable
private fun FilmPosterWName(film: Film, onFilmClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .width(POSTER_WIDTH.dp)
            .padding(end = 8.dp)
            .clickable {
                onFilmClicked.invoke()
            }
    ) {
        Box {
            LoadImageWithPlaceholder(
                imageUrl = film.posterURL,
                placeholderResId = R.drawable.ic_launcher_background,
                modifier = Modifier
                    .height(POSTER_HEIGHT.dp)
                    .width(POSTER_WIDTH.dp),
                contentScale = ContentScale.Crop
            )
            FilmRating(film)
        }
        Text(
            "${film.name} (${film.year})",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun HorizontalRowWTitleBig(
    title: String,
    list: List<Film>,
    onAllClicked: () -> Unit,
    onFilmClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(title, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
            Text(
                stringResource(R.string.All),
                fontSize = 20.sp,
                color = colorResource(R.color.accent),
                modifier = Modifier.clickable {
                    onAllClicked.invoke()
                })
        }
        LazyRow {
            items(list) { film ->
                FilmPosterWName(film) {
                    onFilmClicked.invoke()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FeedScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onFilmClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),

        ) {

        val list = arrayListOf(Film(), Film(), Film(), Film(), Film(), Film(), Film(), Film())

        HorizontalRowWTitleBig(
            title = stringResource(R.string.FilmsForYou), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
        HorizontalRowWTitleBig(title = stringResource(R.string.SerialsForYou), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
        HorizontalRowWTitleBig(title = stringResource(R.string.Detectives), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
        HorizontalRowWTitleBig(title = stringResource(R.string.Comedys), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
        HorizontalRowWTitleBig(title = stringResource(R.string.Romans), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
    }

}