package com.example.data.local.entity.genre

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
)
