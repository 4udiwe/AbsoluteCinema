package com.example.data.remote.api

import com.example.data.remote.dto.common.FilterDto
import com.example.data.remote.dto.common.MovieDto
import com.example.data.remote.dto.responce.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {


    /**
     * Выполняет поиск по названию.
     *
     * [query] - название тайтла
     */
    @Headers("X-API-KEY: 40JQ10H-Q9K4FYF-M2KXT1M-SHKCES3")
    @GET("v1.4/movie/search")
    suspend fun searchMovieByName(@Query("query") query: String): MoviesResponseDto

    @Headers("X-API-KEY: 40JQ10H-Q9K4FYF-M2KXT1M-SHKCES3")
    @GET("v1.4/movie/{id}")
    suspend fun getMovieById(@Path("id") id: Int): MovieDto

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
    @Headers("X-API-KEY: 40JQ10H-Q9K4FYF-M2KXT1M-SHKCES3")
    @GET("v1.4/movie")
    suspend fun searchWithFilter(
        @Query("sortField") fields: List<String>? = null,
        @Query("sortType") sortTypes: List<Int>? = null,
        @Query("type") types: List<String>? = null,
        @Query("isSeries") isSeries: Boolean? = null,
        @Query("year") years: List<String>? = null,
        @Query("rating.kp") kpRating: List<String>? = null,
        @Query("genres.name") genres: List<String>? = null,
        @Query("countries.name") countries: List<String>? = null,
        @Query("lists") inCollection: List<String>? = null,
    ): MoviesResponseDto

    /**
     * Запрос получения доступных фильтров для поиска.
     * Например: genres.name, countries.name.
     *
     * @param field искомое поле.
     * @return список из [FilterDto], содержащий найденные фильтры.
     */
    @Headers("X-API-KEY: 40JQ10H-Q9K4FYF-M2KXT1M-SHKCES3")
    @GET("v1/movie/possible-values-by-field")
    suspend fun getFiltersByFields(@Query("field") field: String): List<FilterDto>
}