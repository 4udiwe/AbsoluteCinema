package com.example.feed.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedViewModel(
    repository: MovieRepository
): ViewModel() {
    private var _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedMovies = _recommendedMovies.asStateFlow()

    private var _recommendedSeries = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedSeries = _recommendedSeries.asStateFlow()

    private var _detectives = MutableStateFlow<List<Movie>>(emptyList())
    val detectives = _detectives.asStateFlow()

    private var _comedies = MutableStateFlow<List<Movie>>(emptyList())
    val comedies = _comedies.asStateFlow()

    private var _romans = MutableStateFlow<List<Movie>>(emptyList())
    val romans = _romans.asStateFlow()

    init {
        _recommendedMovies = repository.getRecommendedFilms() as MutableStateFlow<List<Movie>>
        _recommendedSeries = repository.getRecommendedSeries() as MutableStateFlow<List<Movie>>
        _detectives = repository.getDetectiveMovies() as MutableStateFlow<List<Movie>>
        _comedies = repository.getComedyMovies() as MutableStateFlow<List<Movie>>
        _romans = repository.getRomanMovies() as MutableStateFlow<List<Movie>>
    }
}