package com.example.feed.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.core.ui.CommonMovieListScreen
import com.example.domain.model.Movie
import com.example.feed.viewmodel.FeedViewModel

@Composable
fun AllRecommendedSeriesScreen(
    paddingValues: PaddingValues,
    viewModel: FeedViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit
) {
    val title = stringResource(com.example.core.R.string.SerialsForYou)
    val list = viewModel.recommendedSeries.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked
    )
}