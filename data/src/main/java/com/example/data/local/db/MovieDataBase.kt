package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.category.CategoryEntity
import com.example.data.local.dao.MovieDao
import com.example.data.local.entity.BackdropEntity
import com.example.data.local.entity.BudgetEntity
import com.example.data.local.entity.CountryEntity
import com.example.data.local.entity.FactEntity
import com.example.data.local.entity.FeesEntity
import com.example.data.local.entity.GenreEntity
import com.example.data.local.entity.LogoEntity
import com.example.data.local.entity.MovieEntity
import com.example.data.local.entity.PersonSimpleEntity
import com.example.data.local.entity.PosterEntity
import com.example.data.local.entity.RatingEntity
import com.example.data.local.entity.SeqAndPreqEntity
import com.example.data.local.entity.SimilarMovieEntity

@Database(
    entities = [
        CategoryEntity::class,
        BackdropEntity::class,
        BudgetEntity::class,
        CountryEntity::class,
        FactEntity::class,
        FeesEntity::class,
        GenreEntity::class,
        LogoEntity::class,
        MovieEntity::class,
        PersonSimpleEntity::class,
        PosterEntity::class,
        RatingEntity::class,
        SeqAndPreqEntity::class,
        SimilarMovieEntity::class,
    ],
    version = 1
)
abstract class MovieDataBase : RoomDatabase(){

    abstract fun movieDao() : MovieDao
}