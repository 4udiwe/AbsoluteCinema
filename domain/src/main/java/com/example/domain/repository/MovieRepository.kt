package com.example.domain.repository

import com.example.domain.model.Filter
import com.example.domain.model.Movie
import com.example.domain.model.MoviesResponce
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    /**
     * Выполняет поиск по ID фильма.
     *
     * @param id идентификатор фильма. Берется из API, не создается локально.
     * @return [Movie] класс фильма.
     */
    suspend fun getMovieById(id: Int) : Movie

    /**
     * Выполняет поиск фильма по имени.
     *
     * @param query поисковой запрос (имя фильма)
     * @return [MoviesResponce] класс, который содержит список найденных фильмов,
     * а также информацию по ответу:
     *  - кол-во найденных записей
     *  - ограничение кол-ва
     *
     *  и тд.
     */
    suspend fun searchMoviesByName(query: String) : MoviesResponce

    /**
     * Выполняет поиск с различными фильтрами.
     *
     *
     * @param fields сортировка по полям из MoviesDTO (year, rating.kp...)
     * @param sortTypes  тип сортировки для каждого из полей [fields] ("1", "-1")
     * @param types поиск по типам:
     *      - movie
     *      - tv-series
     *      - cartoon
     *      - animated-series
     *      - anime
     *
     *      Пример: {"movie", "tv-series", "!anime"}
     *
     * @param isSeries является ли сериалом
     * @param years года, как по отдельности, так и диапазоном
     *
     *      Пример: {1874, 2050, !2020, 2020-2024}
     *
     * @param kpRating поиск по рейтингу Кинопоиска
     *
     *      Пример: { 7, 10, 7.2-10 }
     *
     * @param genres жанры
     *
     *      Пример: { "драма", "комедия", "!мелодрама", "+ужасы" }
     *
     * @param countries страны
     *
     *      Пример: { "США", "Россия", "!Франция" , "+Великобритания" }
     *
     * @param inCollection вхождение в какую-либо коллекцию
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

    /**
     * Получает список стран, применяемых для филтрации в поиске из API.
     *
     * @return список из [Filter], содержащий страны.
     */
    suspend fun getCountryFiltersForSearch() : List<Filter>

    /**
     * Получает список жанров, применяемых для филтрации в поиске из API.
     *
     * @return список из [Filter], содержащий жанры.
     */
    suspend fun getGenreFiltersForSearch() : List<Filter>


    /**
     * Получает список типов, применяемых для филтрации в поиске из API.
     *  - movie
     *  - tv-series
     *  - anime
     *  - animated-series
     *  - cartoon
     *
     * @return список из [Filter], содержащий жанры.
     */
    suspend fun getTypeFiltersForSearch() : List<Filter>

    /**
     * Возвращаяет список любимых фильмов из БД.
     */
    fun getFavouriteMovies() : Flow<List<Movie>>

    /**
     * Возвращаяет список фильмов с оценкой пользователя из БД.
     */
    fun getMoviesWithUserRate() : Flow<List<Movie>>

    /**
     * Возвращает спиоск фильмов "Буду смотреть" из БД.
     */
    fun getWillWatchMovies() : Flow<List<Movie>>

    /**
     * Добавляет фильм в список любимых.
     *
     * @param id идентификаторв фильма.
     * @return [Boolean] успех/ошибка
     */
    suspend fun addMovieToFavourites(id: Int) : Boolean

    /**
     * Удаляет фильм из списка любимых.
     *
     * @param id идентификаторв фильма.
     * @return [Boolean] успех/ошибка
     */
    suspend fun removeMovieFromFavourites(id: Int) : Boolean

    /**
     * Устанавливает пользовательскую оценку фильму и сохраняет ее локально
     *
     * @param movieId идентификатор фильма
     * @param rate пользовательская оценка, имеет тип [Int], от 1 до 10
     * @return [Boolean] успех/ошибка
     */
    suspend fun setUserRateToMovie(movieId: Int, rate: Int) : Boolean

    /**
     * Удаляет пользовательскую оценку фильма.
     *
     * @param movieId идентификатор фильма.
     * @return [Boolean] успех/ошибка
     */
    suspend fun removeUserRateFromMovie(movieId: Int) : Boolean

    /**
     * Добавляет фильм в список "Буду смотреть".
     *
     * @param id идентификатор фильма
     * @return [Boolean] успех/ошибка
     */
    suspend fun addMovieToWillWatch(id: Int): Boolean

    /**
     * Удаляет фильм из списка "Буду смотреть".
     *
     * @param id идентификатор фильма
     * @return [Boolean] успех/ошибка
     */
    suspend fun removeMovieFromWillWatch(id: Int): Boolean

    /**
     * Возвращает рекомендованные фильмы.
     *
     *
     * Берет список фильмов из API, если рекомендованные уже есть, то удаляет прошлые и записывает в БД новые.
     * Затем показывает список из БД.
     * Если запрос к API неудачный, то показывает последние кешированные фильмы из БД.
     */
    fun getRecommendedFilms() : Flow<List<Movie>>

    /**
     * Возвращает рекомендованные сериалы.
     *
     *
     * Берет список сериалов из API, если рекомендованные уже есть, то удаляет прошлые и записывает в БД новые.
     * Затем показывает список из БД.
     * Если запрос к API неудачный, то показывает последние кешированные сериалы из БД.
     */
    fun getRecommendedSeries() : Flow<List<Movie>>

    /**
     * Возвращает рекомендованные фильмы.
     *
     *
     * Берет список фильмов из API, если рекомендованные уже есть, то удаляет прошлые и записывает в БД новые.
     * Затем показывает список из БД.
     * Если запрос к API неудачный, то показывает последние кешированные фильмы из БД.
     */
    fun getDetectiveMovies() : Flow<List<Movie>>

    /**
     * Возвращает рекомендованные фильмы.
     *
     *
     * Берет список фильмов из API, если рекомендованные уже есть, то удаляет прошлые и записывает в БД новые.
     * Затем показывает список из БД.
     * Если запрос к API неудачный, то показывает последние кешированные фильмы из БД.
     */
    fun getRomanMovies() : Flow<List<Movie>>

    /**
     * Возвращает рекомендованные фильмы.
     *
     *
     * Берет список фильмов из API, если рекомендованные уже есть, то удаляет прошлые и записывает в БД новые.
     * Затем показывает список из БД.
     * Если запрос к API неудачный, то показывает последние кешированные фильмы из БД.
     */
    fun getComedyMovies() : Flow<List<Movie>>

    /**
     * Функция очистки кэша - удаляет все локаьлно сохраненные фильмы из БД.
     */
    suspend fun clearCache()
}
