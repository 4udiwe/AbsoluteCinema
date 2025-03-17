package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PosterEntity(
    @PrimaryKey
    var posterUrl        : String? = null,
    var posterPreviewUrl : String? = null
)
