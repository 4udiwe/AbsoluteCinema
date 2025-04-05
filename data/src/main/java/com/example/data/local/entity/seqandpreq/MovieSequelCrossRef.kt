package com.example.data.local.entity.seqandpreq

import androidx.room.Entity


@Entity(primaryKeys = ["movieId", "sequelId"])
data class MovieSequelCrossRef(
    val movieId: Int,
    val sequelId: Int
)
