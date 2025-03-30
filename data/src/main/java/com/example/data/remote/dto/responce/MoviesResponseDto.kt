package com.example.data.remote.dto.responce

import com.example.data.remote.dto.common.MovieDto
import com.google.gson.annotations.SerializedName


data class MoviesResponseDto (

    @SerializedName("docs"  ) var movieDtos: List<MovieDto> = listOf(),
    @SerializedName("total" ) var total : Int?            = null,
    @SerializedName("limit" ) var limit : Int?            = null,
    @SerializedName("page"  ) var page  : Int?            = null,
    @SerializedName("pages" ) var pages : Int?            = null

)
