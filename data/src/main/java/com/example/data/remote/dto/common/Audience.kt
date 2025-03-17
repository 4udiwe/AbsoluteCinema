package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Audience (

  @SerializedName("count"   ) var count   : Int?    = null,
  @SerializedName("country" ) var country : String? = null

)