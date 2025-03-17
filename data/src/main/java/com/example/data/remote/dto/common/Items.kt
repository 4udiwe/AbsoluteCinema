package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Items (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("logo" ) var logo : Logo?   = Logo()

)