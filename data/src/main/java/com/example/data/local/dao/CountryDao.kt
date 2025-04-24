package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.country.CountryEntity
import com.example.data.local.entity.country.MovieCountryCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query(""" 
        SELECT CountryEntity.*
        FROM CountryEntity
        INNER JOIN MovieCountryCrossRef ON id = countryId
        WHERE movieId = :movieId
        """
    )
    suspend fun getCountryForMovie(movieId: Int) : List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCountry(countryEntity: CountryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCountryToMovie(countryCrossRef: MovieCountryCrossRef)

}