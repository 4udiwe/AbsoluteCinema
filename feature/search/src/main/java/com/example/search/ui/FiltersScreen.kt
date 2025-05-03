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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import com.example.search.viewmodel.SearchViewModel

@Composable
fun FiltersScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel,
    onBackClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    val filters = viewModel.filters.collectAsState()
    val filtersForSearch = viewModel.filtersForSearch.collectAsState()

    val ratingSliderPosition = remember {
        mutableStateOf(filters.value.rating.lower..filters.value.rating.upper)
    }
    val yearSliderPosition = remember {
        mutableStateOf(filters.value.years.lower..filters.value.years.upper)
    }


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
                "от ${yearSliderPosition.value.start.toInt()} до ${yearSliderPosition.value.endInclusive.toInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            modifier = Modifier.padding(horizontal = 8.dp),
            value = yearSliderPosition.value,
            onValueChange = { range ->
                yearSliderPosition.value = range
            },
            onValueChangeFinished = {
                viewModel.setYearsFilter(yearSliderPosition.value.toRange())
            },
            steps = 0,
            valueRange = (filters.value.years.lower..filters.value.years.upper),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(com.example.core.R.color.accent),
                activeTrackColor = colorResource(com.example.core.R.color.accent),
                inactiveTickColor = colorResource(com.example.core.R.color.accent),
                activeTickColor = colorResource(com.example.core.R.color.white),
                inactiveTrackColor = MaterialTheme.colorScheme.surface
            )
        )

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Тип",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 8.dp)
            )
            if (filtersForSearch.value.types.isNotEmpty()) {
                Text(
                    text = filtersForSearch.value.types.size.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(com.example.core.R.color.accent)
                )
            }
        }
        LazyRow {
            items(filters.value.types) {
                val selected = filtersForSearch.value.types.contains(it)
                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected) viewModel.removeTypeFilter(it)
                        else viewModel.addTypeFilter(it)
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
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Жанры",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 8.dp)
            )
            if (filtersForSearch.value.genres.isNotEmpty()) {
                Text(
                    text = filtersForSearch.value.genres.size.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(com.example.core.R.color.accent)
                )
            }
        }
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
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Страны",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 8.dp)
            )
            if (filtersForSearch.value.countries.isNotEmpty()) {
                Text(
                    text = filtersForSearch.value.countries.size.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(com.example.core.R.color.accent)
                )
            }
        }
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
                "от ${ratingSliderPosition.value.start.toInt()} до ${ratingSliderPosition.value.endInclusive.toInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            modifier = Modifier.padding(horizontal = 8.dp),
            value = ratingSliderPosition.value,
            steps = 8,
            onValueChange = { range -> ratingSliderPosition.value = range },
            valueRange = filters.value.rating.lower..filters.value.rating.upper,
            onValueChangeFinished = {
                viewModel.setRatingFilter(ratingSliderPosition.value.toRange())
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
                onSearchClicked.invoke()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .padding(top = 40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(com.example.core.R.color.accent),
                contentColor = colorResource(com.example.core.R.color.white)
            )
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