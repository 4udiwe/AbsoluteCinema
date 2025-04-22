package com.example.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.core.R
import com.example.domain.model.Movie

/**
 * Функция для асинхронной загрузки постера фильма.
 * Пока постер не загружен (и если не будет загружен)
 * отображает плейсхолдер.
 *
 * @param imageUrl ссылка на постер фильма
 * @param placeholderResId ресурс плейсхолдера (по умолчанию poster_placeholder)
 * @param contentScale тип масштабирования контента (по умолчанию Fit)
 */
@Preview
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
@Composable
fun UserScore(movie: Movie, modifier: Modifier = Modifier.size(20.dp)) {
    val rating = movie.userRate
    val color = when (rating) {
        in 0..3 -> colorResource(R.color.score_red)
        in 4..6 -> colorResource(R.color.score_gray)
        else -> colorResource(R.color.score_green)
    }
    Box(modifier = modifier.background(color = color, shape = CircleShape)) {
        Text(
            text = rating.toString(),
            textAlign = TextAlign.Center,
            color = colorResource(R.color.white),
            modifier = Modifier.align(
                Alignment.Center
            )
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
fun FilmRating(movie: Movie, modifier: Modifier = Modifier.padding(top = 10.dp, start = 14.dp)) {
    val movieRating =
        if (movie.rating?.kp != null)
            movie.rating!!.kp
        else if (movie.rating?.imdb != null)
            movie.rating!!.imdb
        else if (movie.rating?.tmdb != null)
            movie.rating!!.tmdb
        else if (movie.rating?.filmCritics != null)
            movie.rating!!.filmCritics
        else if (movie.rating?.russianFilmCritics != null)
            movie.rating!!.russianFilmCritics
        else if (movie.rating?.await != null)
            movie.rating!!.await
        else
            0.0


    val color = when (movieRating?.toInt()) {
        in 0..3 -> colorResource(R.color.score_red)
        in 4..6 -> colorResource(R.color.score_gray)
        else -> colorResource(R.color.score_green)
    }
    Box(
        modifier =
        if (movie.top250 != null)
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