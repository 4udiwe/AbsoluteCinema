package com.example.data.local.entity.category

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "categoryId"])
data class MovieCategoryCrossRef(
    val movieId: Int,
    val categoryId: Int
)
