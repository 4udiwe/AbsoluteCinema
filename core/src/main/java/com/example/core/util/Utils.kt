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

/**
 * Функция для получения рейтинга фильма.
 *
 * @return первый из встретившихся рейтингов, который не null и не 0.0
 */
fun Movie.getRating(): Double? {
    return if (rating?.kp != 0.0) rating!!.kp
    else if (rating?.imdb != 0.0) rating!!.imdb
    else if (rating?.tmdb != 0.0) rating!!.tmdb
    else if (rating?.filmCritics != 0.0) rating!!.filmCritics
    else if (rating?.russianFilmCritics != 0.0) rating!!.russianFilmCritics
    else if (rating?.await != 0.0) rating!!.await
    else null
}

/**
 *
 * @return форматированная длительность фильма или серии сериала в формате {часы}:{минуты}
 */
fun Movie.getDurationFormated(): String {
    return if (movieLength != null) "${movieLength!! / 60}:${(if (movieLength!! % 60 < 10) "0" + movieLength!! % 60 else movieLength!! % 60)}"
    else if (seriesLength != null) "${seriesLength!! / 60}:${(if (seriesLength!! % 60 < 10) "0" + seriesLength!! % 60 else seriesLength!! % 60)}"
    else ""
}