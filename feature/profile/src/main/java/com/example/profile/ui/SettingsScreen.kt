package com.example.profile.ui

import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit


@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onThemeChanged: (Boolean) -> Unit,
) {

    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE) }
    val isDarkTheme = remember { mutableStateOf(sharedPreferences.getBoolean("dark_theme", false)) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .padding(horizontal = 8.dp),
    ) {
        Text(
            "Настройки",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
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
                Text("Выбрать язык", fontSize = 26.sp, color = MaterialTheme.colorScheme.primary)
                TextButton(
                    onClick = {

                    }
                ) {
                    Text("Русский", fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
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

                Text("Темная тема", fontSize = 26.sp, color = MaterialTheme.colorScheme.primary)
                Switch(
                    checked = isDarkTheme.value,
                    onCheckedChange = { checked ->
                        isDarkTheme.value = checked
                        sharedPreferences.edit { putBoolean("dark_theme", checked) }
                        onThemeChanged(checked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = colorResource(com.example.core.R.color.accent),
                        checkedThumbColor =  colorResource(com.example.core.R.color.white)
                    )
                )
            }
        }
    }
}

