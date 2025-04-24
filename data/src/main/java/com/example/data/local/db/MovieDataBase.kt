package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.CountryDao
import com.example.data.local.dao.FactDao
import com.example.data.local.dao.GenreDao
import com.example.data.local.entity.category.CategoryEntity
import com.example.data.local.entity.category.MovieCategoryCrossRef
import com.example.data.local.dao.MovieDao
import com.example.data.local.dao.PersonDao
import com.example.data.local.dao.SeqAndPreqDao
import com.example.data.local.dao.SimilarDao
import com.example.data.local.entity.MovieEntity
import com.example.data.local.entity.country.CountryEntity
import com.example.data.local.entity.country.MovieCountryCrossRef
import com.example.data.local.entity.fact.FactEntity
import com.example.data.local.entity.genre.GenreEntity
import com.example.data.local.entity.genre.MovieGenreCrossRef
import com.example.data.local.entity.person.MoviePersonCrossRef
import com.example.data.local.entity.person.PersonSimpleEntity
import com.example.data.local.entity.seqandpreq.MovieSequelCrossRef
import com.example.data.local.entity.seqandpreq.SeqAndPreqEntity
import com.example.data.local.entity.similar.MovieSimilarCrossRef
import com.example.data.local.entity.similar.SimilarMovieEntity
import com.example.data.local.entity.userrating.UserRatingEntity

@Database(
    entities = [
        MovieEntity::class,
        CategoryEntity::class,
        MovieCategoryCrossRef::class,
        CountryEntity::class,
        MovieCountryCrossRef::class,
        FactEntity::class,
        GenreEntity::class,
        MovieGenreCrossRef::class,
        PersonSimpleEntity::class,
        MoviePersonCrossRef::class,
        SeqAndPreqEntity::class,
        MovieSequelCrossRef::class,
        SimilarMovieEntity::class,
        MovieSimilarCrossRef::class,
        UserRatingEntity::class,
    ],
    version = 1
)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun categoryDao(): CategoryDao
    abstract fun countryDao(): CountryDao
    abstract fun factDao(): FactDao
    abstract fun genreDao(): GenreDao
    abstract fun personDao(): PersonDao
    abstract fun seqAndPreqDao(): SeqAndPreqDao
    abstract fun similarDao(): SimilarDao
}