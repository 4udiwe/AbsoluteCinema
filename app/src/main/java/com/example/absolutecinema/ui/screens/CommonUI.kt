package com.example.absolutecinema.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.absolutecinema.R

@Composable
fun LoadImageWithPlaceholder(
    imageUrl: String?, // URL изображения
    placeholderResId: Int, // Ресурс заглушки (например, R.drawable.placeholder)
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    // Создаем painter для загрузки изображения
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        placeholder = painterResource(id = placeholderResId) // Устанавливаем заглушку
    )

    // Отображаем изображение
    AsyncImage(
        model = imageUrl,
        contentDescription = null, // Описание для accessibility
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(id = placeholderResId), // Заглушка
        error = painterResource(id = placeholderResId) // Заглушка в случае ошибки
    )
}

@Composable
fun UserScore(film: Film, modifier: Modifier = Modifier.size(20.dp)) {
    val rating = film.userScore
    val color = when (rating) {
        in 0..3 -> colorResource(R.color.score_red)
        in 4..6 -> colorResource(R.color.score_gray)
        else -> colorResource(R.color.score_green)
    }
    Box(modifier = modifier.background(color = color, shape = CircleShape)) {
        Text(
            text = rating.toString(),
            color = colorResource(R.color.white),
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}


@SuppressLint("ResourceType")
@Composable
fun FilmRating(film: Film, modifier: Modifier = Modifier.padding(top = 10.dp, start = 14.dp)) {
    val color = when (film.rating.toInt()) {
        in 0..3 -> colorResource(R.color.score_red)
        in 4..6 -> colorResource(R.color.score_gray)
        else -> colorResource(R.color.score_green)
    }
    Box(
        modifier =
        if (!film.isTop100)
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
            film.rating.toString(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 4.dp),
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.white),
            fontSize = 14.sp
        )
    }
}


