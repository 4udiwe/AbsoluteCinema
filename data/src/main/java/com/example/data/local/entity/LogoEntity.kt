package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LogoEntity(
    @PrimaryKey
    var logoUrl : String? = null
)