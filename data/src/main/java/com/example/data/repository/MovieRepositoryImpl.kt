package com.example.data.repository

import com.example.data.local.dao.*
import com.example.data.local.entity.*
import com.example.data.local.entity.category.MovieCategoryCrossRef
import com.example.data.local.entity.country.CountryEntity
import com.example.data.local.entity.country.MovieCountryCrossRef
import com.example.data.local.entity.fact.FactEntity
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
import com.example.domain.logger.Logger
import com.example.domain.model.Country
import com.example.domain.model.Fact
import com.example.domain.model.Filter
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
    private val logger: Logger
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

    suspend fun saveMovieFromDto(dto: MovieDto) {
        val entity = DtoToEntity.map(dto)

        withContext(Dispatchers.IO) {
            // Вставка самого фильма
            movieDao.insert(entity)

            // Сохраняем связанные данные
            // Жанры
            dto.genres.forEach { genre ->
                genreDao.addGenre(GenreEntity(id = genre.name.hashCode(), name = genre.name.toString()))
                genreDao.addGenreToMovie(MovieGenreCrossRef(
                    movieId = entity.id!!,
                    genreId = genre.name.hashCode()
                ))
            }
            // Страны
            dto.countries.forEach { country ->
                countryDao.addCountry(CountryEntity(id = country.name.hashCode(), name = country.name.toString()))
                countryDao.addCountryToMovie(
                    MovieCountryCrossRef(
                        movieId = entity.id!!,
                        countryId = country.name.hashCode()
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
            // Факты
            dto.facts.forEach { fact ->
                factDao.addFact(
                    FactEntity(
                        id = null,
                        fact = fact.value,
                        type = fact.type,
                        spoiler = fact.spoiler,
                        movieId = entity.id!!
                    )
                )
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
                            id = similar.id,
                            name = similar.name,
                            enName = similar.enName,
                            alternativeName = similar.alternativeName,
                            type = similar.type,
                            poster = Poster(
                                posterUrl = similar.poster?.url,
                                posterPreviewUrl = similar.poster?.previewUrl
                            ),
                            rating = Rating(
                                kp = similar.rating?.kp,
                                imdb = similar.rating?.imdb,
                                tmdb = similar.rating?.tmdb,
                                filmCritics = similar.rating?.filmCritics,
                                russianFilmCritics = similar.rating?.russianFilmCritics,
                                await = similar.rating?.await
                            ),
                            year = similar.year
                        )
                    )
                }

            }
        }
    }

    override suspend fun getMovieById(id: Int): Movie {
        val entity = movieDao.getMovieById(id)

        try {
            val dto = api.getMovieById(id)
            saveMovieFromDto(dto = dto)
        } catch (e: Exception) {
            logger.log("GetMovieById", e.message.toString())
        }

        val movie = EntityToDomain.map(entity ?: MovieEntity())
        return parseMovieInfo(movie)
    }

    override suspend fun searchMoviesByName(query: String): MoviesResponce {
        val responseDto = api.searchMovieByName(query)

        return MoviesResponce(
            movies = responseDto.movieDtos.map { dto ->
                val entity = DtoToEntity.map(dto)
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

    override suspend fun getCountryFiltersForSearch(): List<Filter> {
        return try {
            api.getFiltersByFields("countries.name").map { Filter(name = it.name) }
        } catch (e: Exception){
            logger.log("GetCountryFiltersForSearch", e.message.toString())
            listOf<Filter>()
        }
    }

    override suspend fun getGenreFiltersForSearch(): List<Filter> {
        return try {
            api.getFiltersByFields("genres.name").map { Filter(name = it.name) }
        } catch (e: Exception){
            logger.log("GetGenreFiltersForSearch", e.message.toString())
            listOf<Filter>()
        }
    }

    override suspend fun getTypeFiltersForSearch(): List<Filter> {
        return try {
            api.getFiltersByFields("type").map { Filter(name = it.name) }
        } catch (e: Exception){
            logger.log("GetTypeFiltersForSearch", e.message.toString())
            listOf<Filter>()
        }
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
                    categoryId = categoryDao.getIdForCategory(category = LocalCategory.Favourite.name)!!
                )
            )
            true
        } catch (e: Exception) {
            logger.log("AddMovieToFavourites", e.message.toString())
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
            logger.log("RemoveMovieFromFavourites", e.message.toString())
            false
        }
    }

    override suspend fun setUserRateToMovie(movieId: Int, rate: Int): Boolean {
        return try {
            if (rate !in 1..10) throw Exception("Wrong user rating (less 1 or more 10)!")
            movieDao.setUserRateToMovie(movieId, rate)
            true
        } catch (e: Exception) {
            logger.log("MovieRepo", e.message ?: "Unknown error")
            false
        }
    }

    override suspend fun removeUserRateFromMovie(movieId: Int): Boolean {
        return try {
            movieDao.removeUserRateFromMovie(movieId)
            true
        } catch (e: Exception) {
            logger.log("RemoveUserRateFromMovie", e.message.toString())
            false
        }
    }

    override suspend fun addMovieToWillWatch(id: Int): Boolean {
        return try {
            categoryDao.addCategoryToMovie(
                MovieCategoryCrossRef(
                    movieId = id,
                    categoryId = categoryDao.getIdForCategory(category = LocalCategory.WillWatch.name)!!
                )
            )
            true
        } catch (e: Exception) {
            logger.log("AddMovieToWillWatch", e.message.toString())
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
            logger.log("RemoveMovieFromWillWatch", e.message.toString())
            false
        }
    }

    override fun getRecommendedFilms(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    isSeries = false,
                    inCollection = arrayListOf("top250")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            movieId = entity.id!!,
                            categoryId = categoryDao.getIdForCategory(LocalCategory.RecommendedFilms.name)!!,
                        )
                    )
                }
            } catch (e: Exception) {
                logger.log("GetRecommendedFilms", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.RecommendedFilms.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getRecommendedSeries(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    isSeries = true
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            movieId = entity.id!!,
                            categoryId = categoryDao.getIdForCategory(category = LocalCategory.RecommendedSeries.name)!!
                        )
                    )
                }
            } catch (e: Exception) {
                logger.log("GetRecommendedSeries", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.RecommendedSeries.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getDetectiveMovies(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    genres = listOf("детектив"),
                    inCollection = listOf("top250")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            movieId = entity.id!!,
                            categoryId = categoryDao.getIdForCategory(LocalCategory.Detectives.name)!!
                        )
                    )
                }
            } catch (e: Exception) {
                logger.log("GetDetectiveMovies", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.Detectives.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getRomanMovies(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    genres = listOf("драма"),
                    inCollection = listOf("top250")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            movieId = entity.id!!,
                            categoryId = categoryDao.getIdForCategory(LocalCategory.Romans.name)!!
                        )
                    )
                }
            } catch (e: Exception) {
                logger.log("GetRomanMovies", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.Romans.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override fun getComedyMovies(): Flow<List<Movie>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.searchWithFilter(
                    genres = listOf("комедия"),
                    inCollection = listOf("top250")
                ).movieDtos.forEach { dto ->
                    val entity = DtoToEntity.map(dto)
                    saveMovieFromDto(dto = dto)
                    categoryDao.addCategoryToMovie(
                        MovieCategoryCrossRef(
                            movieId = entity.id!!,
                            categoryId = categoryDao.getIdForCategory(LocalCategory.Comedies.name)!!
                        )
                    )
                }
            } catch (e: Exception) {
                logger.log("GetComedyMovies", e.message.toString())
            }
        }
        return categoryDao.getMoviesByCategory(LocalCategory.Comedies.name)
            .map { movies -> movies.map { parseMovieInfo(EntityToDomain.map(it)) } }
    }

    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            movieDao.clearAll()
            categoryDao.clearAll()
            genreDao.clearAll()
            countryDao.clearAll()
            personDao.clearAll()
            factDao.clearAll()
            seqAndPreqDao.clearAll()
            similarDao.clearAll()
        }
    }
}