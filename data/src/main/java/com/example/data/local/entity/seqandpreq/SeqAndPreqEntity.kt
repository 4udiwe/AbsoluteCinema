package com.example.data.local.entity.seqandpreq

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.local.entity.Poster
import com.example.data.local.entity.Rating

@Entity
data class SeqAndPreqEntity(
    @PrimaryKey
    var id              : Int?    = null,
    var name            : String? = null,
    var enName          : String? = null,
    var alternativeName : String? = null,
    var type            : String? = null,
    @Embedded
    var poster          : Poster? = Poster(),
    @Embedded
    var rating          : Rating? = Rating(),
    var year            : Int?    = null,
)
