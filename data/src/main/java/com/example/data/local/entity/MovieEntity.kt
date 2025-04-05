package com.example.data.local.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey
    var id                 : Int?                          = null,
    var name               : String?                       = null,
    var alternativeName    : String?                       = null,
    var enName             : String?                       = null,
    var type               : String?                       = null,
    var typeNumber         : Int?                          = null,
    var year               : Int?                          = null,
    var description        : String?                       = null,
    var shortDescription   : String?                       = null,
    var slogan             : String?                       = null,
    var status             : String?                       = null,
    var top10              : Int?                          = null,
    var top250             : Int?                          = null,
    var totalSeriesLength  : Int?                          = null,
    var seriesLength       : Int?                          = null,
    var isSeries           : Boolean?                      = null,
    var movieLength        : Int?                          = null,
    var ageRating          : Int?                          = null,
    @Embedded
    var rating             : Rating?                        = Rating(),
    @Embedded
    var logo               : Logo?                          = Logo(),
    @Embedded
    var poster             : Poster?                        = Poster(),
    @Embedded
    var backdrop           : Backdrop                       = Backdrop(),
    @Embedded
    var budget             : Budget?                        = Budget(),
)
