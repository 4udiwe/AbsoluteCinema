package com.example.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsersViewModel(
    repository: MovieRepository,
) : ViewModel() {

    val willWatch = repository.getWillWatchMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val userRates = repository.getMoviesWithUserRate()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val favourites = repository.getFavouriteMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}