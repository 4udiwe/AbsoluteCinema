package com.example.data.repository

import android.util.Log
import com.example.data.local.dao.MovieDao
import com.example.data.mapper.DtoToEntity
import com.example.data.mapper.EntityToDomain
import com.example.data.remote.api.MoviesAPI
import com.example.domain.model.LocalCategory
import com.example.domain.model.Movie
import com.example.domain.model.MoviesResponce
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Реализует логику [MovieRepository]
 *
 * @property api экземпляр [MoviesAPI]
 * @property dao экземпляр [MovieDao]
 */
class MovieRepositoryImpl(
    private val api: MoviesAPI,
    private val dao: MovieDao,
) : MovieRepository {

    /**
     * Метод достает информацию из БД о том, является ли фильм "Любимым" и/или "Буду смотреть"
     * и пользовательской оценке фильма.
     *
     * @param movie фильм без категорий и оценки.
     * @return [Movie] фильм с категориями и пользовательской оценкой.
     */
    private suspend fun parseMovieInfo(movie: Movie): Movie {
        val categories = movie.id?.let { dao.getCategoriesForMovie(movieId = it) }

        movie.isFavorite = categories?.contains(LocalCategory.Favourite) == true
        movie.isWillWatch = categories?.contains(LocalCategory.WillWatch) == true

        movie.userRate = movie.id?.let { dao.getMovieUserRate(movieId = it) }

        return movie
    }

    override suspend fun getMovieById(id: Int): Movie {
        val dto = api.getMovieById(id = id)
        val entity = DtoToEntity.map(movie = dto)

        dao.insert(entity)

        val movie = EntityToDomain.map(movie = entity)

        return parseMovieInfo(movie = movie)
    }

    override suspend fun searchMoviesByName(query: String): MoviesResponce {
        val responseDto = api.searchMovieByName(query = query)

        val response = MoviesResponce(
            movies = responseDto.movieDtos.map { movieDto ->

                val entity = DtoToEntity.map(movieDto)

                val movie = EntityToDomain.map(movie = entity)
                parseMovieInfo(movie)
            },
            total = responseDto.total,
            limit = responseDto.limit,
            page = responseDto.page,
            pages = responseDto.pages
        )
        return response
    }

    override suspend fun searchMoviesWithFilters(
        fields: List<String>?,
        sortTypes: List<Int>?,
        types: List<String>?,
        isSeries: Boolean?,
        years: List<String>?,
        kpRating: List<String>?,
        genres: List<String>?,
        countries: List<String>?,
        inCollection: List<String>?,
    ): MoviesResponce {
        val responseDto = api.searchWithFilter(
            fields = fields,
            sortTypes = sortTypes,
            types = types,
            isSeries = isSeries,
            years = years,
            kpRating = kpRating,
            genres = genres,
            countries = countries,
            inCollection = inCollection
        )
        val response = MoviesResponce(
            movies = responseDto.movieDtos.map { movieDto ->

                val entity = DtoToEntity.map(movieDto)

                val movie = EntityToDomain.map(movie = entity)
                parseMovieInfo(movie)
            },
            total = responseDto.total,
            limit = responseDto.limit,
            page = responseDto.page,
            pages = responseDto.pages
        )
        return response
    }

    override fun getFavouriteMovies(): Flow<List<Movie>> {
        val movieEntities = dao.getMoviesOfCategory(category = LocalCategory.Favourite.name)
        return movieEntities.map { list ->
            list.map { movieEntity ->
                val movie = EntityToDomain.map(movie = movieEntity)
                parseMovieInfo(movie = movie)
            }
        }
    }

    override fun getMoviesWithUserRate(): Flow<List<Movie>> {
        val moviesWRating = dao.getMoviesWithUserRate()

        return moviesWRating.map { list ->
            list.map { wrating ->
                val movie = EntityToDomain.map(wrating.movie)
                movie.userRate = wrating.userRating
                parseMovieInfo(movie = movie)
            }
        }
    }

    override fun getWillWatchMovies(): Flow<List<Movie>> {
        val movieEntities = dao.getMoviesOfCategory(category = LocalCategory.WillWatch.name)
        return movieEntities.map { list ->
            list.map { movieEntity ->
                val movie = EntityToDomain.map(movie = movieEntity)
                parseMovieInfo(movie = movie)
            }
        }
    }

    override suspend fun addMovieToFavourites(id: Int): Boolean {
        try {
            dao.addMovieToCategory(movieId = id, category = LocalCategory.Favourite.name)
            return true
        } catch (e: Exception) {
            Log.e("AddMovieToFavourites", e.message.toString())
        }
        return false
    }

    override suspend fun removeMovieFromFavourites(id: Int): Boolean {
        try {
            dao.removeMovieFromCategory(movieId = id, category = LocalCategory.Favourite.name)
            return true
        } catch (e: Exception) {
            Log.e("RemoveMovieFromFavourites", e.message.toString())
        }
        return false
    }

    override suspend fun setUserRateToMovie(movieId: Int, rate: Int): Boolean {
        try {
            if (rate !in 1..10) throw Exception(message = "Wrong user rating (less 1 or more 10)!")

            dao.setUserRateToMovie(movieId = movieId, rating = rate)
            return true
        } catch (e: Exception) {
            Log.e("SetUserRateToMovie", e.message.toString())
        }
        return false
    }

    override suspend fun removeUserRateFromMovie(movieId: Int): Boolean {
        try {
            dao.removeUserRateFromMovie(movieId = movieId)
            return true
        } catch (e: Exception) {
            Log.e("RemoveUserRateFromMovie", e.message.toString())
        }
        return false
    }

    override suspend fun addMovieToWillWatch(id: Int): Boolean {
        try {
            dao.addMovieToCategory(movieId = id, category = LocalCategory.WillWatch.name)
            return true
        } catch (e: Exception) {
            Log.e("AddMovieToWillWatch", e.message.toString())
        }
        return false
    }

    override suspend fun removeMovieFromWillWatch(id: Int): Boolean {
        try {
            dao.removeMovieFromCategory(movieId = id, category = LocalCategory.WillWatch.name)
            return true
        } catch (e: Exception) {
            Log.e("RemoveMovieFromWillWatch", e.message.toString())
        }
        return false
    }

    override fun getRecommendedFilms(): Flow<List<Movie>> {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val movieEntities = api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    isSeries = false        // обозначаем что ищем фильмы (не сериалы)
                ).movieDtos.map { movieDto ->
                    DtoToEntity.map(movie = movieDto)
                }

                movieEntities.forEach { entity ->
                    entity.id?.let {
                        dao.addMovieToCategory(
                            movieId = it, category = LocalCategory.RecomendedFilms.name
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GetRecommendedFilms", e.message.toString())
        }
        return dao.getMoviesOfCategory(category = LocalCategory.RecomendedFilms.name).map { list ->
            list.map { entity ->
                parseMovieInfo(movie = EntityToDomain.map(entity))
            }
        }
    }

    override fun getRecommendedSeries(): Flow<List<Movie>> {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val movieEntities = api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    isSeries = true         // обозначаем что ищем сериалы (не фильмы)
                ).movieDtos.map { movieDto ->
                    DtoToEntity.map(movie = movieDto)
                }

                movieEntities.forEach { entity ->
                    entity.id?.let {
                        dao.addMovieToCategory(
                            movieId = it, category = LocalCategory.RecomendedSeries.name
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GetRecommendedSeries", e.message.toString())
        }
        return dao.getMoviesOfCategory(category = LocalCategory.RecomendedSeries.name).map { list ->
            list.map { entity ->
                parseMovieInfo(movie = EntityToDomain.map(entity))
            }
        }
    }

    override fun getDetectiveMovies(): Flow<List<Movie>> {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val movieEntities = api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    genres = listOf("детектив") // обозначение конкретного жарна
                ).movieDtos.map { movieDto ->
                    DtoToEntity.map(movie = movieDto)
                }

                movieEntities.forEach { entity ->
                    entity.id?.let {
                        dao.addMovieToCategory(
                            movieId = it, category = LocalCategory.Detectives.name
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GetDetectiveMovies", e.message.toString())
        }
        return dao.getMoviesOfCategory(category = LocalCategory.Detectives.name).map { list ->
            list.map { entity ->
                parseMovieInfo(movie = EntityToDomain.map(entity))
            }
        }
    }

    override fun getRomanMovies(): Flow<List<Movie>> {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val movieEntities = api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    genres = listOf("роман") // обозначение конкретного жарна
                ).movieDtos.map { movieDto ->
                    DtoToEntity.map(movie = movieDto)
                }

                movieEntities.forEach { entity ->
                    entity.id?.let {
                        dao.addMovieToCategory(
                            movieId = it, category = LocalCategory.Romans.name
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GetRomanMovies", e.message.toString())
        }
        return dao.getMoviesOfCategory(category = LocalCategory.Romans.name).map { list ->
            list.map { entity ->
                parseMovieInfo(movie = EntityToDomain.map(entity))
            }
        }
    }

    override fun getComedyMovies(): Flow<List<Movie>> {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val movieEntities = api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    genres = listOf("комедия") // обозначение конкретного жарна
                ).movieDtos.map { movieDto ->
                    DtoToEntity.map(movie = movieDto)
                }

                movieEntities.forEach { entity ->
                    entity.id?.let {
                        dao.addMovieToCategory(
                            movieId = it, category = LocalCategory.Comedys.name
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GetComedyMovies", e.message.toString())
        }
        return dao.getMoviesOfCategory(category = LocalCategory.Comedys.name).map { list ->
            list.map { entity ->
                parseMovieInfo(movie = EntityToDomain.map(entity))
            }
        }
    }
}