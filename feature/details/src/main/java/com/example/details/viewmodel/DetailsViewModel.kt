package com.example.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<Movie>(Movie())
    val movie = _movie.asStateFlow()

    fun updateMovie(movieId: Int) = viewModelScope.launch{
        _movie.value = repository.getMovieById(id = movieId)
    }

    fun setUserScore(score: Int) = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.setUserRateToMovie(id, rate = score)
            updateMovie(id)
        }
    }

    fun deleteUserScore() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.removeUserRateFromMovie(id)
            updateMovie(id)
        }
    }

    fun addToWillWatch() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.addMovieToWillWatch(id)
            updateMovie(id)
        }
    }

    fun removeFromWillWatch() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.removeMovieFromWillWatch(id)
            updateMovie(id)
        }
    }

    fun addToFavourite() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.addMovieToFavourites(id)
            updateMovie(id)
        }
    }

    fun removeFromFavourite() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.removeMovieFromFavourites(id)
            updateMovie(id)
        }
    }

}