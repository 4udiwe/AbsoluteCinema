package com.example.data.repository

import com.example.data.local.dao.MovieDao
import com.example.data.mapper.DtoToEntity
import com.example.data.mapper.EntityToDomain
import com.example.data.remote.api.MoviesAPI
import com.example.domain.model.Movie
import com.example.domain.model.MoviesResponce
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

/**
 * Реализует логику [MovieRepository]
 *
 * @property api экземпляр [MoviesAPI]
 * @property dao экземпляр [MovieDao]
 */
class MovieRepositoryImpl(
    private val api: MoviesAPI,
    private val dao: MovieDao
) : MovieRepository {
    override suspend fun getMovieById(id: Int): Movie {
        val dto = api.getMovieById(id = id)
        val entity =  DtoToEntity.map(movie = dto)

        entity.userRate = dao.getMovieUserRate(entity.id!!)

        dao.insert(entity)

        TODO("добавить парсинг категории")

        return EntityToDomain.map(entity)
    }

    override suspend fun searchMoviesByName(query: String): MoviesResponce {
        val responceDto = api.searchMovieByName(query = query)
        
        val responce = MoviesResponce(
            movies = responceDto.movieDtos.map {
                movieDto ->

                val entity = DtoToEntity.map(movieDto)
                entity.userRate = dao.getMovieUserRate(entity.id!!)

                TODO("добавить парсинг категории")

                EntityToDomain.map(movie = entity)
            },
            total = responceDto.total,
            limit = responceDto.limit,
            page = responceDto.page,
            pages = responceDto.pages
        )
        return responce
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
        TODO("Not yet implemented")
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

    override fun getRecomendedFilms(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getRecomendedSeries(): Flow<List<Movie>> {
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