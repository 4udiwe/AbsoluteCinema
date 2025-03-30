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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absolutecinema.R

@Composable
private fun ActorItem(actor: Actor, modifier: Modifier = Modifier.padding(bottom = 4.dp)) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImageWithPlaceholder(
            actor.pictureURL,
            R.drawable.ic_launcher_background,
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
            Text(actor.name, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            Text(actor.role, color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun MovieScreen(paddingValues: PaddingValues = PaddingValues(), film: Film = Film()) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize().padding(paddingValues)
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
                film.posterURL,
                placeholderResId = R.drawable.ic_launcher_background,
                modifier = Modifier
                    .width(240.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = film.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                Text(
                    film.rating.toString(),
                    color = colorResource(R.color.score_green),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${(film.watches / 1000)}K",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 4.dp), fontWeight = FontWeight.Bold
                )
                Text(film.enname, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text(
                "${film.year}, ${film.genres}",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${film.country}, ${film.duration} мин, ${if (film.isForAdult) "18+" else ""}",
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
                IconButton(onClick = {

                }) {
                    Icon(Icons.Outlined.Star, "star", tint = MaterialTheme.colorScheme.primary)
                }
                Text("Оценить", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(Icons.TwoTone.Add, "star", tint = MaterialTheme.colorScheme.primary)
                }
                Text("Буду смотреть", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {

                }) {
                    Icon(Icons.Outlined.FavoriteBorder, "star", tint = MaterialTheme.colorScheme.primary)
                }
                Text("Избранное", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
            }
        }

        Text(
            film.description,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Все детали фильма",
            color = colorResource(R.color.accent),
            fontSize = 16.sp,
            modifier = Modifier.clickable {

            }
        )
        val actorsGroups = film.actors.chunked(3)

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Актеры", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp)
        ) {
            Text(text = "Интересные факты", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
            LazyRow {
                items(film.facts) { fact ->
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .height(100.dp)
                            .padding(end = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = fact,
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















