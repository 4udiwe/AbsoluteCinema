package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class SeasonsInfo (

  @SerializedName("number"        ) var number        : Int? = null,
  @SerializedName("episodesCount" ) var episodesCount : Int? = null

)