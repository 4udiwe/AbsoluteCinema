package com.example.data.mapper

import com.example.data.local.entity.MovieEntity
import com.example.domain.model.Backdrop
import com.example.domain.model.Budget
import com.example.domain.model.Country
import com.example.domain.model.Fact
import com.example.domain.model.Genre
import com.example.domain.model.Logo
import com.example.domain.model.Movie
import com.example.domain.model.Person
import com.example.domain.model.Poster
import com.example.domain.model.Rating
import com.example.domain.model.SeqAndPreq
import com.example.domain.model.SimilarMovie

object EntityToDomain : MovieMapper<MovieEntity, Movie> {
    override fun map(movie: MovieEntity): Movie =
        Movie(
            id = movie.id,
            name = movie.name,
            alternativeName = movie.alternativeName,
            enName = movie.enName,
            userRate = movie.userRate,
            /**
             *  Запрос о категории фильма происходит в репозитории.
             *  Поэтому в маппере она остается [null]
             */
            category = null,
            type = movie.type,
            typeNumber = movie.typeNumber,
            year = movie.year,
            description = movie.description,
            shortDescription = movie.shortDescription,
            slogan = movie.slogan,
            status = movie.status,
            facts = movie.facts.map { factEntity ->
                Fact(
                    id = factEntity.id,
                    fact = factEntity.fact,
                    type = factEntity.type,
                    spoiler = factEntity.spoiler,
                    movieId = factEntity.id
                )
            },
            rating = Rating(
                kp = movie.rating?.kp,
                imdb = movie.rating?.imdb,
                tmdb = movie.rating?.tmdb,
                filmCritics = movie.rating?.russianFilmCritics,
                russianFilmCritics = movie.rating?.russianFilmCritics,
                await = movie.rating?.await
            ),
            movieLength = movie.movieLength,
            ageRating = movie.ageRating,
            logo = Logo(
                logoUrl = movie.logo?.logoUrl
            ),
            poster = Poster(
                posterUrl = movie.poster?.posterUrl,
                posterPreviewUrl = movie.poster?.posterPreviewUrl
            ),
            backdrop = Backdrop(
                backdropUrl = movie.backdrop.backdropUrl,
                backdropPreviewUrl = movie.backdrop.backdropPreviewUrl
            ),
            genres = movie.genres.map { genreEntity ->
                Genre(
                    id = genreEntity.id,
                    name = genreEntity.name
                )
            },
            countries = movie.countries.map { countryEntity ->
                Country(
                    id = countryEntity.id,
                    name = countryEntity.name
                )
            },
            persons = movie.persons.map { personSimpleEntity ->
                Person(
                    id = personSimpleEntity.id,
                    photo = personSimpleEntity.photo,
                    name = personSimpleEntity.name,
                    enName = personSimpleEntity.enName,
                    description = personSimpleEntity.description,
                    profession = personSimpleEntity.enProfession,
                    enProfession = personSimpleEntity.enProfession
                )
            },
            budget = Budget(
                budgetValue = movie.budget?.budgetValue,
                budgetCurrency = movie.budget?.budgetCurrency
            ),
            similarMovies = movie.similarMovies.map { sim ->
                SimilarMovie(
                    id = sim.id,
                    name = sim.name,
                    enName = sim.enName,
                    alternativeName = sim.alternativeName,
                    type = sim.type,
                    poster = Poster(
                        posterUrl = sim.poster?.posterUrl,
                        posterPreviewUrl = sim.poster?.posterPreviewUrl
                    ),
                    rating = Rating(
                        kp = sim.rating?.kp,
                        imdb = sim.rating?.imdb,
                        tmdb = sim.rating?.tmdb,
                        filmCritics = sim.rating?.russianFilmCritics,
                        russianFilmCritics = sim.rating?.russianFilmCritics,
                        await = sim.rating?.await
                    ),
                    year = sim.year,
                    movieId = sim.movieId
                )
            },
            sequelsAndPrequels = movie.sequelsAndPrequels.map { seq ->
                SeqAndPreq(
                    id = seq.id,
                    name = seq.name,
                    enName = seq.enName,
                    alternativeName = seq.alternativeName,
                    type = seq.type,
                    poster = Poster(
                        posterUrl = seq.poster?.posterUrl,
                        posterPreviewUrl = seq.poster?.posterPreviewUrl
                    ),
                    rating = Rating(
                        kp = seq.rating?.kp,
                        imdb = seq.rating?.imdb,
                        tmdb = seq.rating?.tmdb,
                        filmCritics = seq.rating?.russianFilmCritics,
                        russianFilmCritics = seq.rating?.russianFilmCritics,
                        await = seq.rating?.await
                    ),
                    year = seq.year,
                    movieId = seq.movieId
                )
            },
            top10 = movie.top10,
            top250 = movie.top250,
            totalSeriesLength = movie.totalSeriesLength,
            seriesLength = movie.seriesLength,
            isSeries = movie.isSeries
        )
}