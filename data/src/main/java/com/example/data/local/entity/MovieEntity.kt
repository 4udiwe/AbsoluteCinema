package com.example.data.local.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.data.remote.dto.common.MovieDto

/**
 * [MovieEntity] аналогичен [MovieDto],
 * но имеет меньше полей (отсутсвуют те, которые мы не используем)
 */
@Entity
data class MovieEntity(
    @PrimaryKey
    var id                 : Int?                          = null,
    var name               : String?                       = null,
    var alternativeName    : String?                       = null,
    var enName             : String?                       = null,
    /**
     * [userRate] - оценка пользователя, которая хранится локально.
     */
    var userRate           : Int?                          = null,
    var type               : String?                       = null,
    var typeNumber         : Int?                          = null,
    var year               : Int?                          = null,
    var description        : String?                       = null,
    var shortDescription   : String?                       = null,
    var slogan             : String?                       = null,
    var status             : String?                       = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    var facts              : List<FactEntity>               = listOf(),
    @Embedded
    var rating             : RatingEntity?                 = RatingEntity(),
    var movieLength        : Int?                          = null,
    var ageRating          : Int?                          = null,
    @Embedded
    var logo               : LogoEntity?                   = LogoEntity(),
    @Embedded
    var poster             : PosterEntity?                 = PosterEntity(),
    @Embedded
    var backdrop           : BackdropEntity                = BackdropEntity(),
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    var genres             : List<GenreEntity>              = listOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    var countries          : List<CountryEntity>            = listOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    var persons            : List<PersonSimpleEntity>       = listOf(),
    @Embedded
    var budget             : BudgetEntity?                 = BudgetEntity(),
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    var similarMovies      : List<SimilarMovieEntity>       = listOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    var sequelsAndPrequels : List<SeqAndPreqEntity>         = listOf(),
    var top10              : Int?                          = null,
    var top250             : Int?                          = null,
    var totalSeriesLength  : Int?                          = null,
    var seriesLength       : Int?                          = null,
    var isSeries           : Boolean?                      = null,

)
