package com.example.data

import com.example.data.local.dao.MovieDao
import com.example.data.mapper.DtoToEntity
import com.example.data.mapper.EntityToDomain
import com.example.data.remote.api.MoviesAPI
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.model.LocalCategory
import com.example.domain.model.Movie
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MovieRepositoryUnitTest {

    @MockK
    private lateinit var mockApi: MoviesAPI

    @MockK
    private lateinit var mockDao: MovieDao

    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = MovieRepositoryImpl(mockApi, mockDao)
    }

    @Test
    fun `getMovieById should return movie with parsed info`() = runBlocking {
        // Arrange
        val movieId = 1
        val mockMovieDto = mockk<com.example.data.remote.dto.common.MovieDto>()
        val mockMovieEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()

        coEvery { mockApi.getMovieById(movieId) } returns mockMovieDto
        every { DtoToEntity.map(mockMovieDto) } returns mockMovieEntity
        every { EntityToDomain.map(mockMovieEntity) } returns mockDomainMovie
        coEvery { mockDao.insert(mockMovieEntity) } just Runs
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getMovieById(movieId)

        // Assert
        assertEquals(mockDomainMovie, result)
        coVerify { mockApi.getMovieById(movieId) }
        coVerify { mockDao.insert(mockMovieEntity) }
    }

    @Test
    fun `searchMoviesByName should return mapped response with parsed info`() = runBlocking {
        // Arrange
        val query = "test"
        val mockResponseDto = mockk<com.example.data.remote.dto.responce.MoviesResponseDto>()
        val mockMovieDto = mockk<com.example.data.remote.dto.common.MovieDto>()
        val mockMovieEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()

        every { mockResponseDto.movieDtos } returns listOf(mockMovieDto)
        every { mockResponseDto.total } returns 1
        every { mockResponseDto.limit } returns 10
        every { mockResponseDto.page } returns 1
        every { mockResponseDto.pages } returns 1
        coEvery { mockApi.searchMovieByName(query) } returns mockResponseDto
        every { DtoToEntity.map(mockMovieDto) } returns mockMovieEntity
        every { EntityToDomain.map(mockMovieEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.searchMoviesByName(query)

        // Assert
        assertEquals(1, result.movies.size)
        assertEquals(1, result.total)
        coVerify { mockApi.searchMovieByName(query) }
    }

    @Test
    fun `searchMoviesWithFilters should return mapped response`() = runBlocking {
        // Arrange
        val mockResponseDto = mockk<com.example.data.remote.dto.responce.MoviesResponseDto>()
        val mockMovieDto = mockk<com.example.data.remote.dto.common.MovieDto>()
        val mockMovieEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()

        every { mockResponseDto.movieDtos } returns listOf(mockMovieDto)
        every { mockResponseDto.total } returns 1
        every { mockResponseDto.limit } returns 10
        every { mockResponseDto.page } returns 1
        every { mockResponseDto.pages } returns 1
        coEvery { mockApi.searchWithFilter(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns mockResponseDto
        every { DtoToEntity.map(mockMovieDto) } returns mockMovieEntity
        every { EntityToDomain.map(mockMovieEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Возможно неправильные параметры
        // Act
        val result = repository.searchMoviesWithFilters(null, null, null, null, null, null, null, null, null)

        // Assert
        assertEquals(1, result.movies.size)
        coVerify { mockApi.searchWithFilter(any(), any(), any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `getFavouriteMovies should return flow of favourite movies`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        every { mockDao.getMoviesOfCategory(LocalCategory.Favourite.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getFavouriteMovies().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `getMoviesWithUserRate should return flow of rated movies`() = runBlocking {
        // Arrange
        val mockEntityWithRating = mockk<com.example.data.local.dao.MovieWithRating>()
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val rating = 5

        every { mockEntityWithRating.movie } returns mockEntity
        every { mockEntityWithRating.userRating } returns rating
        every { mockDao.getMoviesWithUserRate() } returns flowOf(listOf(mockEntityWithRating))
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getMoviesWithUserRate().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(rating, result[0][0].userRate)
    }

    @Test
    fun `getWillWatchMovies should return flow of will watch movies`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        every { mockDao.getMoviesOfCategory(LocalCategory.WillWatch.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getWillWatchMovies().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `addMovieToFavourites should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { mockDao.addMovieToCategory(movieId, LocalCategory.Favourite.name) } just Runs

        // Act
        val result = repository.addMovieToFavourites(movieId)

        // Assert
        assertTrue(result)
        coVerify { mockDao.addMovieToCategory(movieId, LocalCategory.Favourite.name) }
    }

    @Test
    fun `removeMovieFromFavourites should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { mockDao.removeMovieFromCategory(movieId, LocalCategory.Favourite.name) } just Runs

        // Act
        val result = repository.removeMovieFromFavourites(movieId)

        // Assert
        assertTrue(result)
        coVerify { mockDao.removeMovieFromCategory(movieId, LocalCategory.Favourite.name) }
    }

    @Test
    fun `setUserRateToMovie should return true for valid rate`() = runBlocking {
        // Arrange
        val movieId = 1
        val rate = 5
        coEvery { mockDao.setUserRateToMovie(movieId, rate) } just Runs

        // Act
        val result = repository.setUserRateToMovie(movieId, rate)

        // Assert
        assertTrue(result)
        coVerify { mockDao.setUserRateToMovie(movieId, rate) }
    }

    @Test
    fun `setUserRateToMovie should return false for invalid rate`() = runBlocking {
        // Arrange
        val movieId = 1
        val rate = 11

        // Act
        val result = repository.setUserRateToMovie(movieId, rate)

        // Assert
        assertFalse(result)
        coVerify(exactly = 0) { mockDao.setUserRateToMovie(any(), any()) }
    }

    @Test
    fun `removeUserRateFromMovie should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { mockDao.removeUserRateFromMovie(movieId) } just Runs

        // Act
        val result = repository.removeUserRateFromMovie(movieId)

        // Assert
        assertTrue(result)
        coVerify { mockDao.removeUserRateFromMovie(movieId) }
    }

    @Test
    fun `addMovieToWillWatch should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { mockDao.addMovieToCategory(movieId, LocalCategory.WillWatch.name) } just Runs

        // Act
        val result = repository.addMovieToWillWatch(movieId)

        // Assert
        assertTrue(result)
        coVerify { mockDao.addMovieToCategory(movieId, LocalCategory.WillWatch.name) }
    }

    @Test
    fun `removeMovieFromWillWatch should return true on success`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { mockDao.removeMovieFromCategory(movieId, LocalCategory.WillWatch.name) } just Runs

        // Act
        val result = repository.removeMovieFromWillWatch(movieId)

        // Assert
        assertTrue(result)
        coVerify { mockDao.removeMovieFromCategory(movieId, LocalCategory.WillWatch.name) }
    }

    @Test
    fun `getRecommendedFilms should return flow of recommended films`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        coEvery { mockApi.searchWithFilter(any(), any(), any()) } returns mockk {
            every { movieDtos } returns emptyList()
        }
        every { mockDao.getMoviesOfCategory(LocalCategory.RecomendedFilms.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getRecommendedFilms().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `getRecommendedSeries should return flow of recommended series`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        coEvery { mockApi.searchWithFilter(any(), any(), any()) } returns mockk {
            every { movieDtos } returns emptyList()
        }
        every { mockDao.getMoviesOfCategory(LocalCategory.RecomendedSeries.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getRecommendedSeries().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `getDetectiveMovies should return flow of detective movies`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        coEvery { mockApi.searchWithFilter(any(), any(), any()) } returns mockk {
            every { movieDtos } returns emptyList()
        }
        every { mockDao.getMoviesOfCategory(LocalCategory.Detectives.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getDetectiveMovies().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `getRomanMovies should return flow of roman movies`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        coEvery { mockApi.searchWithFilter(any(), any(), any()) } returns mockk {
            every { movieDtos } returns emptyList()
        }
        every { mockDao.getMoviesOfCategory(LocalCategory.Romans.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getRomanMovies().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `getComedyMovies should return flow of comedy movies`() = runBlocking {
        // Arrange
        val mockEntity = mockk<com.example.data.local.entity.MovieEntity>()
        val mockDomainMovie = mockk<Movie>()
        val entities = listOf(mockEntity)

        coEvery { mockApi.searchWithFilter(any(), any(), any()) } returns mockk {
            every { movieDtos } returns emptyList()
        }
        every { mockDao.getMoviesOfCategory(LocalCategory.Comedys.name) } returns flowOf(entities)
        every { EntityToDomain.map(mockEntity) } returns mockDomainMovie
        coEvery { repository.parseMovieInfo(mockDomainMovie) } returns mockDomainMovie

        // Act
        val result = repository.getComedyMovies().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals(mockDomainMovie, result[0][0])
    }

    @Test
    fun `parseMovieInfo should set correct properties`() = runBlocking {
        // Arrange
        val movieId = 1
        val movie = Movie(id = movieId)
        val categories = listOf(LocalCategory.Favourite, LocalCategory.WillWatch)
        val userRate = 5

        coEvery { mockDao.getCategoriesForMovie(movieId) } returns categories
        coEvery { mockDao.getMovieUserRate(movieId) } returns userRate

        // Act
        val result = repository.parseMovieInfo(movie)

        // Assert
        assertTrue(result.isFavorite)
        assertTrue(result.isWillWatch)
        assertEquals(userRate, result.userRate)
    }
}