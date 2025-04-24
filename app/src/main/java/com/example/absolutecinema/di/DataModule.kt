package com.example.absolutecinema.di

import androidx.room.Room
import com.example.data.local.converter.LocalCategoryConverter
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.CountryDao
import com.example.data.local.dao.FactDao
import com.example.data.local.dao.GenreDao
import com.example.data.local.dao.MovieDao
import com.example.data.local.dao.PersonDao
import com.example.data.local.dao.SeqAndPreqDao
import com.example.data.local.dao.SimilarDao
import com.example.data.local.db.MovieDataBase
import com.example.data.remote.api.MoviesAPI
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<MovieRepository> {
        MovieRepositoryImpl(
            api = get(),
            movieDao = get(),
            categoryDao = get(),
            countryDao = get(),
            factDao = get(),
            genreDao = get(),
            personDao = get(),
            seqAndPreqDao = get(),
            similarDao = get(),
            logger = get()
        )
    }

    single<MoviesAPI> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesAPI::class.java)
    }

    single<MovieDataBase> {
        Room.databaseBuilder(
            context = get(),
            klass = MovieDataBase::class.java,
            "movies.db"
        )
            .createFromAsset("db/movies.db")
            .build()
    }

    single<MovieDao> {
        val db = get<MovieDataBase>()
        db.movieDao()
    }
    single<CountryDao> {
        val db = get<MovieDataBase>()
        db.countryDao()
    }
    single<CategoryDao> {
        val db = get<MovieDataBase>()
        db.categoryDao()
    }
    single<FactDao> {
        val db = get<MovieDataBase>()
        db.factDao()
    }
    single<GenreDao> {
        val db = get<MovieDataBase>()
        db.genreDao()
    }
    single<PersonDao> {
        val db = get<MovieDataBase>()
        db.personDao()
    }
    single<SeqAndPreqDao> {
        val db = get<MovieDataBase>()
        db.seqAndPreqDao()
    }
    single<SimilarDao> {
        val db = get<MovieDataBase>()
        db.similarDao()
    }
}