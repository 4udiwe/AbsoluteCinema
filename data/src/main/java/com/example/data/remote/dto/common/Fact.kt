package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Fact (

  @SerializedName("value"   ) var value   : String?  = null,
  @SerializedName("type"    ) var type    : String?  = null,
  @SerializedName("spoiler" ) var spoiler : Boolean? = null

)