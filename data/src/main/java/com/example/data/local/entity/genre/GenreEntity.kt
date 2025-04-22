package com.example.data.local.entity.genre

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id", "name"])
data class GenreEntity(
    var id: Int,
    var name: String,
)
