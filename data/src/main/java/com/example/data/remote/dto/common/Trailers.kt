package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Trailers (

  @SerializedName("url"  ) var url  : String? = null,
  @SerializedName("name" ) var name : String? = null,
  @SerializedName("site" ) var site : String? = null,
  @SerializedName("size" ) var size : Int?    = null,
  @SerializedName("type" ) var type : String? = null

)