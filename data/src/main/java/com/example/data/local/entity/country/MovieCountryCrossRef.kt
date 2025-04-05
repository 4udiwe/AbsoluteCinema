package com.example.data.local.entity.country

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "countryId"])
data class MovieCountryCrossRef(
    val movieId: Int,
    val countryId: Int
)
