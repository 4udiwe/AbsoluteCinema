package com.example.data.local.entity

import com.example.data.remote.dto.common.Russia
import com.example.data.remote.dto.common.Usa
import com.example.data.remote.dto.common.World

data class Fees(
    var id     : Int? ,
    var world  : World?  = World(),
    var russia : Russia? = Russia(),
    var usa    : Usa?    = Usa()
)
