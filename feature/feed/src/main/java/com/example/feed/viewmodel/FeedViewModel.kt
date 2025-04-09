package com.example.feed.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedViewModel(
    private val repository: MovieRepository
): ViewModel() {

    val recommendedMovies = repository.getRecommendedFilms()

    val recommendedSeries = repository.getRecommendedSeries()

    var detectives = repository.getDetectiveMovies()

    var comedies = repository.getComedyMovies()

    var romans = repository.getRomanMovies()

}