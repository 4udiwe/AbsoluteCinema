package com.example.data.repository

import android.util.Log
import com.example.data.local.dao.*
import com.example.data.local.entity.*
import com.example.data.local.entity.category.MovieCategoryCrossRef
import com.example.data.local.entity.country.CountryEntity
import com.example.data.local.entity.country.MovieCountryCrossRef
import com.example.data.local.entity.genre.GenreEntity
import com.example.data.local.entity.genre.MovieGenreCrossRef
import com.example.data.local.entity.person.MoviePersonCrossRef
import com.example.data.local.entity.person.PersonSimpleEntity
import com.example.data.local.entity.seqandpreq.SeqAndPreqEntity
import com.example.data.local.entity.similar.SimilarMovieEntity
import com.example.data.mapper.DtoToEntity
import com.example.data.mapper.EntityToDomain
import com.example.data.remote.api.MoviesAPI
import com.example.data.remote.dto.common.MovieDto
import com.example.domain.model.Country
import com.example.domain.model.Fact
import com.example.domain.model.Genre
import com.example.domain.model.LocalCategory
import com.example.domain.model.Movie
import com.example.domain.model.MoviesResponce
import com.example.domain.model.Person
import com.example.domain.model.SeqAndPreq
import com.example.domain.model.SimilarMovie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val api: MoviesAPI,
    private val movieDao: MovieDao,
    private val categoryDao: CategoryDao,
    private val countryDao: CountryDao,
    private val factDao: FactDao,
    private val genreDao: GenreDao,
    private val personDao: PersonDao,
    private val seqAndPreqDao: SeqAndPreqDao,
    private val similarDao: SimilarDao,
) : MovieRepository {

    suspend fun parseMovieInfo(movie: Movie): Movie {
        val categories = categoryDao.getCategoriesForMovie(movie.id!!).map { it.name?.name }

        movie.isFavorite = categories.contains(LocalCategory.Favourite.name) == true
        movie.isWillWatch = categories.contains(LocalCategory.WillWatch.name) == true
        movie.userRate = movie.id?.let { movieDao.getMovieUserRate(it) }

        movie.countries = countryDao.getCountryForMovie(movieId = movie.id!!).map {
            Country(id = it.id, name = it.name)
        }
        movie.genres = genreDao.getGenresForMovie(movieId = movie.id!!).map {
            Genre(id = it.id, name = it.name)
        }
        movie.facts = factDao.getFactsForMovie(movieId = movie.id!!).map {
            Fact(id = it.id, fact = it.fact, type = it.type, spoiler = it.spoiler)
        }
        movie.persons = personDao.getPersonsForMovie(movieId = movie.id!!).map {
            Person(
                id = it.id,
                photo = it.photo,
                name = it.name,
                enName = it.enName,
                description = it.description,
                profession = it.profession,
                enProfession = it.enProfession
            )
        }
        movie.sequelsAndPrequels = seqAndPreqDao.getSequelsForMovie(movieId = movie.id!!).map {
            SeqAndPreq(
                id = it.id,
                name = it.name,
                enName = it.enName,
                alternativeName = it.alternativeName,
                type = it.type,
                poster = com.example.domain.model.Poster(
                    posterUrl = it.poster?.posterUrl,
                    posterPreviewUrl = it.poster?.posterPreviewUrl
                ),
                rating = com.example.domain.model.Rating(
                    kp = it.rating?.kp,
                    imdb = it.rating?.imdb,
                    tmdb = it.rating?.tmdb,
                    filmCritics = it.rating?.filmCritics,
                    russianFilmCritics = it.rating?.russianFilmCritics,
                    await = it.rating?.await
                ),
                year = it.year,
            )
        }
        movie.similarMovies = similarDao.getSimilarsForMovie(movieId = movie.id!!).map {
            SimilarMovie(
                id = it.id,
                name = it.name,
                enName = it.enName,
                alternativeName = it.alternativeName,
                type = it.type,
                poster = com.example.domain.model.Poster(
                    posterUrl = it.poster?.posterUrl,
                    posterPreviewUrl = it.poster?.posterPreviewUrl
                ),
                rating = com.example.domain.model.Rating(
                    kp = it.rating?.kp,
                    imdb = it.rating?.imdb,
                    tmdb = it.rating?.tmdb,
                    filmCritics = it.rating?.filmCritics,
                    russianFilmCritics = it.rating?.russianFilmCritics,
                    await = it.rating?.await
                ),
                year = it.year,
            )
        }

        return movie
    }

    private suspend fun saveMovieFromDto(dto: MovieDto) {
        val entity = DtoToEntity.map(dto)

        withContext(Dispatchers.IO) {
            // Вставка самого фильма
            movieDao.insert(entity)

            // Сохраняем связанные данные
            // Жанры
            dto.genres.forEach { genre ->
                genreDao.addGenre(GenreEntity(name = genre.name))
                genreDao.addGenreToMovie(MovieGenreCrossRef(entity.id!!, genre.name.hashCode()))
            }
            // Страны
            dto.countries.forEach { country ->
                countryDao.addCountry(CountryEntity(name = country.name))
                countryDao.addCountryToMovie(
                    MovieCountryCrossRef(
                        entity.id!!,
                        country.name.hashCode()
                    )
                )
            }
            // Персоны
            dto.persons.forEach { person ->
                personDao.addPerson(
                    PersonSimpleEntity(
                        id = person.id,
                        name = person.name,
                        photo = person.photo,
                        enName = person.enName,
                        description = person.description,
                        profession = person.profession,
                        enProfession = person.enProfession
                    )
                )
                personDao.addPersonToMovie(MoviePersonCrossRef(entity.id!!, person.id!!))
            }
            // Сиквелы и приквелы
            dto.sequelsAndPrequels.forEach { sequel ->
                seqAndPreqDao.addSequel(
                    SeqAndPreqEntity(
                        id = sequel.id,
                        name = sequel.name,
                        enName = sequel.enName,
                        alternativeName = sequel.alternativeName,
                        type = sequel.type,
                        poster = Poster(
                            posterUrl = sequel.poster?.url,
                            posterPreviewUrl = sequel.poster?.previewUrl
                        ),
                        rating = Rating(
                            kp = sequel.rating?.kp,
                            imdb = sequel.rating?.imdb,
                            tmdb = sequel.rating?.tmdb,
                            filmCritics = sequel.rating?.filmCritics,
                            russianFilmCritics = sequel.rating?.russianFilmCritics,
                            await = sequel.rating?.await
                        ),
                        year = sequel.year
                    )
                )
                // Похожие
                dto.similarMovies.forEach { similar ->
                    similarDao.addSimilar(
                        SimilarMovieEntity(
                            id = sequel.id,
                            name = sequel.name,
                            enName = sequel.enName,
                            alternativeName = sequel.alternativeName,
                            type = sequel.type,
                            poster = Poster(
                                posterUrl = sequel.poster?.url,
                                posterPreviewUrl = sequel.poster?.previewUrl
                            ),
                            rating = Rating(
                                kp = sequel.rating?.kp,
                                imdb = sequel.rating?.imdb,
                                tmdb = sequel.rating?.tmdb,
                                filmCritics = sequel.rating?.filmCritics,
                                russianFilmCritics = sequel.rating?.russianFilmCritics,
                                await = sequel.rating?.await
                            ),
                            year = sequel.year
                        )
                    )
                }

            }
        }
    }

    override suspend fun getMovieById(id: Int): Movie {
        var entity = movieDao.getMovieById(id)

        if (entity == null) {
            val dto = api.getMovieById(id)
            entity = DtoToEntity.map(dto)

            saveMovieFromDto(dto = dto)
        }

        val movie = EntityToDomain.map(entity)
        return parseMovieInfo(movie)
    }

    override suspend fun searchMoviesByName(query: String): MoviesResponce {
        val responseDto = api.searchMovieByName(query)

        return MoviesResponce(
            movies = responseDto.movieDtos.map { dto ->
                val entity = DtoToEntity.map(dto)
                movieDao.insert(entity)
                saveMovieFromDto(dto = dto)
                parseMovieInfo(EntityToDomain.map(entity))
            },
            total = responseDto.total,
            limit = responseDto.limit,
            page = responseDto.page,
            pages = responseDto.pages
        )
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
            fields, sortTypes, types, isSeries, years,
            kpRating, genres, countries, inCollection
        )

        return MoviesResponce(
            movies = responseDto.movieDtos.map { dto ->
                val entity = DtoToEntity.map(dto)
                movieDao.insert(entity)
                parseMovieInfo(EntityToDomain.map(entity))
            },
            total = responseDto.total,
            limit = responseDto.limit,
            page = responseDto.page,
            pages = responseDto.pages
        )
    }

    override fun getFavouriteMovies(): Flow<List<Movie>> {
        return categoryDao.getMoviesByCategory(LocalCategory.Favourite.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getMoviesWithUserRate(): Flow<List<Movie>> {
        return movieDao.getMoviesWithUserRate()
            .map { list ->
                list.map {
                    val movie = EntityToDomain.map(it.movie)
                    movie.userRate = it.userRating
                    parseMovieInfo(movie)
                }
            }
    }

    override fun getWillWatchMovies(): Flow<List<Movie>> {
        return categoryDao.getMoviesByCategory(LocalCategory.WillWatch.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override suspend fun addMovieToFavourites(id: Int): Boolean {
        return try {
            categoryDao.addCategoryToMovie(
                MovieCategoryCrossRef(
                    movieId = id,
                    categoryId = LocalCategory.Favourite.name.hashCode()
                )
            )
            true
        } catch (e: Exception) {
            Log.e("AddMovieToFavourites", e.message.toString())
            false
        }
    }

    override suspend fun removeMovieFromFavourites(id: Int): Boolean {
        return try {
            categoryDao.deleteCategoryFromMovie(
                movieId = id,
                category = LocalCategory.Favourite.name
            )
            true
        } catch (e: Exception) {
            Log.e("RemoveMovieFromFavourites", e.message.toString())
            false
        }
    }

    override suspend fun setUserRateToMovie(movieId: Int, rate: Int): Boolean {
        return try {
            if (rate !in 1..10) throw Exception("Wrong user rating (less 1 or more 10)!")
            movieDao.setUserRateToMovie(movieId, rate)
            true
        } catch (e: Exception) {
            Log.e("SetUserRateToMovie", e.message.toString())
            false
        }
    }

    override suspend fun removeUserRateFromMovie(movieId: Int): Boolean {
        return try {
            movieDao.removeUserRateFromMovie(movieId)
            true
        } catch (e: Exception) {
            Log.e("RemoveUserRateFromMovie", e.message.toString())
            false
        }
    }

    override suspend fun addMovieToWillWatch(id: Int): Boolean {
        return try {
            categoryDao.addCategoryToMovie(
                MovieCategoryCrossRef(
                    movieId = id,
                    categoryId = LocalCategory.WillWatch.name.hashCode()
                )
            )
            true
        } catch (e: Exception) {
            Log.e("AddMovieToWillWatch", e.message.toString())
            false
        }
    }

    override suspend fun removeMovieFromWillWatch(id: Int): Boolean {
        return try {
            categoryDao.deleteCategoryFromMovie(
                movieId = id,
                category = LocalCategory.WillWatch.name
            )
            true
        } catch (e: Exception) {
            Log.e("RemoveMovieFromWillWatch", e.message.toString())
            false
        }
    }

    override fun getRecommendedFilms(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    isSeries = false
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            entity.id!!,
                            LocalCategory.RecomendedFilms.name.hashCode()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GetRecommendedFilms", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.RecomendedFilms.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getRecommendedSeries(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    isSeries = true
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            entity.id!!,
                            LocalCategory.RecomendedSeries.name.hashCode()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GetRecommendedSeries", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.RecomendedSeries.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getDetectiveMovies(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    genres = listOf("детектив")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            entity.id!!,
                            LocalCategory.Detectives.name.hashCode()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GetDetectiveMovies", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.Detectives.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getRomanMovies(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    genres = listOf("роман")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            entity.id!!,
                            LocalCategory.Romans.name.hashCode()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GetRomanMovies", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.Romans.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getComedyMovies(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    fields = listOf("top250"),
                    sortTypes = listOf(1),
                    genres = listOf("комедия")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            entity.id!!,
                            LocalCategory.Comedys.name.hashCode()
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GetComedyMovies", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.Comedys.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override suspend fun clearCache() {
//        withContext(Dispatchers.IO) {
//            movieDao.clearAll()
//            categoryDao.clearAll()
//            genreDao.clearAll()
//            countryDao.clearAll()
//            personDao.clearAll()
//        }
    }
}