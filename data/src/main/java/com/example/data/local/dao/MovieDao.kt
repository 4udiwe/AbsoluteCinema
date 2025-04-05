package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.MovieEntity
import com.example.data.local.entity.userrating.MovieWithRating
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

    @Query("DELETE FROM movieentity")
    suspend fun clearAll()

    @Query("SELECT * FROM movieentity")
    fun getAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movieentity WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int) : MovieEntity?

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