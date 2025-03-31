package com.example.users.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersViewModel(
    repository: MovieRepository,
) : ViewModel() {

    private var _willWatch = MutableStateFlow<List<Movie>>(emptyList())
    val willWatch = _willWatch.asStateFlow()

    private var _userRates = MutableStateFlow<List<Movie>>(emptyList())
    val userRates = _userRates.asStateFlow()

    private var _favourites = MutableStateFlow<List<Movie>>(emptyList())
    val favourites = _favourites.asStateFlow()

    init {
        _willWatch = repository.getWillWatchMovies() as MutableStateFlow<List<Movie>>
        _userRates = repository.getMoviesWithUserRate() as MutableStateFlow<List<Movie>>
        _favourites = repository.getFavouriteMovies() as MutableStateFlow<List<Movie>>
    }

}