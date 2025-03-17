package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Networks (

  @SerializedName("items" ) var items : ArrayList<Items> = arrayListOf()

)