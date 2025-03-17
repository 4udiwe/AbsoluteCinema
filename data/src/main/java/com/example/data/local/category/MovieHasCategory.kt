package com.example.data.local.category

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.data.local.entity.MovieEntity

@Entity(
    tableName = "movie_category",
    primaryKeys = ["movieId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieCategoryEntity(
    val movieId: Int,
    val categoryId: String
)
