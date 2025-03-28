package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRating(
    @PrimaryKey
    val movieId: Int,
    val userRate: Int
)
