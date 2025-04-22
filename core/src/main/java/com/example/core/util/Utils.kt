package com.example.core.util

import com.example.domain.model.Movie

/**
 * Функция для получения названия фильма.
 *
 * @return name, enName или alternativeName фильма.
 */
fun Movie.getName(): String {
    return name ?: enName ?: alternativeName ?: "(Name missing)"
}