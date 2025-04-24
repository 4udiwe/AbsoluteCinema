package com.example.data

import com.example.data.local.dao.*
import com.example.data.local.entity.*
import com.example.data.local.entity.category.MovieCategoryCrossRef
import com.example.data.local.entity.userrating.MovieWithRating
import com.example.data.remote.api.MoviesAPI
import com.example.data.remote.dto.common.MovieDto
import com.example.data.remote.dto.responce.MoviesResponseDto
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.model.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MovieRepositoryUnitTestOLD {

    @MockK
    private lateinit var api: MoviesAPI

    @MockK
    private lateinit var movieDao: MovieDao

    @MockK
    private lateinit var categoryDao: CategoryDao

    @MockK
    private lateinit var countryDao: CountryDao

    @MockK
    private lateinit var factDao: FactDao

    @MockK
    private lateinit var genreDao: GenreDao

    @MockK
    private lateinit var personDao: PersonDao

    @MockK
    private lateinit var seqAndPreqDao: SeqAndPreqDao

    @MockK
    private lateinit var similarDao: SimilarDao

    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // Добавлен relaxUnitFun
        repository = MovieRepositoryImpl(
            api = api,
            movieDao = movieDao,
            categoryDao = categoryDao,
            countryDao = countryDao,
            factDao = factDao,
            genreDao = genreDao,
            personDao = personDao,
            seqAndPreqDao = seqAndPreqDao,
            similarDao = similarDao
        )
    }

    @Test
    fun `getMovieById should return movie from local db if exists`() = runBlocking {
        // Arrange
        val movieId = 1
        val localMovie = MovieEntity(id = movieId, name = "Local Movie")
        val expectedMovie = Movie(id = movieId, name = "Local Movie")

        coEvery { movieDao.getMovieById(movieId) } returns localMovie
        coEvery { repository.parseMovieInfo(any()) } returns expectedMovie

        // Act
        val result = repository.getMovieById(movieId)

        // Assert
        assertEquals(expectedMovie, result)
        coVerify(exactly = 0) { api.getMovieById(any()) }
    }

    @Test
    fun `getMovieById should fetch from api and save if not exists locally`() = runBlocking {
        // Arrange
        val movieId = 1
        val apiMovieDto = MovieDto(id = movieId, name = "API Movie")
        val expectedMovie = Movie(id = movieId, name = "API Movie")

        coEvery { movieDao.getMovieById(movieId) } returns null
        coEvery { api.getMovieById(movieId) } returns apiMovieDto
        coEvery { movieDao.insert(any()) } just Runs
        coEvery { repository.saveMovieFromDto(apiMovieDto) } just Runs
        coEvery { repository.parseMovieInfo(any()) } returns expectedMovie

        // Act
        val result = repository.getMovieById(movieId)

        // Assert
        assertEquals(expectedMovie, result)
        coVerify(exactly = 1) { api.getMovieById(movieId) }
        coVerify { movieDao.insert(any()) }
    }

    @Test
    fun `searchMoviesByName should return mapped movies`() = runBlocking {
        // Arrange
        val query = "test"
        val apiResponse = MoviesResponseDto(
            movieDtos = listOf(MovieDto(id = 1, name = "Test Movie")),
            total = 1,
            limit = 10,
            page = 1,
            pages = 1
        )
        val expectedResponse = MoviesResponce(
            movies = listOf(Movie(id = 1, name = "Test Movie")),
            total = 1,
            limit = 10,
            page = 1,
            pages = 1
        )

        coEvery { api.searchMovieByName(query) } returns apiResponse
        coEvery { movieDao.insert(any()) } just Runs
        coEvery { repository.saveMovieFromDto(any()) } just Runs
        coEvery { repository.parseMovieInfo(any()) } returns Movie(id = 1, name = "Test Movie")

        // Act
        val result = repository.searchMoviesByName(query)

        // Assert
        assertEquals(expectedResponse.movies.size, result.movies.size)
        assertEquals(expectedResponse.total, result.total)
    }

    @Test
    fun `getFavouriteMovies should return movies from favourite category`() = runBlocking {
        // Arrange
        val favouriteMovies = listOf(
            MovieEntity(id = 1, name = "Fav 1"),
            MovieEntity(id = 2, name = "Fav 2")
        )
        val expectedMovies = listOf(
            Movie(id = 1, name = "Fav 1"),
            Movie(id = 2, name = "Fav 2")
        )

        every { categoryDao.getMoviesByCategory(LocalCategory.Favourite.name) } returns flowOf(favouriteMovies)
        coEvery { repository.parseMovieInfo(any()) } returnsMany expectedMovies

        // Act
        val result = repository.getFavouriteMovies().toList().first()

        // Assert
        assertEquals(expectedMovies.size, result.size)
        assertEquals(expectedMovies[0].name, result[0].name)
    }

    @Test
    fun `addMovieToFavourites should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery {
            categoryDao.addCategoryToMovie(
                MovieCategoryCrossRef(
                    movieId = movieId,
                    categoryId = LocalCategory.Favourite.name.hashCode()
                )
            )
        } just Runs

        // Act
        val result = repository.addMovieToFavourites(movieId)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `removeMovieFromFavourites should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery {
            categoryDao.deleteCategoryFromMovie(
                movieId = movieId,
                category = LocalCategory.Favourite.name
            )
        } just Runs

        // Act
        val result = repository.removeMovieFromFavourites(movieId)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `setUserRateToMovie should return true for valid rate`() = runBlocking {
        // Arrange
        val movieId = 1
        val rate = 5
        coEvery { movieDao.setUserRateToMovie(movieId, rate) } just Runs

        // Act
        val result = repository.setUserRateToMovie(movieId, rate)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `getMoviesWithUserRate should return movies with rates`() = runBlocking {
        // Arrange
        val movieWithRate = MovieWithRating(
            movie = MovieEntity(id = 1, name = "Movie with rate"),
            userRating = 5
        )
        val expectedMovie = Movie(id = 1, name = "Movie with rate", userRate = 5)

        every { movieDao.getMoviesWithUserRate() } returns flowOf(listOf(movieWithRate))
        coEvery { repository.parseMovieInfo(any()) } returns expectedMovie

        // Act
        val result = repository.getMoviesWithUserRate().toList().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(5, result[0].userRate)
    }
}