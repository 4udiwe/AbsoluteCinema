package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.person.MoviePersonCrossRef
import com.example.data.local.entity.person.PersonSimpleEntity

@Dao
interface PersonDao {

    @Query("""
        SELECT PersonSimpleEntity.*
        FROM PersonSimpleEntity
        JOIN MoviePersonCrossRef ON id = personId
        WHERE movieId = :movieId
    """)
    suspend fun getPersonsForMovie(movieId: Int): List<PersonSimpleEntity>

    @Insert
    suspend fun addPerson(personSimpleEntity: PersonSimpleEntity)

    @Insert
    suspend fun addPersonToMovie(moviePersonCrossRef: MoviePersonCrossRef)

    @Query("DELETE FROM personsimpleentity")
    suspend fun clearPersons()

    @Query("DELETE FROM moviepersoncrossref")
    suspend fun clearMoviePersonRelations()

    @Transaction
    suspend fun clearAll() {
        clearPersons()
        clearMoviePersonRelations()
    }
}