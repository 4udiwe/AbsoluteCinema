package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Name (

  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("language" ) var language : String? = null,
  @SerializedName("type"     ) var type     : String? = null

)