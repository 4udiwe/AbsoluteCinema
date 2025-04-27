package com.example.data

import com.example.data.local.dao.*
import com.example.data.local.entity.*
import com.example.data.local.entity.category.MovieCategoryCrossRef
import com.example.data.logger.TestLogger
import com.example.data.remote.api.MoviesAPI
import com.example.data.remote.dto.common.Country
import com.example.data.remote.dto.common.Genre
import com.example.data.remote.dto.common.MovieDto
import com.example.data.remote.dto.common.PersonSimple
import com.example.data.remote.dto.common.SequelsAndPrequels
import com.example.data.remote.dto.common.SimilarMovie
import com.example.data.remote.dto.responce.MoviesResponseDto
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.model.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private lateinit var api: MoviesAPI
    private lateinit var movieDao: MovieDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var countryDao: CountryDao
    private lateinit var factDao: FactDao
    private lateinit var genreDao: GenreDao
    private lateinit var personDao: PersonDao
    private lateinit var seqAndPreqDao: SeqAndPreqDao
    private lateinit var similarDao: SimilarDao
    private lateinit var logger: TestLogger

    @Before
    fun setUp() {
        api = mock()
        movieDao = mock()
        categoryDao = mock()
        countryDao = mock()
        factDao = mock()
        genreDao = mock()
        personDao = mock()
        seqAndPreqDao = mock()
        similarDao = mock()
        logger = mock()

        repository = MovieRepositoryImpl(
            api = api,
            movieDao = movieDao,
            categoryDao = categoryDao,
            countryDao = countryDao,
            factDao = factDao,
            genreDao = genreDao,
            personDao = personDao,
            seqAndPreqDao = seqAndPreqDao,
            similarDao = similarDao,
            logger = logger
        )
    }

    @Test
    fun `getMovieById should return movie from local DB if exists`() = runTest {
        // Arrange
        val movieId = 123
        val localMovie = MovieEntity(id = movieId, name = "Local Movie")
        val expectedMovie = Movie(id = movieId, name = "Local Movie")

        `when`(movieDao.getMovieById(movieId)).thenReturn(localMovie)
        `when`(categoryDao.getCategoriesForMovie(movieId)).thenReturn(emptyList())
        `when`(countryDao.getCountryForMovie(movieId)).thenReturn(emptyList())
        `when`(genreDao.getGenresForMovie(movieId)).thenReturn(emptyList())
        `when`(factDao.getFactsForMovie(movieId)).thenReturn(emptyList())
        `when`(personDao.getPersonsForMovie(movieId)).thenReturn(emptyList())
        `when`(seqAndPreqDao.getSequelsForMovie(movieId)).thenReturn(emptyList())
        `when`(similarDao.getSimilarsForMovie(movieId)).thenReturn(emptyList())

        // Act
        val result = repository.getMovieById(movieId)

        // Assert
        assertEquals(expectedMovie.id, result.id)
        assertEquals(expectedMovie.name, result.name)
        verify(api, never()).getMovieById(any())
    }

    @Test
    fun `getMovieById should fetch from API and save if not in local DB`() = runTest {
        // Arrange
        val movieId = 123
        val apiMovieDto = MovieDto(id = movieId, name = "API Movie")
        val expectedMovie = Movie(id = movieId, name = "API Movie")

        `when`(movieDao.getMovieById(movieId)).thenReturn(null)
        `when`(api.getMovieById(movieId)).thenReturn(apiMovieDto)
        `when`(categoryDao.getCategoriesForMovie(movieId)).thenReturn(emptyList())
        `when`(countryDao.getCountryForMovie(movieId)).thenReturn(emptyList())
        `when`(genreDao.getGenresForMovie(movieId)).thenReturn(emptyList())
        `when`(factDao.getFactsForMovie(movieId)).thenReturn(emptyList())
        `when`(personDao.getPersonsForMovie(movieId)).thenReturn(emptyList())
        `when`(seqAndPreqDao.getSequelsForMovie(movieId)).thenReturn(emptyList())
        `when`(similarDao.getSimilarsForMovie(movieId)).thenReturn(emptyList())

        // Act
        val result = repository.getMovieById(movieId)

        // Assert
        assertEquals(expectedMovie.id, result.id)
        assertEquals(expectedMovie.name, result.name)
        verify(api).getMovieById(movieId)
        verify(movieDao).insert(any())
    }

    @Test
    fun `searchMoviesByName should return mapped response`() = runTest {
        // Arrange
        val query = "test"
        val apiResponse = MoviesResponseDto(
            movieDtos = listOf(MovieDto(id = 1, name = "Test Movie")),
            total = 1,
            limit = 10,
            page = 1,
            pages = 1
        )

        `when`(api.searchMovieByName(query)).thenReturn(apiResponse)
        `when`(categoryDao.getCategoriesForMovie(any())).thenReturn(emptyList())
        `when`(countryDao.getCountryForMovie(any())).thenReturn(emptyList())
        `when`(genreDao.getGenresForMovie(any())).thenReturn(emptyList())
        `when`(factDao.getFactsForMovie(any())).thenReturn(emptyList())
        `when`(personDao.getPersonsForMovie(any())).thenReturn(emptyList())
        `when`(seqAndPreqDao.getSequelsForMovie(any())).thenReturn(emptyList())
        `when`(similarDao.getSimilarsForMovie(any())).thenReturn(emptyList())

        // Act
        val result = repository.searchMoviesByName(query)

        // Assert
        assertEquals(1, result.movies.size)
        assertEquals("Test Movie", result.movies[0].name)
        assertEquals(1, result.total)
        verify(movieDao).insert(any())
    }

    @Test
    fun `addMovieToFavourites should return true on success`() = runTest {
        // Arrange
        val movieId = 123
        val category = LocalCategory.Favourite.name
        `when`(categoryDao.addCategoryToMovie(any())).thenReturn(Unit)
        `when`(categoryDao.getIdForCategory(category)).thenReturn(category.hashCode())

        // Act
        val result = repository.addMovieToFavourites(movieId)

        // Assert
        assertEquals(true, result)
        val captor = argumentCaptor<MovieCategoryCrossRef>()
        verify(categoryDao).addCategoryToMovie(captor.capture())
        assertEquals(movieId, captor.firstValue.movieId)
        assertEquals("Favourite".hashCode(), captor.firstValue.categoryId)
    }

    @Test
    fun `removeMovieFromFavourites should return true on success`() = runTest {
        // Arrange
        val movieId = 123
        `when`(categoryDao.deleteCategoryFromMovie(eq(movieId), eq("Favourite"))).thenReturn(Unit)

        // Act
        val result = repository.removeMovieFromFavourites(movieId)

        // Assert
        assertTrue(result)
        verify(categoryDao).deleteCategoryFromMovie(movieId, "Favourite")
    }

    @Test
    fun `setUserRateToMovie should return true for valid rate`() = runTest {
        // Arrange
        val movieId = 123
        val rate = 5
        `when`(movieDao.setUserRateToMovie(eq(movieId), eq(rate))).thenReturn(Unit)

        // Act
        val result = repository.setUserRateToMovie(movieId, rate)

        // Assert
        assertTrue(result)
        verify(movieDao).setUserRateToMovie(movieId, rate)
    }

    @Test
    fun `setUserRateToMovie should return false for invalid rate`() = runTest {
        // Arrange
        val movieId = 123
        val invalidRate = 11

        // Act
        val result = repository.setUserRateToMovie(movieId, invalidRate)

        // Assert
        assertFalse(result)
        verify(movieDao, never()).setUserRateToMovie(any(), any())
    }

    @Test
    fun `getFavouriteMovies should return flow of favourite movies`() = runTest {
        // Arrange
        val movieEntity = MovieEntity(id = 1, name = "Favourite Movie")
        val expectedMovie = Movie(id = 1, name = "Favourite Movie")
        `when`(categoryDao.getMoviesByCategory("Favourite")).thenReturn(flowOf(listOf(movieEntity)))
        `when`(categoryDao.getCategoriesForMovie(any())).thenReturn(emptyList())
        `when`(countryDao.getCountryForMovie(any())).thenReturn(emptyList())
        `when`(genreDao.getGenresForMovie(any())).thenReturn(emptyList())
        `when`(factDao.getFactsForMovie(any())).thenReturn(emptyList())
        `when`(personDao.getPersonsForMovie(any())).thenReturn(emptyList())
        `when`(seqAndPreqDao.getSequelsForMovie(any())).thenReturn(emptyList())
        `when`(similarDao.getSimilarsForMovie(any())).thenReturn(emptyList())

        // Act
        val result = repository.getFavouriteMovies().firstOrNull()

        // Assert
        assertNotNull(result)
        assertEquals(1, result?.size)
        assertEquals(expectedMovie.id, result?.get(0)?.id)
        assertEquals(expectedMovie.name, result?.get(0)?.name)
    }

    @Test
    fun `saveMovieFromDto should save all related data`() = runTest {
        // Arrange
        val movieDto = MovieDto(
            id = 1,
            name = "Test Movie",
            genres = arrayListOf(Genre(name = "Action")),
            countries = arrayListOf(Country(name = "USA")),
            persons = arrayListOf(
                PersonSimple(
                    id = 101,
                    name = "Actor",
                    photo = "photo_url"
                )
            ),
            sequelsAndPrequels = arrayListOf(
                SequelsAndPrequels(
                    id = 201,
                    name = "Sequel"
                )
            ),
            similarMovies = arrayListOf(
                SimilarMovie(
                    id = 301,
                    name = "Similar"
                )
            )
        )

        // Act
        repository.saveMovieFromDto(movieDto)

        // Assert
        verify(movieDao).insert(any())
        verify(genreDao).addGenre(any())
        verify(genreDao).addGenreToMovie(any())
        verify(countryDao).addCountry(any())
        verify(countryDao).addCountryToMovie(any())
        verify(personDao).addPerson(any())
        verify(personDao).addPersonToMovie(any())
        verify(seqAndPreqDao).addSequel(any())
        verify(similarDao).addSimilar(any())
    }

    @Test
    fun `searchWithFilters should return mapped response`() = runTest {
        // Arrange
        val fields = listOf("top250")
        val apiResponse = MoviesResponseDto(
            movieDtos = listOf(MovieDto(id = 1, name = "Filtered Movie")),
            total = 1,
            limit = 10,
            page = 1,
            pages = 1
        )

        `when`(
            api.searchWithFilter(
                fields = eq(fields),
                sortTypes = isNull(),
                types = isNull(),
                isSeries = isNull(),
                years = isNull(),
                kpRating = isNull(),
                genres = isNull(),
                countries = isNull(),
                inCollection = isNull()
            )
        ).thenReturn(apiResponse)

        `when`(categoryDao.getCategoriesForMovie(any())).thenReturn(emptyList())
        `when`(countryDao.getCountryForMovie(any())).thenReturn(emptyList())
        `when`(genreDao.getGenresForMovie(any())).thenReturn(emptyList())
        `when`(factDao.getFactsForMovie(any())).thenReturn(emptyList())
        `when`(personDao.getPersonsForMovie(any())).thenReturn(emptyList())
        `when`(seqAndPreqDao.getSequelsForMovie(any())).thenReturn(emptyList())
        `when`(similarDao.getSimilarsForMovie(any())).thenReturn(emptyList())

        // Act
        val result = repository.searchMoviesWithFilters(
            fields = fields,
            sortTypes = null,
            types = null,
            isSeries = null,
            years = null,
            kpRating = null,
            genres = null,
            countries = null,
            inCollection = null
        )

        // Assert
        assertEquals(1, result.movies.size)
        assertEquals("Filtered Movie", result.movies[0].name)
        verify(movieDao).insert(any())
    }

    @Test
    fun `getMovieById should log error when API call fails`() = runTest {
        // Arrange
        val movieId = 123
        val errorMessage = "Network error"

        `when`(movieDao.getMovieById(movieId)).thenReturn(null)
        `when`(api.getMovieById(movieId)).thenThrow(RuntimeException(errorMessage))

        // Act
        val result = repository.getMovieById(movieId)

        // Assert
        assertEquals(result, Movie())
        verify(logger).log(eq("GetMovieById"), contains(errorMessage))
    }

    @Test
    fun `addMovieToFavourites should return false and log error when DB operation fails`() = runTest {
        // Arrange
        val movieId = 123
        val errorMessage = "DB error"
        `when`(categoryDao.getIdForCategory(LocalCategory.Favourite.name)).thenReturn(LocalCategory.Favourite.name.hashCode())
        `when`(categoryDao.addCategoryToMovie(any())).thenThrow(RuntimeException(errorMessage))

        // Act
        val result = repository.addMovieToFavourites(movieId)

        // Assert
        assertFalse(result)
        verify(logger).log(eq("AddMovieToFavourites"), contains(errorMessage))
    }

    @Test
    fun `searchMoviesByName should return empty result and log error when API fails`() = runTest {
        // Arrange
        val query = "test"
        val errorMessage = "API unavailable"
        `when`(api.searchMovieByName(query)).thenThrow(RuntimeException(errorMessage))

        // Act
        val result = repository.searchMoviesByName(query)

        // Assert
        assertEquals(0, result.movies.size)
        assertEquals(0, result.total)
        verify(logger).log(eq("SearchMoviesByName"), contains(errorMessage))
        verify(movieDao, never()).insert(any())
    }
}