package com.example.data.local.entity.similar

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "similarId"])
data class MovieSimilarCrossRef(
    val movieId: Int,
    val similarId: Int
)
