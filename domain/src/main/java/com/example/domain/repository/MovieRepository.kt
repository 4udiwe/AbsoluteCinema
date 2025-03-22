package com.example.domain.repository

import com.example.domain.model.Movie
import com.example.domain.model.MoviesResponce
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieById(id: Int) : Movie

    suspend fun searchMoviesByName(query: String) : MoviesResponce

    /**
     * Выполняет поиск с различными фильтрами.
     *
     *
     * - [fields] - сортировка по полям из MoviesDTO (year, rating.kp...)
     * - [sortTypes] - тип сортировки для каждого из полей [fields] ("1", "-1")
     * - [types] - поиск по типам:
     *      - movie
     *      - tv-series
     *      - cartoon
     *      - animated-series
     *      - anime
     *
     *      Пример: {"movie", "tv-series", "!anime"}
     *
     * - [isSeries] - является ли сериалом
     * - [years] - года, как по отдельности, так и диапазоном
     *
     *      Пример: {1874, 2050, !2020, 2020-2024}
     *
     * - [kpRating] - поиск по рейтингу Кинопоиска
     *
     *      Пример: { 7, 10, 7.2-10 }
     *
     * - [genres] - жанры
     *
     *      Пример: { "драма", "комедия", "!мелодрама", "+ужасы" }
     *
     * - [countries] - страны
     *
     *      Пример: { "США", "Россия", "!Франция" , "+Великобритания" }
     *
     * - [inCollection] - вхождение в какую-либо коллекцию
     *
     *      Пример: { "top250", "top-100-indian-movies", "!top-100-movies" }
     */
    suspend fun searchMoviesWithFilters(
        fields: List<String>?,
        sortTypes: List<Int>?,
        types: List<String>?,
        isSeries: Boolean?,
        years: List<String>?,
        kpRating: List<String>?,
        genres: List<String>?,
        countries: List<String>?,
        inCollection: List<String>?,
    ) : MoviesResponce

    fun getFavouriteMovies() : Flow<List<Movie>>

    fun getMoviesWithUserRate() : Flow<List<Movie>>

    fun getWillWatchMovies() : Flow<List<Movie>>

    suspend fun addMovieToFavourites(id: Int) : Boolean
    suspend fun removeMovieFromFavourites(id: Int) : Boolean

    suspend fun setUserRateToMovie(movieId: Int, rate: Int) : Boolean
    suspend fun removeUserRateFromMovie(movieId: Int) : Boolean

    suspend fun addMovieToWillWatch(id: Int): Boolean
    suspend fun removeMovieFromWillWatch(id: Int): Boolean

}