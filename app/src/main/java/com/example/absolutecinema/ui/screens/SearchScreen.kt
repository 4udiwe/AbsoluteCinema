package com.example.absolutecinema.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.absolutecinema.R


/**
 * Экран поиска.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(paddingValues: PaddingValues = PaddingValues()) {

    val query = rememberSaveable { mutableStateOf("") }
    val isActive = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            query = query.value,
            onQueryChange = { query.value = it },
            placeholder = {
                Text(
                    text = "Фильмы, сериалы, персоны",
                    color = colorResource(R.color.text_second)
                )
            },
            leadingIcon = {
                IconButton(onClick = {
                    //viewModel.search(query = query.value)
                }) {
                    Icon(Icons.Default.Search, "", tint = colorResource(R.color.text_second))
                }
            },
            trailingIcon = {
                Row {
                    AnimatedVisibility(
                        visible = isActive.value && query.value != "",
                        enter = slideInVertically(
                            initialOffsetY = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = 300)
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        IconButton(onClick = { query.value = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                "",
                                tint = colorResource(R.color.text_second)
                            )
                        }
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Default.Menu, "", tint = colorResource(R.color.text_second))
                    }
                }
            },
            onSearch = { },
            active = isActive.value,
            onActiveChange = {
                isActive.value = it
            },
            colors = SearchBarDefaults.colors(
                containerColor = colorResource(R.color.background_second),
                dividerColor = colorResource(R.color.background),
                inputFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.text),
                    unfocusedTextColor = colorResource(R.color.text)
                )
            )
        ) {
            if (
                true
                //viewModel.errorState.collectAsState().value
                ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Проблемы с соединением",
                            color = colorResource(R.color.text),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        TextButton(onClick = {
                            //viewModel.search(query.value)
                        }) {
                            Text(
                                "Обновить",
                                color = colorResource(R.color.accent),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                }
            } else if (
                false
                //viewModel.emptyState.collectAsState().value
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Ничего не найдено",
                            color = colorResource(R.color.text),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                }
            } else {
//                val moviesList = viewModel.movies.collectAsState().value
//                LazyColumn {
//                    items(moviesList) { movie ->
//                        Text(movie.name.toString())
//                    }
//                }
            }


        }
    }
}