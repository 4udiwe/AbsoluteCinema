package com.example.absolutecinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.auth.viewmodel.AuthViewModel
import com.example.absolutecinema.navigtion.AppNavigation
import com.example.absolutecinema.ui.theme.AbsoluteCinemaTheme
import com.example.details.viewmodel.DetailsViewModel
import com.example.feed.viewmodel.FeedViewModel
import com.example.search.viewmodel.SearchViewModel
import com.example.users.viewmodel.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val feedViewModel by viewModel<FeedViewModel>()
    private val detailsViewModel by viewModel<DetailsViewModel>()
    private val usersViewModel by viewModel<UsersViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()
    private val authViewModel by viewModel<AuthViewModel>()

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

                /**
                 * Изменяет тему приложения и пересоздает активити.
                 *
                 * @param isDark индикатор темной темы.
                 */
                fun changeTheme(isDark: Boolean){
                    currentTheme.value = isDark
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    recreate()
                }

                AppNavigation(
                    navController = navController,
                    feedViewModel = feedViewModel,
                    detailsViewModel = detailsViewModel,
                    usersViewModel = usersViewModel,
                    searchViewModel = searchViewModel,
                    authViewModel = authViewModel,
                    onThemeChanged = { isDarkTheme ->
                        changeTheme(isDark = isDarkTheme)
                    },
                    context = this
                )

            }
        }
    }
}

