package com.example.search.ui

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
import androidx.compose.ui.unit.dp
import com.example.core.ui.CommonMovieItem
import com.example.domain.model.Movie
import com.example.search.viewmodel.SearchState
import com.example.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    viewModel: SearchViewModel,
    onMovieClicked: (Movie) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val searchState by viewModel.searchstate.collectAsState()
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

        when(searchState) {
            SearchState.Idle -> {
                Text(
                    text = "Ищите фильмы и сериалы по названию или фильтрам",
                    modifier = Modifier.padding(top = 16.dp)
                )
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
                Text(
                    text = "Ошибка ${(searchState as SearchState.Error).message}, попробуйте ещё раз",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            SearchState.Empty -> {
                Text(
                    text = "Ничего не найдено",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            is SearchState.Success -> {
                val movies = (searchState as SearchState.Success).movies
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp)
                ) {
                    items(movies) { movie ->
                        CommonMovieItem(movie = movie, onMovieClicked = onMovieClicked)
                    }
                }
            }
        }
    }
}
