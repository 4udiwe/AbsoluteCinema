package com.example.details.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.ui.LoadImageWithPlaceholder
import com.example.details.viewmodel.DetailsViewModel
import com.example.domain.model.Person

/**
 * Отображает краткую информацию об актере на экране фильма (DetailsScreen)
 * Отображает фото актера, справа от фото имя и роль.
 *
 * @param actor актер типа [Person]
 */
@Composable
private fun ActorItem(actor: Person, modifier: Modifier = Modifier.padding(bottom = 4.dp)) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImageWithPlaceholder(
            imageUrl = actor.photo,
            placeholderResId = com.example.core.R.drawable.actor_placeholder,
            modifier = Modifier
                .height(90.dp)
                .width(60.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .width(160.dp)
                .padding(start = 4.dp, end = 16.dp)
        ) {
            Text(actor.name.toString(), color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            Text(
                actor.description.toString(),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Функция отображения звездочки в MovieScoreDialog
 */
@Composable
private fun Star(score: MutableIntState, number: Int) {
    Icon(
        painter =
        if (score.intValue >= 1)
            painterResource(com.example.core.R.drawable.star)
        else
            painterResource(com.example.core.R.drawable.star_border),
        tint =
        if (score.intValue >= 1)
            colorResource(com.example.core.R.color.score_legend_start)
        else
            colorResource(com.example.core.R.color.text_second),
        contentDescription = "star",
        modifier = Modifier
            .clickable {
                score.intValue = number
            }
            .size(28.dp)
    )

}

/**
 * Диалог выставления оценки фильму
 *
 * @param dialogState стейт диалога
 * @param isSeries является ли сериалом (для правильного отображения надписи)
 * @param onSave сохраняет результат, записывает оценку фильма
 * @param onCancel убирает оценку
 */
@Composable
private fun MovieScoreDialog(
    modifier: Modifier = Modifier,
    dialogState: MutableState<Boolean>,
    isSeries: Boolean = false,
    onSave: (score: Int) -> Unit,
    onCancel: () -> Unit,
) {

    val score = remember { mutableIntStateOf(-1) }

    Dialog(
        onDismissRequest = {
            dialogState.value = false
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Оцените ${if (isSeries) "сериал" else "фильм"}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(10) { number ->
                        Star(score = score, number = number)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = {
                            onSave(score.intValue)
                        }
                    ) {
                        Text(
                            text = "Сохранить",
                            color = colorResource(com.example.core.R.color.accent),
                        )
                    }
                    TextButton(
                        onClick = {
                            onCancel.invoke()
                        }
                    ) {
                        Text(
                            text = "Отмена",
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: DetailsViewModel,
) {

    val movie by viewModel.movie.collectAsState()

    val dialogState = remember { mutableStateOf(false) }

    if (dialogState.value) {
        MovieScoreDialog(
            dialogState = dialogState,
            isSeries = movie.isSeries == true,
            onSave = { score ->
                viewModel.setUserScore(score = score)
            },
            onCancel = {
                viewModel.deleteUserScore()
            }
        )
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadImageWithPlaceholder(
                imageUrl = movie.poster?.posterUrl,
                placeholderResId = com.example.core.R.drawable.poster_placeholder,
                modifier = Modifier
                    .width(240.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = movie.name.toString(),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                Text(
                    movie.rating?.kp.toString(),
                    color = colorResource(com.example.core.R.color.score_green),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    movie.enName.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "${movie.year}, ${movie.genres.joinToString()}",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${movie.countries.joinToString()}, ${movie.movieLength} мин, ${movie.ageRating.toString()}+",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        dialogState.value = true
                    }

                ) {
                    Icon(Icons.Outlined.Star, "star", tint = MaterialTheme.colorScheme.primary)
                }
                Text("Оценить", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        if (movie.isWillWatch)
                            viewModel.removeFromWillWatch()
                        else
                            viewModel.addToWillWatch()
                    }
                ) {
                    Icon(
                        painter =
                        if (movie.isWillWatch)
                            painterResource(com.example.core.R.drawable.bookmark_filled)
                        else
                            painterResource(com.example.core.R.drawable.bookmark),
                        contentDescription = "willwach",
                        tint = colorResource(com.example.core.R.color.accent)
                    )
                }
                Text("Буду смотреть", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    if (movie.isFavorite)
                        viewModel.removeFromFavourite()
                    else
                        viewModel.addToFavourite()
                }) {
                    Icon(
                        imageVector =
                        if (movie.isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Filled.FavoriteBorder,
                        contentDescription = "favourite",
                        tint = colorResource(com.example.core.R.color.accent)
                    )
                }
                Text("Избранное", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
        }

        Text(
            text = (movie.shortDescription ?: movie.description).toString(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Все детали фильма",
            color = colorResource(com.example.core.R.color.accent),
            fontSize = 16.sp,
            modifier = Modifier.clickable {

            }
        )
        val actorsGroups = movie.persons.chunked(3)

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Актеры",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow {
                items(actorsGroups) { group ->
                    Column {
                        group.forEach { actor ->
                            ActorItem(actor = actor)
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
        ) {
            Text(
                text = "Интересные факты",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow {
                items(movie.facts) { fact ->
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .height(100.dp)
                            .padding(end = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "${if (fact.spoiler == true) "Спойлер: " else ""} ${fact.fact.toString()}",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(10.dp),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

    }
}















