package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class FilterDto(
    @SerializedName("name") val name: String,
    @SerializedName("sluq") val slug: String
)
