package com.example.domain.model

data class MoviesResponce(
    var movies: List<Movie> = listOf(),
    var total: Int? = null,
    var limit: Int? = null,
    var page: Int? = null,
    var pages: Int? = null,
)
