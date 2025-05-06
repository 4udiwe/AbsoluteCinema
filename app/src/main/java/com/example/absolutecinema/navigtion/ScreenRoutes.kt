package com.example.absolutecinema.navigtion

import kotlinx.serialization.Serializable

sealed class ScreenRoutes {

    @Serializable
    object ScreenBegin

    @Serializable
    object ScreenHome

    @Serializable
    object ScreenSearch

    @Serializable
    object ScreenSearchFilters

    @Serializable
    object ScreenSearchFiltersResult

    @Serializable
    object ScreenUsers

    @Serializable
    object ScreenProfile

    @Serializable
    object ScreenSettings

    @Serializable
    object ScreenMovie

    @Serializable
    object ScreenDescription

    @Serializable
    object ScreenRegistration

    @Serializable
    object ScreenLogin

    @Serializable
    object ScreenAllMovies

    @Serializable
    object ScreenAllSeries

    @Serializable
    object ScreenAllDetectives

    @Serializable
    object ScreenAllComedies

    @Serializable
    object ScreenAllRomans
}