package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RatingEntity(
    @PrimaryKey
    var kp                 : Double? = null,
    var imdb               : Double? = null,
    var tmdb               : Double? = null,
    var filmCritics        : Double? = null,
    var russianFilmCritics : Double? = null,
    var await              : Double? = null
)
