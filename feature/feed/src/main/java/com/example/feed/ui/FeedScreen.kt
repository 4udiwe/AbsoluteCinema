package com.example.feed.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.FilmRatingBox
import com.example.core.ui.LoadImageWithPlaceholder
import com.example.core.util.getName
import com.example.domain.model.Movie
import com.example.feed.viewmodel.FeedViewModel

private const val POSTER_WIDTH = 133
private const val POSTER_HEIGHT = 200

/**
 * Отображает постер фильма с названием на главном экране
 *
 * @param movie фильм
 * @param onMovieClicked вызывается при нажатии на постер
 */
@Composable
private fun FilmPosterWName(movie: Movie, onMovieClicked: (Movie) -> Unit) {
    Column(
        modifier = Modifier
            .width(POSTER_WIDTH.dp)
            .padding(end = 8.dp)
            .clickable {
                onMovieClicked(movie)
            }
    ) {
        Box {
            LoadImageWithPlaceholder(
                imageUrl = movie.poster?.posterUrl,
                placeholderResId = com.example.core.R.drawable.poster_placeholder,
                modifier = Modifier
                    .height(POSTER_HEIGHT.dp)
                    .width(POSTER_WIDTH.dp),
                contentScale = ContentScale.Crop
            )
            FilmRatingBox(movie)
        }
        Text(
            movie.getName(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (movie.year != null) {
            Text(
                "(${movie.year})",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Отображает горизонтальный список фильмов из постеров определенной категории
 *
 * @param title название категории, отображается над списком
 * @param list список отображаемых фильмов
 * @param onAllClicked вызывается при нажатии на кнопку "все" (навигация на экран с полным списокм из категории)
 * @param onMovieClicked вызывается при нажатии на постер фильма
 */
@Composable
private fun HorizontalRowWTitleBig(
    title: String,
    list: List<Movie>,
    onAllClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp).height(320.dp)
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
                stringResource(com.example.core.R.string.All),
                fontSize = 20.sp,
                color = colorResource(com.example.core.R.color.accent),
                modifier = Modifier.clickable {
                    onAllClicked.invoke()
                })
        }
        LazyRow {
            items(list.take(10)) { film ->
                FilmPosterWName(movie = film, onMovieClicked = onMovieClicked)
            }
        }
    }
}

/**
 * Главный экран приложения - отображает рекомендации и определенные категории фильмов
 *
 *
 * @param onMovieClicked вызывается при нажатии на постер фильма (навигация на экран фильма DetailsScreen).
 * @param onAllRecommendedMoviesClicked вызывается при клике на кнопку "все" соответствующего списка.
 * @param onAllRecommendedSeriesClicked вызывается при клике на кнопку "все" соответствующего списка.
 * @param onAllDetectiveMoviesClicked вызывается при клике на кнопку "все" соответствующего списка.
 * @param onAllComedyMoviesClicked вызывается при клике на кнопку "все" соответствующего списка.
 * @param onAllRomanMoviesClicked вызывается при клике на кнопку "все" соответствующего списка.
 */
@Composable
fun FeedScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: FeedViewModel,
    onMovieClicked: (Movie) -> Unit = {},
    onAllRecommendedMoviesClicked: () -> Unit,
    onAllRecommendedSeriesClicked: () -> Unit,
    onAllDetectiveMoviesClicked: () -> Unit,
    onAllComedyMoviesClicked: () -> Unit,
    onAllRomanMoviesClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val recommendedMovies = viewModel.recommendedMovies.collectAsState(emptyList())
    val recommendedSeries = viewModel.recommendedSeries.collectAsState(emptyList())
    val detectives = viewModel.detectives.collectAsState(emptyList())
    val romans = viewModel.romans.collectAsState(emptyList())
    val comedies = viewModel.comedies.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState),

        ) {
        val filmsForYouTitle = stringResource(com.example.core.R.string.FilmsForYou)
        HorizontalRowWTitleBig(
            title = filmsForYouTitle, recommendedMovies.value,
            onAllClicked = {
                onAllRecommendedMoviesClicked.invoke()
            },
            onMovieClicked = onMovieClicked
        )

        val seriesForYouTitle = stringResource(com.example.core.R.string.SerialsForYou)
        HorizontalRowWTitleBig(title = seriesForYouTitle, recommendedSeries.value,
            onAllClicked = {
                onAllRecommendedSeriesClicked.invoke()
            },
            onMovieClicked = onMovieClicked
        )

        val detectivesTitle = stringResource(com.example.core.R.string.Detectives)
        HorizontalRowWTitleBig(title = detectivesTitle, detectives.value,
            onAllClicked = {
                onAllDetectiveMoviesClicked.invoke()
            },
            onMovieClicked = onMovieClicked
        )

        val comediesTitle = stringResource(com.example.core.R.string.Comedys)
        HorizontalRowWTitleBig(title = comediesTitle, comedies.value,
            onAllClicked = {
                onAllComedyMoviesClicked.invoke()
            },
            onMovieClicked = onMovieClicked
        )

        val romansTitle = stringResource(com.example.core.R.string.Romans)
        HorizontalRowWTitleBig(title = romansTitle, romans.value,
            onAllClicked = {
                onAllRomanMoviesClicked.invoke()
            },
            onMovieClicked = onMovieClicked
        )
    }
}