package com.example.domain.model

data class SeqAndPreq(
    var id              : Int?    = null,
    var name            : String? = null,
    var enName          : String? = null,
    var alternativeName : String? = null,
    var type            : String? = null,
    var poster          : Poster? = Poster(),
    var rating          : Rating? = Rating(),
    var year            : Int?    = null,
)
