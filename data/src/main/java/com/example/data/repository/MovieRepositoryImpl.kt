package com.example.data.repository

import com.example.data.local.dao.MovieDao
import com.example.data.mapper.DtoToEntity
import com.example.data.mapper.EntityToDomain
import com.example.data.remote.api.MoviesAPI
import com.example.domain.model.LocalCategory
import com.example.domain.model.Movie
import com.example.domain.model.MoviesResponce
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun getWillWatchMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMovieToFavourites(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeMovieFromFavourites(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setUserRateToMovie(movieId: Int, rate: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserRateFromMovie(movieId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addMovieToWillWatch(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeMovieFromWillWatch(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRecommendedFilms(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getRecommendedSeries(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getDetectiveMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getRomanMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getComedyMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }
}