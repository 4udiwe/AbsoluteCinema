package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.MovieEntity
import com.example.domain.model.LocalCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun deleteAll(movies: List<MovieEntity>)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("SELECT * FROM movieentity")
    fun getAll(): Flow<List<MovieEntity>>

    @Query("SELECT userRate FROM UserRatingEntity WHERE movieId = :movieId LIMIT 1")
    suspend fun getMovieUserRate(movieId: Int): Int?

    @Query(
        """
        SELECT MovieEntity.*, UserRatingEntity.userRate
        FROM MovieEntity
        INNER JOIN UserRatingEntity ON MovieEntity.id = UserRatingEntity.movieId
        """
    )
    fun getMoviesWithUserRate(): Flow<List<MovieWithRating>>

    @Query(
        """
        SELECT categoryentity.name
        FROM categoryEntity 
        INNER JOIN movie_category 
            ON categoryEntity.id = movie_category.categoryId 
        WHERE movie_category.movieId = :movieId
        """
    )
    suspend fun getCategoriesForMovie(movieId: Int): List<LocalCategory>

    @Query(
        """
        SELECT MovieEntity.* 
        FROM MovieEntity
        INNER JOIN movie_category ON MovieEntity.id = movie_category.movieId
        INNER JOIN CategoryEntity ON movie_category.categoryId = CategoryEntity.id
        WHERE CategoryEntity.name = :category
        """
    )
    fun getMoviesOfCategory(category: String): Flow<List<MovieEntity>>

    @Query(
        """
        INSERT INTO movie_category (movieId, categoryId)
        SELECT :movieId, id 
        FROM CategoryEntity 
        WHERE name = :category
    """
    )
    suspend fun addMovieToCategory(movieId: Int, category: String)

    @Query(
        """
        DELETE FROM movie_category
        WHERE movieId = :movieId 
        AND categoryId IN (
            SELECT id 
            FROM CategoryEntity 
            WHERE name = :category
        )
    """
    )
    suspend fun removeMovieFromCategory(movieId: Int, category: String)

    @Query(
        """
        INSERT OR REPLACE INTO userratingentity (movieId, userRate)
        VALUES (:movieId, :rating)
    """
    )
    suspend fun setUserRateToMovie(movieId: Int, rating: Int)

    @Query(
        """
        DELETE FROM userratingentity
        WHERE movieId = :movieId
    """
    )
    suspend fun removeUserRateFromMovie(movieId: Int)
}