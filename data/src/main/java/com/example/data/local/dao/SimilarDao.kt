package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.similar.MovieSimilarCrossRef
import com.example.data.local.entity.similar.SimilarMovieEntity

@Dao
interface SimilarDao {
    @Query("""
        SELECT SimilarMovieEntity.*
        FROM SimilarMovieEntity
        JOIN MovieSimilarCrossRef ON SimilarMovieEntity.id = MovieSimilarCrossRef.similarId
        WHERE MovieSimilarCrossRef.movieId = :movieId
    """)
    suspend fun getSimilarsForMovie(movieId: Int): List<SimilarMovieEntity>

    @Insert
    suspend fun addSimilar(similarMovieEntity: SimilarMovieEntity)

    @Insert
    suspend fun addSimilarToMovie(movieSimilarCrossRef: MovieSimilarCrossRef)

    @Query("DELETE FROM similarmovieentity")
    suspend fun clearSimilars()

    @Query("DELETE FROM moviesimilarcrossref")
    suspend fun clearMovieSimilarsRelation()

    @Transaction
    suspend fun clearAll(){
        clearSimilars()
        clearMovieSimilarsRelation()
    }
}