package com.example.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.core.R
import com.example.core.util.getDurationFormated
import com.example.core.util.getName
import com.example.core.util.getRating
import com.example.domain.model.Movie
import com.example.domain.model.Rating

/**
 * Функция для асинхронной загрузки постера фильма.
 * Пока постер не загружен (и если не будет загружен)
 * отображает плейсхолдер.
 *
 * @param imageUrl ссылка на постер фильма
 * @param placeholderResId ресурс плейсхолдера (по умолчанию poster_placeholder)
 * @param contentScale тип масштабирования контента (по умолчанию Fit)
 */
@Composable
fun LoadImageWithPlaceholder(
    imageUrl: String? = "",
    placeholderResId: Int = R.drawable.poster_placeholder,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(id = placeholderResId), // Заглушка
        error = painterResource(id = placeholderResId) // Заглушка в случае ошибки
    )
}

/**
 * Отображает локальную оценку пользователя в кружке соответствующего оценке цвета.
 *
 * @param movie фильм, оценка которого отображается
 */
@Preview
@Composable
fun UserScore(movie: Movie = Movie(userRate = 10), modifier: Modifier = Modifier) {
    val rating = movie.userRate ?: return
    val color = when (rating) {
        in 0..3 -> colorResource(R.color.score_red)
        in 4..6 -> colorResource(R.color.score_gray)
        else -> colorResource(R.color.score_green)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(color = color, shape = CircleShape)
    ) {
        Text(
            text = rating.toString(),
            textAlign = TextAlign.Center,
            color = colorResource(R.color.white)
        )
    }
}


/**
 * Отображет оценку фильма по версии кинопоиска в небольшом прямоугольнике соответствующего цвета.
 * Если фильм входит в топ 250, то цвет прямоугольника становится золотистым.
 *
 * @param movie фильм, оценку которого отображается.
 */
@SuppressLint("ResourceType")
@Composable
fun FilmRatingBox(movie: Movie, modifier: Modifier = Modifier.padding(top = 10.dp, start = 14.dp)) {
    val movieRating = movie.getRating() ?: return

    val color = when (movieRating.toInt()) {
        in 0..3 -> colorResource(R.color.score_red)
        in 4..6 -> colorResource(R.color.score_gray)
        else -> colorResource(R.color.score_green)
    }
    Box(
        modifier =
        if (movie.top250 == null || movie.top250 == 0)
            modifier.background(color = color, shape = RoundedCornerShape(8.dp))
        else
            modifier.background(
                brush =
                Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.score_legend_start),
                        colorResource(R.color.score_legend_end)
                    ),
                    start = Offset(50.0f, 0f),
                    end = Offset(50.0f, 100f)
                ), shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            movieRating.toString(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.white),
            fontSize = 14.sp
        )
    }
}

/**
 * Функция для отображения вхождения фильма в топ 250. Отображает его место в списке и иконку.
 *
 * @param place место фильма в списке.
 */
@Composable
fun WreathOfTop250(modifier: Modifier = Modifier.padding(4.dp), place: Int = 249) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = place.toString(),
            color = colorResource(R.color.score_legend_start)
        )
        Icon(
            modifier = modifier.size(32.dp),
            painter = painterResource(R.drawable.top250_wreath),
            contentDescription = "",
            tint = colorResource(R.color.score_legend_end).copy(alpha = 0.8f)
        )
    }
}

/**
 * Отображает рейтинг фильма соответствующего цвета и венок с местом в топ 250
 *
 * @param movie
 */
@Composable
fun MovieRatingWithWreath(movie: Movie, modifier: Modifier = Modifier) {
    val movieRating = movie.getRating()
    if (movieRating != null) {
        val color = when (movieRating.toInt()) {
            in 0..3 -> colorResource(R.color.score_red)
            in 4..6 -> colorResource(R.color.score_gray)
            else -> colorResource(R.color.score_green)
        }
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (movie.top250 != null && movie.top250!! > 0) {
                WreathOfTop250(place = movie.top250!!)
            }
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = movieRating.toString(),
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Общий эленмент фильма для списков и экрана поиска
 *
 * @param modifier
 */
@Preview(showSystemUi = true)
@Composable
fun CommonMovieItem(
    movie: Movie = Movie(
        year = 1990,
        enName = "English name",
        userRate = 10,
        rating = Rating(kp = 7.9),
        top250 = 123,
        movieLength = 124
    ),
    modifier: Modifier = Modifier.height(120.dp),
    onMovieClicked: (Movie) -> Unit = {}
) {
    Row(
        modifier = modifier.clickable {
            onMovieClicked(movie)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImageWithPlaceholder(
            imageUrl = movie.poster?.posterUrl,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 10.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                ) {
                    Text(
                        text = movie.getName(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Bottom)
                    )
                    UserScore(
                        movie, modifier = Modifier
                            .align(Alignment.Top)
                            .padding(10.dp)
                            .size(24.dp)
                    )
                }
                Text(
                    text = "${if (movie.enName != null) movie.enName + " " else ""}${if (movie.year != null) "(" + movie.year + ")" else ""}",
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = movie.genres.map { it.name }.joinToString(),
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MovieRatingWithWreath(movie = movie)

                    Row(
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(
                            text = movie.countries.map { it.name }.joinToString(),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(0.8f).padding(end = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Right
                        )
                        Text(
                            text = movie.getDurationFormated(),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}
