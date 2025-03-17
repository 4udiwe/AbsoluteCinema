package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class ExternalId (

  @SerializedName("kpHD" ) var kpHD : String? = null,
  @SerializedName("imdb" ) var imdb : String? = null,
  @SerializedName("tmdb" ) var tmdb : Int?    = null

)