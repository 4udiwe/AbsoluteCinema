package com.example.absolutecinema.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.Person
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


@Composable
fun BotBar(
    onHome: () -> Unit,
    onSearch: () -> Unit,
    onUsers: () -> Unit,
    onProfile: () -> Unit,
) {

    var home by remember { mutableStateOf(true) }
    var search by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf(false) }
    var profile by remember { mutableStateOf(false) }
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                onHome.invoke()
                home = true
                search = false
                users = false
                profile = false
            }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home",
                )
            }
            IconButton(onClick = {
                onSearch.invoke()
                home = false
                search = true
                users = false
                profile = false
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                )

            }
            IconButton(onClick = {
                onUsers.invoke()
                home = false
                search = false
                users = true
                profile = false
            }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "favorite",
                )
            }
            IconButton(onClick = {
                onProfile.invoke()
                home = false
                search = false
                users = false
                profile = true
            }) {
                Icon(
                    imageVector = Icons.TwoTone.Person,
                    contentDescription = "user",

                    )
            }
        }
    }
}