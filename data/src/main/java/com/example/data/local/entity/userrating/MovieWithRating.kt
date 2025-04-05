package com.example.data.local.entity.userrating

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.data.local.entity.MovieEntity

/**
 * Класс используется для возврата всех данных из таблицы UserRatingEntity
 *
 * @property movie entity фильма
 * @property userRating оценка фильма
 */
data class MovieWithRating(
    @Embedded
    val movie: MovieEntity,

    @ColumnInfo(name = "userRate")
    val userRating: Int
)
