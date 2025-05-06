package com.example.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.R
import com.example.core.ui.LoadImageWithPlaceholder
import com.example.core.ui.MovieRatingWithWreath
import com.example.core.ui.UserScore
import com.example.core.util.getName
import com.example.details.viewmodel.DetailsViewModel
import com.example.domain.model.Movie
import com.example.domain.model.Person

/**
 * Отображает краткую информацию об актере на экране фильма (DetailsScreen)
 * Отображает фото актера, справа от фото имя и роль.
 *
 * @param actor актер типа [Person]
 */
@Composable
private fun ActorItem(actor: Person, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(bottom = 4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImageWithPlaceholder(
            imageUrl = actor.photo,
            placeholderResId = R.drawable.actor_placeholder,
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
            Text(actor.name ?: "", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            Text(
                actor.description ?: "",
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
    Icon(painter = if (score.intValue >= number + 1) painterResource(R.drawable.star)
    else painterResource(R.drawable.star_border),
        tint = if (score.intValue >= number + 1) colorResource(R.color.score_legend_start)
        else colorResource(R.color.text_second),
        contentDescription = "star",
        modifier = Modifier
            .clickable {
                score.intValue = number + 1
            }
            .size(28.dp))

}

/**
 * Диалог выставления оценки фильму
 *
 * @param dialogState стейт диалога
 * @param movieScore пользовательская оценка фильма, если есть
 * @param isSeries является ли сериалом (для правильного отображения надписи)
 * @param onSave сохраняет результат, записывает оценку фильма
 * @param onCancel убирает оценку
 */
@Preview
@Composable
private fun MovieScoreDialog(
    modifier: Modifier = Modifier,
    dialogState: MutableState<Boolean> = mutableStateOf(false),
    movieScore: Int? = null,
    isSeries: Boolean = false,
    onSave: (score: Int) -> Unit = {},
    onCancel: () -> Unit = {},
) {

    val score = remember { mutableIntStateOf(movieScore ?: -1) }

    Dialog(
        onDismissRequest = {
            dialogState.value = false
        }, properties = DialogProperties(
            dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = true
        )
    ) {
        Box(
            modifier = modifier.background(
                    color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp)
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
                    modifier = Modifier.padding(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    repeat(10) { number ->
                        Star(score = score, number = number)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(onClick = {
                        onSave(score.intValue)
                        dialogState.value = false
                    }) {
                        Text(
                            text = "Сохранить",
                            color = colorResource(R.color.accent),
                        )
                    }
                    TextButton(onClick = {
                        onCancel.invoke()
                        dialogState.value = false
                    }) {
                        Text(
                            text = "Убрать оценку",
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
    onDescriptionClicked: (Movie) -> Unit,
) {

    val movie by viewModel.movie.collectAsState()

    val dialogState = remember { mutableStateOf(false) }


    if (dialogState.value) {
        MovieScoreDialog(dialogState = dialogState,
            movieScore = movie.userRate,
            isSeries = movie.isSeries == true,
            onSave = { score ->
                viewModel.setUserScore(score = score)
            },
            onCancel = {
                viewModel.deleteUserScore()
            })
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
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LoadImageWithPlaceholder(
                    imageUrl = movie.backdrop?.backdropUrl,
                    modifier = Modifier.fillMaxWidth().height(500.dp).alpha(0.5f).align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )
                LoadImageWithPlaceholder(
                    imageUrl = movie.poster?.posterUrl,
                    placeholderResId = R.drawable.poster_placeholder,
                    modifier = Modifier.width(240.dp).align(Alignment.BottomCenter),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                text = movie.getName(),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                MovieRatingWithWreath(movie = movie)
            }
            Text(
                "${
                    if (movie.status == "announced") "Анонсирован," 
                    else if (movie.year != null) movie.year.toString() + "," 
                    else ""
                } ${movie.genres.map { it.name }.joinToString()}",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${
                    movie.countries.map { it.name }.joinToString()
                } ${
                    if (movie.movieLength != null) movie.movieLength.toString() + " мин" else ""
                }${
                    if (movie.ageRating != null) ", " + movie.ageRating.toString() + "+" else ""
                }", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 70.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(onClick = {
                    dialogState.value = true
                }

                ) {
                    if (movie.userRate == null) {
                        Icon(
                            Icons.Outlined.Star, "star", tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        UserScore(
                            movie = movie, modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Text(
                    text = if (movie.userRate == null) "Оценить" else "Ваша оценка",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            }
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if (movie.isWillWatch) viewModel.removeFromWillWatch()
                    else viewModel.addToWillWatch()
                }) {
                    Icon(
                        painter = if (movie.isWillWatch) painterResource(R.drawable.bookmark_filled)
                        else painterResource(R.drawable.bookmark),
                        contentDescription = "willwach",
                        tint = colorResource(R.color.accent)
                    )
                }
                Text("Буду смотреть", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if (movie.isFavorite) viewModel.removeFromFavourite()
                    else viewModel.addToFavourite()
                }) {
                    Icon(
                        imageVector = if (movie.isFavorite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = "favourite",
                        tint = colorResource(R.color.accent)
                    )
                }
                Text("Избранное", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
        }

        if (movie.shortDescription != null || movie.description != null) {
            Text(
                text = (movie.shortDescription ?: movie.description).toString(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = "Все детали фильма",
                color = colorResource(R.color.accent),
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    onDescriptionClicked(movie)
                })
        } else {
            Text(
                text = "Нет описания",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }



        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Актеры",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (movie.persons.isNotEmpty()) {
                val actorsGroups = movie.persons.chunked(3)
                LazyRow {
                    items(actorsGroups) { group ->
                        Column {
                            group.forEach { actor ->
                                ActorItem(actor = actor)
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Нет информации об актерах",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
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

            if (movie.facts.isNotEmpty()) {
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
            } else {
                Text(
                    text = "Нет информации о фактах",
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
}















