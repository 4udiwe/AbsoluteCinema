package com.example.absolutecinema.ui.screens.dummy

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum


/**
 * УДАЛИТЬ ПОСЛЕ РЕАЛИЗАЦИИ DATA MODULE
 *
 * Заглушка для UI, отображает информацию о фильме.
 */
data class Film(
    val name: String = "Название фильма",
    val enname: String = "Film name",
    val posterURL: String = "",
    val year: Int = 1990,
    val duration: Int = 200,
    val isForAdult: Boolean = true,
    val genres: String = "драма, криминал",
    val description: String = LoremIpsum(99).values.joinToString(),
    val userScore: Int = 8,
    val rating: Float = 7.2f,
    val watches: Int = 400_000,
    val country: String = "США",
    val actors: List<Actor> = List(10) { Actor() },
    val facts: List<String> = List(5) { LoremIpsum(20).values.joinToString() },
    val isTop100: Boolean = true,
)