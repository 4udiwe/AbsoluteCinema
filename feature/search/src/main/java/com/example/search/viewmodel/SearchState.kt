package com.example.search.viewmodel

import com.example.domain.model.Movie

/**
 * Описывает состояние поиска фильмов.
 *
 *
 * @param [Idle] начальное состояние экрана
 * @param [Loading] состояние загрузки
 * @param [Success] состояние успеха, содержит список найденных фильмов [Movie]
 * @param [Error] состояние ошибки, содержит сообщение ошибки [String]
 * @param [Empty] пустое состояние, когда ни один фильм не был найден
 *
 */
sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val movies: List<Movie>) : SearchState()
    data class Error(val message: String) : SearchState()
    data object Empty : SearchState()
}