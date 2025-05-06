package com.example.absolutecinema.navigtion

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auth.ui.LoginScreen
import com.example.auth.ui.RegistrationScreen
import com.example.auth.viewmodel.AuthViewModel
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllComedies
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllDetectives
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllMovies
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllRomans
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllSeries
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenBegin
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenDescription
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenHome
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenLogin
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenMovie
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenProfile
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenRegistration
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenSearch
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenSearchFilters
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenSearchFiltersResult
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenSettings
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenUsers
import com.example.core.ui.BeginScreen
import com.example.core.ui.BotBar
import com.example.details.ui.DescriptionScreen
import com.example.details.ui.DetailsScreen
import com.example.details.viewmodel.DetailsViewModel
import com.example.domain.model.Movie
import com.example.feed.ui.AllComedyMoviesScreen
import com.example.feed.ui.AllDetectiveMoviesScreen
import com.example.feed.ui.AllRecommendedMoviesScreen
import com.example.feed.ui.AllRecommendedSeriesScreen
import com.example.feed.ui.AllRomanMoviesScreen
import com.example.feed.ui.FeedScreen
import com.example.feed.viewmodel.FeedViewModel
import com.example.profile.ui.ProfileScreen
import com.example.profile.ui.SettingsScreen
import com.example.search.ui.FilterSearchResultScreen
import com.example.search.ui.FiltersScreen
import com.example.search.ui.SearchScreen
import com.example.search.viewmodel.SearchViewModel
import com.example.users.ui.UsersScreen
import com.example.users.viewmodel.UsersViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    detailsViewModel: DetailsViewModel,
    usersViewModel: UsersViewModel,
    searchViewModel: SearchViewModel,
    authViewModel: AuthViewModel,
    onThemeChanged: (Boolean) -> Unit,
    context: Context,
) {
    var botBarState by rememberSaveable { mutableStateOf(false) }

    /**
     * Обработка нажатия на элемент фильма на любом экране.
     *
     * @param movie фильм, который был кликнут.
     */
    fun handleMovieClick(movie: Movie) {
        movie.id?.let { id -> detailsViewModel.updateMovie(movieId = id) }
        navController.navigate(ScreenMovie)
    }

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

        NavHost(navController = navController, startDestination = ScreenBegin) {
            composable<ScreenBegin> {
                var checkPerformed by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if (!checkPerformed) {
                        delay(500)

                        val userPresent = authViewModel.checkUser()

                        if (userPresent) {
                            navController.navigate(ScreenHome) {
                                popUpTo(ScreenBegin) { inclusive = true }
                            }
                            botBarState = true
                        } else {
                            navController.navigate(ScreenRegistration) {
                                popUpTo(ScreenBegin) { inclusive = true }
                            }
                        }

                        checkPerformed = true
                    }
                }
                BeginScreen(paddingValues = innerPadding)

            }
            composable<ScreenHome> {
                FeedScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onAllRecommendedMoviesClicked = { navController.navigate(ScreenAllMovies) },
                    onAllRecommendedSeriesClicked = { navController.navigate(ScreenAllSeries) },
                    onAllDetectiveMoviesClicked = { navController.navigate(ScreenAllDetectives) },
                    onAllComedyMoviesClicked = { navController.navigate(ScreenAllComedies) },
                    onAllRomanMoviesClicked = { navController.navigate(ScreenAllRomans) },
                )
            }
            composable<ScreenAllMovies> {
                AllRecommendedMoviesScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable<ScreenAllSeries> {
                AllRecommendedSeriesScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable<ScreenAllDetectives> {
                AllDetectiveMoviesScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable<ScreenAllComedies> {
                AllComedyMoviesScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable<ScreenAllRomans> {
                AllRomanMoviesScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onBackClicked = { navController.popBackStack() }
                )
            }

            composable<ScreenSearch> {
                SearchScreen(
                    paddingValues = innerPadding,
                    viewModel = searchViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onFiltersMenuClicked = { navController.navigate(ScreenSearchFilters) }
                )
            }
            composable<ScreenSearchFilters> {
                FiltersScreen(
                    paddingValues = innerPadding,
                    viewModel = searchViewModel,
                    onBackClicked = { navController.navigate(ScreenSearch) },
                    onSearchClicked = { navController.navigate(ScreenSearchFiltersResult) },
                )
            }
            composable<ScreenSearchFiltersResult> {
                FilterSearchResultScreen(
                    viewModel = searchViewModel,
                    paddingValues = innerPadding,
                    onBackClicked = { navController.navigate(ScreenSearchFilters) },
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    }
                )
            }
            composable<ScreenUsers> {
                UsersScreen(
                    paddingValues = innerPadding,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    viewModel = usersViewModel
                )
            }
            composable<ScreenProfile> {
                ProfileScreen(
                    paddingValues = innerPadding,
                    onSettingsClicked = { navController.navigate(ScreenSettings) },
                    onLogOut = {
                        navController.navigate(ScreenRegistration)
                        botBarState = false
                    },
                    viewmodel = authViewModel
                )
            }
            composable<ScreenSettings> {
                SettingsScreen(
                    paddingValues = innerPadding,
                    onThemeChanged = { isDark ->
                        onThemeChanged(isDark)

                    }
                )
            }
            composable<ScreenMovie> {
                DetailsScreen(
                    paddingValues = innerPadding,
                    viewModel = detailsViewModel,
                    onDescriptionClicked = {
                        navController.navigate(ScreenDescription)
                    },
                )
            }
            composable<ScreenDescription> {
                DescriptionScreen(
                    paddingValues = innerPadding,
                    viewModel = detailsViewModel
                )
            }
            composable<ScreenLogin> {
                LoginScreen(
                    onToRegistration = {
                        navController.navigate(ScreenRegistration)
                    },
                    viewModel = authViewModel,
                    onSuccess = {
                        navController.navigate(ScreenHome)
                        botBarState = true
                    },
                    context = context,
                )
            }
            composable<ScreenRegistration> {
                RegistrationScreen(
                    onToLogin = {
                        navController.navigate(ScreenLogin)
                    },
                    viewModel = authViewModel,
                    onSuccess = {
                        navController.navigate(ScreenLogin)
                    },
                    context = context,
                )
            }
        }
    }
}