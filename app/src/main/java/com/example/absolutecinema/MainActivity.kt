package com.example.absolutecinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.absolutecinema.ui.screens.LoginScreen
import com.example.absolutecinema.ui.screens.RegistrationScreen
import com.example.absolutecinema.ui.theme.AbsoluteCinemaTheme
import com.example.core.ui.BotBar
import com.example.details.ui.DetailsScreen
import com.example.details.viewmodel.DetailsViewModel
import com.example.feed.ui.FeedScreen
import com.example.feed.viewmodel.FeedViewModel
import com.example.profile.ui.ProfileScreen
import com.example.profile.ui.SettingsScreen
import com.example.users.ui.UsersScreen
import com.example.users.viewmodel.UsersViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val feedViewModel by viewModel<FeedViewModel>()
    private val detailsViewModel by viewModel<DetailsViewModel>()
    private val usersViewModel by viewModel<UsersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean("dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )


        enableEdgeToEdge()
        setContent {
            val currentTheme = remember { mutableStateOf(isDarkTheme) }

            AbsoluteCinemaTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                var botBarState by rememberSaveable { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (botBarState) {
                            BotBar(
                                onHome = { navController.navigate(ScreenHome) },
                                onSearch = { navController.navigate(ScreenSearch) },
                                onUsers = { navController.navigate(ScreenUsers) },
                                onProfile = { navController.navigate(ScreenProfile) }
                            )
                        }
                    }
                ) { innerPadding ->

                    NavHost(startDestination = ScreenRegistration, navController = navController) {
                        composable<ScreenHome> {
                            FeedScreen(
                                paddingValues = innerPadding,
                                viewModel = feedViewModel,
                                onMovieClicked = { movie ->
                                    movie.id?.let { id -> detailsViewModel.updateMovie(movieId = id) }
                                    navController.navigate(ScreenMovie)
                                },
                                onAllClicked = { list, string ->

                                }
                            )

                        }
                        composable<ScreenSearch> {

                        }
                        composable<ScreenUsers> {
                            UsersScreen(
                                paddingValues = innerPadding,
                                onMovieClicked = { movie ->
                                    movie.id?.let { id -> detailsViewModel.updateMovie(movieId = id) }
                                    navController.navigate(ScreenMovie)
                                },
                                viewModel = usersViewModel
                            )
                        }
                        composable<ScreenProfile> {
                            ProfileScreen(
                                paddingValues = innerPadding,
                                onSettingsClicked = { navController.navigate(ScreenSettings) })
                        }
                        composable<ScreenSettings> {
                            SettingsScreen(
                                paddingValues = innerPadding,
                                onThemeChanged = { isDark ->
                                    currentTheme.value = isDark
                                    AppCompatDelegate.setDefaultNightMode(
                                        if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                                        else AppCompatDelegate.MODE_NIGHT_NO
                                    )
                                    recreate()
                                }
                            )
                        }
                        composable<ScreenMovie> {
                            DetailsScreen(
                                paddingValues = innerPadding,
                                viewModel = detailsViewModel,
                                onDescriptionClicked = { },
                            )
                        }
                        composable<ScreenLogin> {
                            LoginScreen(
                                onToRegistration = {
                                    navController.navigate(ScreenRegistration)
                                },
                                onEnter = {
                                    navController.navigate(ScreenHome)
                                    botBarState = true
                                }
                            )
                        }
                        composable<ScreenRegistration> {
                            RegistrationScreen(
                                onToLogin = {
                                    navController.navigate(ScreenLogin)
                                },
                                onEnter = {
                                    navController.navigate(ScreenHome)
                                    botBarState = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object ScreenHome

@Serializable
object ScreenSearch

@Serializable
object ScreenUsers

@Serializable
object ScreenProfile

@Serializable
object ScreenSettings

@Serializable
object ScreenMovie

@Serializable
object ScreenRegistration

@Serializable
object ScreenLogin