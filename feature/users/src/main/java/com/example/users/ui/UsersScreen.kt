package com.example.users.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.LoadImageWithPlaceholder
import com.example.core.ui.UserScore
import com.example.core.util.getName
import com.example.domain.model.Movie
import com.example.users.viewmodel.UsersViewModel

private const val POSTER_HEIGHT = 150
private const val POSTER_WIDTH = 100


/**
 * Отображает постер фильма, название и жарны.
 *
 * @param movie
 * @param onMovieClicked
 */
@Composable
private fun FilmPosterWNameGenre(movie: Movie, onMovieClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .width(POSTER_WIDTH.dp)
            .padding(end = 8.dp)
            .clickable { onMovieClicked.invoke() }
    ) {
        LoadImageWithPlaceholder(
            imageUrl = movie.poster?.posterUrl,
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.getName(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.genres.map { it.name }.joinToString(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            UserScore(movie = movie, modifier = Modifier.size(20.dp))
        }
    }
}

/**
 * Горизонтальный список с фильмами пользователя.
 *
 * @param title заголовок категории.
 * @param list список фильмов.
 * @param onAllClicked функция вызывается при клике на кнопку "все" рядом с заголовком.
 * @param onMovieClicked функция вызывается при клике на фильм.
 */
@Composable
private fun HorizontalRowWTitleSmall(
    title: String,
    list: List<Movie>,
    onAllClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit,
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
            if (list.isNotEmpty()) {
                Text(
                    stringResource(com.example.core.R.string.All),
                    fontSize = 20.sp,
                    color = colorResource(com.example.core.R.color.accent),
                    modifier = Modifier.clickable {
                        onAllClicked.invoke()
                    })
            }
        }
        if (list.isNotEmpty()) {
            LazyRow {
                items(list) { movie ->
                    FilmPosterWNameGenre(movie) {
                        onMovieClicked(movie)
                    }
                }
            }
        } else {
            Text(
                text = "В этом разделе еще нет фильмов",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


/**
 * Экран фильмов пользователя.
 * Отображает такие разделы как:
 *  - Буду смотреть
 *  - Ваши оценки
 *  - Любимые
 *
 * @param onMovieClicked функция для навигации, вызывается при клике на любой из фильмов.
 */
@Composable
fun UsersScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onMovieClicked: (Movie) -> Unit = {},
    viewModel: UsersViewModel,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ) {

        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.WillWatch),
            list = viewModel.willWatch.collectAsState().value,
            onAllClicked = { },
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(title = stringResource(com.example.core.R.string.YourRates),
            list = viewModel.userRates.collectAsState().value,
            onAllClicked = { },
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(title = stringResource(com.example.core.R.string.Favourite),
            list = viewModel.favourites.collectAsState().value,
            onAllClicked = { },
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
    }
}