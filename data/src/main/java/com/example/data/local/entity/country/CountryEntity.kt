package com.example.data.local.entity.country

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.data.local.entity.MovieEntity

@Entity(primaryKeys = ["id", "name"])
data class CountryEntity(
    var id: Int,
    var name: String,
)
