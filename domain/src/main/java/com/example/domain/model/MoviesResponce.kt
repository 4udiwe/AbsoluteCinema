package com.example.domain.model

data class MoviesResponce(
    var movieDtos: ArrayList<Movie> = arrayListOf(),
    var total: Int? = null,
    var limit: Int? = null,
    var page: Int? = null,
    var pages: Int? = null,
)
