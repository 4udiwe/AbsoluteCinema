package com.example.data.local.entity.fact

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.data.local.entity.MovieEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FactEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var fact: String? = null,
    var type: String? = null,
    var spoiler: Boolean? = null,
    val movieId: Int
)
