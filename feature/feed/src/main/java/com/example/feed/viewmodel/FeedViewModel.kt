package com.example.feed.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.repository.MovieRepository


class FeedViewModel(
    private val repository: MovieRepository
): ViewModel() {

    val recommendedMovies = repository.getRecommendedFilms()

    val recommendedSeries = repository.getRecommendedSeries()

    var detectives = repository.getDetectiveMovies()

    var comedies = repository.getComedyMovies()

    var romans = repository.getRomanMovies()

}