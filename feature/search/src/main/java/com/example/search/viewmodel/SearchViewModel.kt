package com.example.search.viewmodel

import android.content.SharedPreferences
import android.util.Log
import android.util.Range
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Filter
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState = _searchState.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    private val _filters = MutableStateFlow(SearchFilters())
    val filters = _filters.asStateFlow()

    private val _filtersForSearch = MutableStateFlow(SearchFilters())
    val filtersForSearch = _filtersForSearch.asStateFlow()


    // Ключ для хранения истории в SharedPreferences
    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
    }

    // Загрузка истории поиска из SharedPreferences
    init {
        loadSearchHistory()
        loadFilters()
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
            if (result.total == 0) _searchState.value = SearchState.Empty
            else _searchState.value = SearchState.Success(movies = result.movies)
        } catch (e: Exception) {
            _searchState.value = SearchState.Error(message = e.message ?: "Unknown error")
            Log.e("Search", e.message.toString())
        }
    }

    private var searchJob: Job? = null
    fun searchWithDebounce(query: String, launchedFromButton: Boolean) = viewModelScope.launch {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (!launchedFromButton) {
                delay(2000)
            }
            search(query = query)
            addToSearchHistory(query = query)
        }
    }

    fun clearState() {
        _searchState.value = SearchState.Idle
        searchJob?.cancel()
    }

    fun searchWithFilters() = viewModelScope.launch {
        searchJob?.cancel()

        _searchState.value = SearchState.Loading
        try {
            val result = repository.searchMoviesWithFilters(
                types = _filtersForSearch.value.types.map { it.name },
                years = listOf("${_filtersForSearch.value.years.lower}-${_filtersForSearch.value.years.upper}"),
                kpRating = listOf("${_filtersForSearch.value.rating.lower}-${_filtersForSearch.value.rating.upper}"),
                genres = _filtersForSearch.value.genres.map { it.name },
                countries = _filtersForSearch.value.countries.map { it.name },
                fields = null,
                sortTypes = null,
                isSeries = null,
                inCollection = null,
            )
            if (result.total == 0) _searchState.value = SearchState.Empty
            else _searchState.value = SearchState.Success(movies = result.movies)

        } catch (e: Exception) {
            _searchState.value = SearchState.Error(message = e.message ?: "Unknown error")
            Log.e("Search", e.message.toString())
        }
    }

    //загрузка фильтров для поиска
    private fun loadFilters() = viewModelScope.launch {
        _filters.value.countries = repository.getCountryFiltersForSearch().toMutableList()
        _filters.value.genres = repository.getGenreFiltersForSearch().toMutableList()
        _filters.value.types = repository.getTypeFiltersForSearch().toMutableList()
    }

    fun setYearsFilter(range: Range<Float>) {
        _filtersForSearch.value.years = range
    }

    fun setRatingFilter(range: Range<Float>) {
        _filtersForSearch.value.rating = range
    }

    fun addTypeFilter(type: Filter) {
        _filtersForSearch.update { searchFilters ->
            searchFilters.copy(types = searchFilters.types + type)
        }
    }

    fun removeTypeFilter(type: Filter) {
        _filtersForSearch.update { searchFilters ->
            searchFilters.copy(types = searchFilters.types.filter {
                it != type
            })
        }
    }

    fun addCountryFilter(country: Filter) {
        _filtersForSearch.update { searchFilters ->
            searchFilters.copy(countries = searchFilters.countries + country)
        }
    }

    fun removeCountryFilter(country: Filter) {
        _filtersForSearch.update { searchFilters ->
            searchFilters.copy(countries = searchFilters.countries.filter {
                it != country
            })
        }
    }

    fun addGenreFilter(genre: Filter) {
        _filtersForSearch.update { searchFilters ->
            searchFilters.copy(genres = searchFilters.genres + genre)
        }
    }

    fun removeGenreFilter(genre: Filter) {
        _filtersForSearch.update { searchFilters ->
            searchFilters.copy(genres = searchFilters.genres.filter {
                it != genre
            })
        }
    }


}