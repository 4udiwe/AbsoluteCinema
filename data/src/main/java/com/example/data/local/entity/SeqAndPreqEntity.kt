package com.example.data.local.entity

import androidx.room.Embedded
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
data class SeqAndPreqEntity(
    @PrimaryKey
    var id              : Int?    = null,
    var name            : String? = null,
    var enName          : String? = null,
    var alternativeName : String? = null,
    var type            : String? = null,
    @Embedded
    var poster          : PosterEntity? = PosterEntity(),
    @Embedded
    var rating          : RatingEntity? = RatingEntity(),
    var year            : Int?    = null,
    val movieId         : Int?    = null,
)
