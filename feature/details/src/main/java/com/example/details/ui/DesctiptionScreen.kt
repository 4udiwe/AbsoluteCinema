package com.example.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.util.getName
import com.example.details.viewmodel.DetailsViewModel

@Composable
fun DescriptionScreen(paddingValues: PaddingValues, viewModel: DetailsViewModel) {

    val movie by viewModel.movie.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 8.dp)
    ) {
        Text(
            text = movie.getName(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = movie.shortDescription.toString(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(8.dp))

        Text(
            text = movie.description.toString(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        )
    }
}