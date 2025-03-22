package com.example.domain.model

data class Movie(
    var id                 : Int?                          = null,
    var name               : String?                       = null,
    var alternativeName    : String?                       = null,
    var enName             : String?                       = null,
    /**
     * [userRate] - оценка пользователя, которая хранится локально.
     */
    var userRate           : Int?                          = null,
    /**
     * [category] - категория фильма, которая приходит из бд
     */
    var category           : LocalCategory?                = null,
    var type               : String?                       = null,
    var typeNumber         : Int?                          = null,
    var year               : Int?                          = null,
    var description        : String?                       = null,
    var shortDescription   : String?                       = null,
    var slogan             : String?                       = null,
    var status             : String?                       = null,
    var facts              : ArrayList<Fact>               = arrayListOf(),
    var rating             : Rating?                       = Rating(),
    var movieLength        : Int?                          = null,
    var ageRating          : Int?                          = null,
    var logo               : Logo?                         = Logo(),
    var poster             : Poster?                       = Poster(),
    var backdrop           : Backdrop                      = Backdrop(),
    var genres             : ArrayList<Genre>              = arrayListOf(),
    var countries          : ArrayList<Country>            = arrayListOf(),
    var persons            : ArrayList<Person>             = arrayListOf(),
    var budget             : Budget?                       = Budget(),
    var similarMovies      : ArrayList<SimilarMovie>       = arrayListOf(),
    var sequelsAndPrequels : ArrayList<SeqAndPreq>         = arrayListOf(),
    var top10              : Int?                          = null,
    var top250             : Int?                          = null,
    var totalSeriesLength  : Int?                          = null,
    var seriesLength       : Int?                          = null,
    var isSeries           : Boolean?                      = null,
)
