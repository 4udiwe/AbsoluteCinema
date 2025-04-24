package com.example.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.Movie
import com.example.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    viewModel: SearchViewModel,
    onMovieClicked: (Movie) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val movies by viewModel.movies.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val emptyState by viewModel.emptyState.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Box {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    isDropdownExpanded = true
                    viewModel.searchWithDebounce(query, launchedFromButton = false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isDropdownExpanded = focusState.isFocused
                    },
                label = { Text(text = "Фильмы, сериалы, персоны") }
            )

            DropdownMenu(
                expanded = isDropdownExpanded && searchHistory.isNotEmpty(),
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                searchHistory.forEach { historyItem ->
                    DropdownMenuItem(
                        text = { Text(historyItem) },
                        onClick = {
                            query = historyItem
                            isDropdownExpanded = false
                            viewModel.searchWithDebounce(historyItem, launchedFromButton = true)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                isDropdownExpanded = false
                viewModel.searchWithDebounce(query, launchedFromButton = true)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Найти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isSearching -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorState -> {
                Text(
                    text = "Ошибка при поиске, попробуйте ещё раз.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            emptyState -> {
                Text(
                    text = "Ничего не найдено.",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp)
                ) {
                    items(movies) { movie ->
                        MovieItem(movie = movie, onMovieClicked = onMovieClicked)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClicked: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onMovieClicked(movie) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            movie.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorResource(com.example.core.R.color.accent)
                )
            }
        }
    }
}