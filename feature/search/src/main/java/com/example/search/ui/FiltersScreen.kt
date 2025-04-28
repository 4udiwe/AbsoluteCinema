package com.example.search.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import com.example.search.viewmodel.SearchViewModel

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel,
    onBackClicked: () -> Unit,
) {
    val filters = viewModel.filters.collectAsState()
    val filtersForSearch = viewModel.filtersForSearch.collectAsState()

    var yearSliderPosition: ClosedFloatingPointRange<Float> =
        (filters.value.years.lower..filters.value.years.upper)
    var ratingSliderPosition: ClosedFloatingPointRange<Float> =
        (filters.value.rating.lower..filters.value.rating.upper)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .padding(horizontal = 8.dp)
            .verticalScroll(state = scrollState),
    ) {
        Text(
            "Фильтры",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "Год",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Text(
                "от ${yearSliderPosition.start.toInt()} до ${yearSliderPosition.endInclusive.toInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            value = yearSliderPosition,
            steps = 20,
            onValueChange = { range -> yearSliderPosition = range },
            valueRange = filters.value.years.lower..filters.value.years.upper,
            onValueChangeFinished = {
                viewModel.setYearsFilter(yearSliderPosition.toRange())
            },
            colors = SliderDefaults.colors(
                thumbColor = colorResource(com.example.core.R.color.accent),
                activeTrackColor = colorResource(com.example.core.R.color.accent),
                inactiveTickColor = colorResource(com.example.core.R.color.accent),
                activeTickColor = colorResource(com.example.core.R.color.white),
                inactiveTrackColor = MaterialTheme.colorScheme.surface
            )
        )

        Text(
            text = "Тип",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        LazyRow {
            items(filters.value.types) {
                val selected = filtersForSearch.value.types.contains(it)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected) viewModel.removeTypeFilter(it)
                        else viewModel.addTypeFliter(it)
                    },
                    label = { Text(it.name) },
                    modifier = Modifier.padding(4.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(com.example.core.R.color.accent),
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.secondary
                    ),
                    border = BorderStroke(
                        width = 1.dp, colorResource(com.example.core.R.color.accent)
                    )
                )
            }
        }

        Text(
            text = "Жанры",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        LazyRow {
            items(filters.value.genres) {
                val selected = filtersForSearch.value.genres.contains(it)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected) viewModel.removeGenreFilter(it)
                        else viewModel.addGenreFilter(it)
                    },
                    label = { Text(it.name) },
                    modifier = Modifier.padding(4.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(com.example.core.R.color.accent),
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.secondary
                    ),
                    border = BorderStroke(
                        width = 1.dp, colorResource(com.example.core.R.color.accent)
                    )
                )
            }
        }
        Text(
            text = "Страны",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        LazyRow {
            items(filters.value.countries) {
                val selected = filtersForSearch.value.countries.contains(it)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected) viewModel.removeCountryFilter(it)
                        else viewModel.addCountryFilter(it)
                    },
                    label = { Text(it.name) },
                    modifier = Modifier.padding(4.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(com.example.core.R.color.accent),
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.secondary
                    ),
                    border = BorderStroke(
                        width = 1.dp, colorResource(com.example.core.R.color.accent)
                    )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "Рейтинг",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Text(
                "от ${ratingSliderPosition.start.toInt()} до ${ratingSliderPosition.endInclusive.toInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            value = ratingSliderPosition,
            steps = 20,
            onValueChange = { range -> ratingSliderPosition = range },
            valueRange = filters.value.years.lower..filters.value.years.upper,
            onValueChangeFinished = {
                viewModel.setRatingFilter(ratingSliderPosition.toRange())
            },
            colors = SliderDefaults.colors(
                thumbColor = colorResource(com.example.core.R.color.accent),
                activeTrackColor = colorResource(com.example.core.R.color.accent),
                inactiveTickColor = colorResource(com.example.core.R.color.accent),
                activeTickColor = colorResource(com.example.core.R.color.white),
                inactiveTrackColor = MaterialTheme.colorScheme.surface
            )
        )

        Button(
            onClick = {
                viewModel.searchWithFilters()
                onBackClicked.invoke()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .padding(top = 40.dp)
        ) {
            Text("Искать")
        }

        TextButton(
            onClick = {
                onBackClicked.invoke()
            }, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
        ) {
            Text("Назад", color = colorResource(com.example.core.R.color.accent))
        }
    }
}