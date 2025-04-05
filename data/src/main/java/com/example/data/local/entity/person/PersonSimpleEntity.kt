package com.example.data.local.entity.person

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.data.local.entity.MovieEntity

@Entity
data class PersonSimpleEntity(
    @PrimaryKey
    var id           : Int?    = null,
    var photo        : String? = null,
    var name         : String? = null,
    var enName       : String? = null,
    var description  : String? = null,
    var profession   : String? = null,
    var enProfession : String? = null,
)
