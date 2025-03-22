package com.example.domain.model

/**
 * [LocalCategory] используется для определения того,
 * в какую категорию фильм должен попасть на устройстве
 * когда приходит из сети.
 *
 * Каждый фильм может иметь несколько категорий, например: [RecomendedFilms], [Detectives], [Favourite]
 *
 * Данный класс используется в качестве строкового поля в CategoryEntity.
 */
enum class LocalCategory {
    RecomendedFilms, RecomendedSeries, Detectives, Romans, Comedys, WillWatch, Favourite
}