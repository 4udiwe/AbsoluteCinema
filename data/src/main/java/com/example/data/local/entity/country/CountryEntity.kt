package com.example.data.local.entity.country

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.data.local.entity.MovieEntity

@Entity()
data class CountryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
)
