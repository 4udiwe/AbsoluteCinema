package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Usa (

  @SerializedName("value"    ) var value    : Int?    = null,
  @SerializedName("currency" ) var currency : String? = null

)