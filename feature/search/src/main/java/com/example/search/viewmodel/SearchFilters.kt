package com.example.search.viewmodel

import android.util.Range
import androidx.core.util.toRange
import com.example.domain.model.Filter
import java.time.LocalDate

data class SearchFilters(
    var types: List<Filter> = listOf(),
    var genres: List<Filter> = listOf(),
    var countries: List<Filter> = listOf(),
    var years: Range<Float> = (1900F..LocalDate.now().year.toFloat()).toRange(),
    var rating: Range<Float> = (1F..10F).toRange(),
)
