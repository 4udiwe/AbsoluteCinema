package com.example.search.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()
    private val _errorState = MutableStateFlow(false)
    val errorState = _errorState.asStateFlow()
    private val _emptyState = MutableStateFlow(false)
    val emptyState = _emptyState.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
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
        val history = sharedPreferences.getStringSet(SEARCH_HISTORY_KEY, setOf())?.toList() ?: emptyList()
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
        _isSearching.value = true
        _errorState.value = false
        _emptyState.value = false
        try {
            _movies.value = repository.searchMoviesByName(query).movies
            Log.d("Search", "query: $query")
        } catch (e: Exception) {
            Log.e("Search", e.message.toString())
            _errorState.value = true
            _movies.value = emptyList()
        } finally {
            if (!errorState.value && movies.value.isEmpty()) {
                _emptyState.value = true
            }
            _isSearching.value = false
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
    fun clearState(){
        _movies.value = emptyList()
        _errorState.value = false
        _emptyState.value = false
        _isSearching.value = false
        searchJob?.cancel()
    }
}