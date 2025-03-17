package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName


data class Premiere(

    @SerializedName("country") var country: String? = null,
    @SerializedName("world") var world: String? = null,
    @SerializedName("russia") var russia: String? = null,
    @SerializedName("digital") var digital: String? = null,
    @SerializedName("cinema") var cinema: String? = null,
    @SerializedName("bluray") var bluray: String? = null,
    @SerializedName("dvd") var dvd: String? = null,

    )