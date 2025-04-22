package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.genre.GenreEntity
import com.example.data.local.entity.genre.MovieGenreCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("""
        SELECT GenreEntity.*
        FROM GenreEntity
        INNER JOIN MovieGenreCrossRef ON GenreEntity.id = MovieGenreCrossRef.genreId
        WHERE movieId = :movieId
    """)
    suspend fun getGenresForMovie(movieId: Int): List<GenreEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGenre(genreEntity: GenreEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGenreToMovie(movieGenreCrossRef: MovieGenreCrossRef)

}