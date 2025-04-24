package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.country.CountryEntity
import com.example.data.local.entity.country.MovieCountryCrossRef

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


    @Query("DELETE FROM countryentity")
    suspend fun clearCountries()

    @Query("DELETE FROM moviecountrycrossref")
    suspend fun clearMovieCountryRelations()

    @Transaction
    suspend fun clearAll() {
        clearCountries()
        clearMovieCountryRelations()
    }

}