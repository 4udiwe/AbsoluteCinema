package com.example.absolutecinema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absolutecinema.R

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onThemeChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background))
            .padding(paddingValues)
            .padding(horizontal = 8.dp),
    ) {
        Text(
            "Настройки",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(
                    color = colorResource(R.color.background_second),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clickable {

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Выбрать язык", fontSize = 26.sp, color = colorResource(R.color.text))
                TextButton(
                    onClick = {

                    }
                ) {
                    Text("Русский", fontSize = 20.sp, color = colorResource(R.color.text_second))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(
                    color = colorResource(R.color.background_second),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TODO("реализовать переключение темной темы")
                val isDarkTheme = remember { mutableStateOf(false) }
                Text("Темная тема", fontSize = 26.sp, color = colorResource(R.color.text))
                Switch(
                    checked = isDarkTheme.value,
                    onCheckedChange = {
                        isDarkTheme.value = it
                        onThemeChanged(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = colorResource(R.color.accent),
                    )
                )
            }
        }
    }
}