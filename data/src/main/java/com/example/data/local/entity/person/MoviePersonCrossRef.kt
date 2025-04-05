package com.example.data.local.entity.person

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "personId"])
data class MoviePersonCrossRef(
    val movieId: Int,
    val personId: Int
)
