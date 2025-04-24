package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.entity.fact.FactEntity

@Dao
interface FactDao {
    @Query("""
        SELECT FactEntity.*
        FROM FactEntity
        WHERE movieId = :movieId
    """)
    suspend fun getFactsForMovie(movieId: Int) : List<FactEntity>

    @Insert
    suspend fun addFact(factEntity: FactEntity)

    @Query("""DELETE FROM factentity""")
    suspend fun clearAll()
}