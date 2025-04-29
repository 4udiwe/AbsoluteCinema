package com.example.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.CommonMovieItem
import com.example.core.ui.CommonMovieListScreen
import com.example.domain.model.Movie
import com.example.search.R
import com.example.search.viewmodel.SearchState
import com.example.search.viewmodel.SearchViewModel

@Composable
fun FilterSearchResultScreen(
    viewModel: SearchViewModel,
    paddingValues: PaddingValues,
    onBackClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit
) {

    val searchState by viewModel.searchState.collectAsState()

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
                        viewModel.searchWithFilters()
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
                        "По таким фильтрам Ничего не найдено",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    TextButton(
                        onClick = {
                            onBackClicked.invoke()
                        }
                    ) {
                        Text(
                            "Назад",
                            color = colorResource(com.example.core.R.color.accent)
                        )
                    }
                }
            }
        }
        is SearchState.Success -> {
            val movies = (searchState as SearchState.Success).movies
            CommonMovieListScreen(
                paddingValues = paddingValues,
                title = "Результаты поиска",
                moviesList = movies,
                onBackClicked = onBackClicked,
                onMovieClicked = onMovieClicked
            )
        }
    }
}