package com.example.search.viewmodel

import android.util.Range
import androidx.core.util.toRange
import com.example.domain.model.Filter
import java.time.LocalDate

data class SearchFilters(
    var types: MutableList<Filter> = mutableListOf(),
    var genres: MutableList<Filter> = mutableListOf(),
    var countries: MutableList<Filter> = mutableListOf(),
    var years: Range<Float> = (1900F..LocalDate.now().year.toFloat()).toRange(),
    var rating: Range<Float> = (1F..10F).toRange(),
)
