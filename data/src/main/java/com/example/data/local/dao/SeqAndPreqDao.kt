package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.seqandpreq.MovieSequelCrossRef
import com.example.data.local.entity.seqandpreq.SeqAndPreqEntity

@Dao
interface SeqAndPreqDao {
    @Query("""
        SELECT SeqAndPreqEntity.*
        FROM SeqAndPreqEntity
        JOIN MovieSequelCrossRef ON SeqAndPreqEntity.id = MovieSequelCrossRef.sequelId
        WHERE MovieSequelCrossRef.movieId = :movieId
    """)
    suspend fun getSequelsForMovie(movieId: Int): List<SeqAndPreqEntity>

    @Insert
    suspend fun addSequel(seqAndPreqEntity: SeqAndPreqEntity)

    @Insert
    suspend fun addSequelToMovie(movieSequelCrossRef: MovieSequelCrossRef)

    @Query("DELETE FROM seqandpreqentity")
    suspend fun clearSequels()

    @Query("DELETE FROM moviesequelcrossref")
    suspend fun clearMovieSequelsRelation()

    @Transaction
    suspend fun clearAll(){
        clearSequels()
        clearMovieSequelsRelation()
    }
}