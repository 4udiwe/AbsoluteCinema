package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    var budgetId       : Int?    = null,
    var budgetValue    : Int?    = null,
    var budgetCurrency : String? = null
)
