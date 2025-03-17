package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Videos (

  @SerializedName("trailers" ) var trailers : ArrayList<Trailers> = arrayListOf()

)