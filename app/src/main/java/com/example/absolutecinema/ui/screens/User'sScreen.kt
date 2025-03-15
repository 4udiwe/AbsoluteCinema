package com.example.absolutecinema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absolutecinema.R
import com.example.absolutecinema.ui.screens.dummy.Film

private const val POSTER_HEIGHT = 150
private const val POSTER_WIDTH = 100


/**
 * Отображает постер фильома с названием и жанрами внизу и оценкой пользователя.
 * Используется на экране пользователя (User'sScreen)
 */
@Composable
private fun FilmPosterWNameGenre(film: Film, onFilmClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .width(POSTER_WIDTH.dp)
            .padding(end = 8.dp)
            .clickable { onFilmClicked.invoke() }
    ) {
        LoadImageWithPlaceholder(
            imageUrl = film.posterURL,
            placeholderResId = R.drawable.ic_launcher_background,
            modifier = Modifier
                .height(POSTER_HEIGHT.dp)
                .width(POSTER_WIDTH.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier.width(POSTER_WIDTH.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = film.name,
                    color = colorResource(R.color.text),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = film.genres,
                    color = colorResource(R.color.text_second),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            UserScore(film)
        }
    }
}

/**
 * Отображает небольшой ряд фильмов с постером.
 * Содержит одно из названий категории:
 * - буду смотреть
 * - ваши оценки
 * - любимые
 * Используется на экране пользователя (User'sScreen)
 */
@Composable
private fun HorizontalRowWTitleSmall(title: String, list: List<Film>, onAllClicked: () -> Unit, onFilmClicked: () -> Unit) {
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
            Text(title, fontSize = 24.sp, color = colorResource(R.color.text))
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
                FilmPosterWNameGenre(film) {
                    onFilmClicked.invoke()
                }
            }
        }
    }
}


/**
 * Экран пользователя.
 * Содержит 3 ряда с фильмами:
 * - буду смотреть
 * - ваши оценки
 * - любимые
 */
@Preview(showSystemUi = true)
@Composable
fun UsersScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onFilmClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(paddingValues)
            .verticalScroll(scrollState)
        ) {

        val list = arrayListOf(Film(), Film(), Film(), Film(), Film(), Film(), Film(), Film())

        HorizontalRowWTitleSmall(
            title = stringResource(R.string.WillWatch), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
        HorizontalRowWTitleSmall(title = stringResource(R.string.YourRates), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
        HorizontalRowWTitleSmall(title = stringResource(R.string.Favourite), list,
            onAllClicked = { },
            onFilmClicked = { onFilmClicked.invoke() }
        )
    }

}