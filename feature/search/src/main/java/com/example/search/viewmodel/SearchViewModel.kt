package com.example.search.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchstate = _searchState.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    // Ключ для хранения истории в SharedPreferences
    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
    }

    // Загрузка истории поиска из SharedPreferences
    init {
        loadSearchHistory()
    }

    // Загрузка истории поиска
    private fun loadSearchHistory() {
        val history =
            sharedPreferences.getStringSet(SEARCH_HISTORY_KEY, setOf())?.toList() ?: emptyList()
        _searchHistory.value = history
    }

    // Сохранение истории поиска
    private fun saveSearchHistory() {
        val historySet = _searchHistory.value.toSet()
        sharedPreferences.edit().putStringSet(SEARCH_HISTORY_KEY, historySet).apply()
    }

    // Добавление нового запроса в историю
    private fun addToSearchHistory(query: String) {
        val updatedHistory = (_searchHistory.value + query).takeLast(10)
        _searchHistory.value = updatedHistory
        saveSearchHistory()
    }

    // Очистка истории поиска
    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    private suspend fun search(query: String) {
        _searchState.value = SearchState.Loading
        try {
            val result = repository.searchMoviesByName(query)
            if (result.total == 0)
                _searchState.value = SearchState.Empty
            else
                _searchState.value = SearchState.Success(movies = result.movies)
        } catch (e: Exception) {
            _searchState.value = SearchState.Error(message = e.message ?: "Unknown error")
            Log.e("Search", e.message.toString())
        }
    }

    private var searchJob: Job? = null
    fun searchWithDebounce(query: String, launchedFromButton: Boolean) = viewModelScope.launch {
        searchJob?.cancel()
        Log.d("Search", "Debounce began")
        searchJob = viewModelScope.launch {
            if (!launchedFromButton) {
                delay(2000)
            }
            Log.d("Search", "Debounce ended, searching...")
            search(query = query)
            addToSearchHistory(query = query)
        }
    }

    fun clearState() {
        _searchState.value = SearchState.Idle
        searchJob?.cancel()
    }
}