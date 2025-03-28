package com.example.domain.model

data class Movie(
    var id: Int? = null,
    var name: String? = null,
    var alternativeName: String? = null,
    var enName: String? = null,
    /**
     * [userRate] - оценка пользователя, которая хранится локально.
     */
    var userRate: Int? = null,
    var type: String? = null,
    var typeNumber: Int? = null,
    var year: Int? = null,
    var description: String? = null,
    var shortDescription: String? = null,
    var slogan: String? = null,
    var status: String? = null,
    var facts: List<Fact> = arrayListOf(),
    var rating: Rating? = Rating(),
    var movieLength: Int? = null,
    var ageRating: Int? = null,
    var logo: Logo? = Logo(),
    var poster: Poster? = Poster(),
    var backdrop: Backdrop? = Backdrop(),
    var genres: List<Genre> = arrayListOf(),
    var countries: List<Country> = arrayListOf(),
    var persons: List<Person> = arrayListOf(),
    var budget: Budget? = Budget(),
    var similarMovies: List<SimilarMovie> = arrayListOf(),
    var sequelsAndPrequels: List<SeqAndPreq> = arrayListOf(),
    var top10: Int? = null,
    var top250: Int? = null,
    var totalSeriesLength: Int? = null,
    var seriesLength: Int? = null,
    var isSeries: Boolean? = null,
)
