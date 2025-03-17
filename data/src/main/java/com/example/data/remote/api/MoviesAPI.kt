package com.example.data.remote.api

import com.example.data.remote.dto.common.MovieDto
import com.example.data.remote.dto.responce.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MoviesAPI {
    @Headers("X-API-KEY: 40JQ10H-Q9K4FYF-M2KXT1M-SHKCES3")

    /**
     * Выполняет поиск по названию.
     *
     * [query] - название тайтла
     */
    @GET("v1.4/movie/search")
    suspend fun searchMovieByName(@Query("query") query: String): MoviesResponseDto


    @GET("v1.4/movie")
    suspend fun getMovieById(@Query("id") id: Int): MovieDto

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
    @GET("v1.4/movie")
    suspend fun searchWithFilter(
        @Query("sortField") fields: List<String>?,
        @Query("sortType") sortTypes: List<Int>?,
        @Query("type") types: List<String>?,
        @Query("isSeries") isSeries: Boolean?,
        @Query("year") years: List<String>?,
        @Query("rating.kp") kpRating: List<String>?,
        @Query("genres.name") genres: List<String>?,
        @Query("countries.name") countries: List<String>?,
        @Query("lists") inCollection: List<String>?,
    ): MoviesResponseDto
}