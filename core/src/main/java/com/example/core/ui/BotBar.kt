package com.example.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.R

enum class BotBarState {
    Home, Search, Users, Profile
}

@Preview
@Composable
fun BotBar(
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onUsers: () -> Unit = {},
    onProfile: () -> Unit = {},
) {

    var state by remember { mutableStateOf(BotBarState.Home) }
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    onHome.invoke()
                    state = BotBarState.Home
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home",
                    tint =
                    if (state == BotBarState.Home)
                        colorResource(R.color.accent)
                    else
                        MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = {
                onSearch.invoke()
                state = BotBarState.Search
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    tint =
                    if (state == BotBarState.Search)
                        colorResource(R.color.accent)
                    else
                        MaterialTheme.colorScheme.secondary
                )

            }
            IconButton(onClick = {
                onUsers.invoke()
                state = BotBarState.Users
            }) {
                Icon(
                    painter = painterResource(R.drawable.bookmark),
                    contentDescription = "favorite",
                    tint =
                    if (state == BotBarState.Users)
                        colorResource(R.color.accent)
                    else
                        MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = {
                onProfile.invoke()
                state = BotBarState.Profile
            }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "user",
                    tint =
                    if (state == BotBarState.Profile)
                        colorResource(R.color.accent)
                    else
                        MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}