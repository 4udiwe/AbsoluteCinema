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
data class PersonSimpleEntity(
    @PrimaryKey
    var id           : Int?    = null,
    var photo        : String? = null,
    var name         : String? = null,
    var enName       : String? = null,
    var description  : String? = null,
    var profession   : String? = null,
    var enProfession : String? = null,
    val movieId      : Int?    = null,
)
