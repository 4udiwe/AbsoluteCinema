package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class FactEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var fact: String? = null,
    var type: String? = null,
    var spoiler: Boolean? = null,
    var movieId: Int? = null,
)
