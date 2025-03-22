package com.example.data.local.category

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.data.local.entity.MovieEntity
import com.example.domain.model.LocalCategory

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name : LocalCategory? = null
)