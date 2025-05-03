package com.example.absolutecinema.navigtion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllComedies
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllDetectives
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllMovies
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllRomans
import com.example.absolutecinema.navigtion.ScreenRoutes.ScreenAllSeries
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
import com.example.absolutecinema.ui.screens.LoginScreen
import com.example.absolutecinema.ui.screens.RegistrationScreen
import com.example.core.ui.BotBar
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

@Composable
fun AppNavigation(
    navController: NavHostController,
    feedViewModel: FeedViewModel,
    detailsViewModel: DetailsViewModel,
    usersViewModel: UsersViewModel,
    searchViewModel: SearchViewModel,
    onThemeChanged: (Boolean) -> Unit
) {
    var botBarState by rememberSaveable { mutableStateOf(false) }

    /**
     * Обработка нажатия на элемент фильма на любом экране.
     *
     * @param movie фильм, который был кликнут.
     */
    fun handleMovieClick(movie: Movie){
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

        NavHost(navController = navController, startDestination = ScreenRegistration) {
            composable<ScreenHome> {
                FeedScreen(
                    paddingValues = innerPadding,
                    viewModel = feedViewModel,
                    onMovieClicked = { movie ->
                        handleMovieClick(movie)
                    },
                    onAllRecommendedMoviesClicked = { navController.navigate(ScreenAllMovies)},
                    onAllRecommendedSeriesClicked = { navController.navigate(ScreenAllSeries)},
                    onAllDetectiveMoviesClicked = { navController.navigate(ScreenAllDetectives)},
                    onAllComedyMoviesClicked = { navController.navigate(ScreenAllComedies)},
                    onAllRomanMoviesClicked = { navController.navigate(ScreenAllRomans)},
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
                    onSettingsClicked = { navController.navigate(ScreenSettings) })
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