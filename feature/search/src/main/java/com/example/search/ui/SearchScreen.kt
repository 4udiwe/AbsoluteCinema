package com.example.search.ui

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.CommonMovieItem
import com.example.domain.model.Movie
import com.example.search.viewmodel.SearchState
import com.example.search.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    viewModel: SearchViewModel,
    onMovieClicked: (Movie) -> Unit,
    onFiltersMenuClicked: () -> Unit
) {
    val query = remember { mutableStateOf("") }
    val isSearchBarFocused = rememberSaveable { mutableStateOf(false) }
    val searchState by viewModel.searchState.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    //Поиск без нажатия кнопки с задержкой
    LaunchedEffect(query.value) {
        if (query.value == "") {
            viewModel.clearState()
        } else if (query.value.length >= 3) {
            viewModel.searchWithDebounce(query.value, launchedFromButton = false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            query = query.value,
            onQueryChange = { query.value = it },
            placeholder = {
                Text(
                    text = "Фильмы, сериалы, персоны", color = MaterialTheme.colorScheme.secondary
                )
            },
            leadingIcon = {
                IconButton(onClick = {
                    viewModel.searchWithDebounce(
                        query = query.value, launchedFromButton = true
                    )
                }) {
                    Icon(Icons.Default.Search, "", tint = MaterialTheme.colorScheme.secondary)
                }
            },
            trailingIcon = {
                Row {
                    AnimatedVisibility(
                        visible = isSearchBarFocused.value && query.value != "", enter = slideInVertically(
                            initialOffsetY = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = 300)
                        ), exit = slideOutVertically(
                            targetOffsetY = { fullWidth -> -fullWidth },
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        IconButton(onClick = {
                            query.value = ""
                        }) {
                            Icon(
                                Icons.Default.Clear, "", tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    IconButton(onClick = {
                        onFiltersMenuClicked.invoke()
                    }) {
                        Icon(Icons.Default.Menu, "", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            },
            onSearch = {
                viewModel.searchWithDebounce(
                    query = query.value, launchedFromButton = true
                )
            },
            active = isSearchBarFocused.value,
            onActiveChange = {
                isSearchBarFocused.value = it
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                dividerColor = MaterialTheme.colorScheme.background,
                inputFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                )
            )
        ) {
            //История поиска
            if (isSearchBarFocused.value && query.value.isEmpty()){
                Box {
                    LazyColumn {
                        items(searchHistory){
                            TextButton(
                                onClick = {query.value = it}
                            ) {
                                Text(text = it)
                            }
                        }
                        if (searchHistory.isNotEmpty()) {
                            item {
                                TextButton(
                                    onClick = {
                                        viewModel.clearSearchHistory()
                                    }
                                ) {
                                    Text("Очистить историю")
                                }
                            }
                        }
                    }
                }
            }
            when(searchState) {
                SearchState.Idle -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Ищите фильмы и сериалы по названию или фильтрам",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                SearchState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SearchState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Проблемы с соединением",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            TextButton(onClick = {
                                viewModel.searchWithDebounce(
                                    query = query.value, launchedFromButton = true
                                )
                            }) {
                                Text(
                                    "Обновить",
                                    color = colorResource(com.example.core.R.color.accent),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
                SearchState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Ничего не найдено",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                is SearchState.Success -> {
                    val movies = (searchState as SearchState.Success).movies
                    LazyColumn {
                        items(movies) { movie ->
                            CommonMovieItem(modifier = Modifier.height(120.dp).padding(vertical = 4.dp), movie = movie, onMovieClicked = onMovieClicked)
                        }
                    }
                }
            }
        }
    }
}


