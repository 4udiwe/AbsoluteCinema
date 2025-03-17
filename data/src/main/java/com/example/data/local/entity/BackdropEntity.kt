package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BackdropEntity(
    @PrimaryKey
    var backdropUrl        : String? = null,
    var backdropPreviewUrl : String? = null
)
