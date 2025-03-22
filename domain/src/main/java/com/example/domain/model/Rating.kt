package com.example.domain.model

data class Rating(
    var kp                 : Double? = null,
    var imdb               : Double? = null,
    var tmdb               : Double? = null,
    var filmCritics        : Double? = null,
    var russianFilmCritics : Double? = null,
    var await              : Double? = null
)
