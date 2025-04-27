package com.example.search.ui

import android.util.Range
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import kotlinx.coroutines.flow.MutableStateFlow

@Preview
@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
) {

    //var sliderPosition by remember { mutableStateOf() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .padding(horizontal = 8.dp),
    ) {
        Text(
            "Фильтры",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        //FilterRangeSlider()


    }
}

@Composable
fun FilterRangeSlider(
    title: String = "Title",
    minvalue: Float = 0F,
    maxvalue: Float = 100F,
    range: MutableStateFlow<Range<Float>>
) {
    var sliderPosition: ClosedFloatingPointRange<Float> = (minvalue..maxvalue)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Text(
                "от ${sliderPosition.start.toInt()} до ${sliderPosition.endInclusive.toInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        RangeSlider(
            value = sliderPosition,
            steps = 1,
            onValueChange = { range -> sliderPosition = range },
            valueRange = minvalue..maxvalue,
            onValueChangeFinished = {
                range.value = (sliderPosition.toRange())
            },
        )
    }
}