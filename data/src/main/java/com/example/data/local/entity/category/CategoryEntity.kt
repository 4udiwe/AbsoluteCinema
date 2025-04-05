package com.example.data.local.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.LocalCategory

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name : LocalCategory? = null
)