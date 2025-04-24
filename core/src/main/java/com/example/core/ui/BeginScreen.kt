package com.example.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.R

@Preview(showSystemUi = true)
@Composable
fun BeginScreen(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {
        Icon(
            painter = painterResource(R.drawable.absolutecinema_icon),
            contentDescription = "",
            modifier = Modifier.align(
                Alignment.Center
            ).fillMaxSize(0.5f),
            tint = colorResource(R.color.accent)
        )
    }
}