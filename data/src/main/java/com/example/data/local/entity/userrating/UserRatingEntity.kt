package com.example.data.local.entity.userrating

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRatingEntity(
    @PrimaryKey
    val movieId: Int,
    val userRate: Int
)
