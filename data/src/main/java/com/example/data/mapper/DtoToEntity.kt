package com.example.data.mapper

import com.example.data.local.entity.BackdropEntity
import com.example.data.local.entity.BudgetEntity
import com.example.data.local.entity.CountryEntity
import com.example.data.local.entity.FactEntity
import com.example.data.local.entity.GenreEntity
import com.example.data.local.entity.LogoEntity
import com.example.data.local.entity.MovieEntity
import com.example.data.local.entity.PersonSimpleEntity
import com.example.data.local.entity.PosterEntity
import com.example.data.local.entity.RatingEntity
import com.example.data.local.entity.SeqAndPreqEntity
import com.example.data.local.entity.SimilarMovieEntity
import com.example.data.remote.dto.common.MovieDto


object DtoToEntity : MovieMapper<MovieDto, MovieEntity> {
    override fun map(movie: MovieDto): MovieEntity =
        MovieEntity(
            id = movie.id,
            name = movie.name,
            alternativeName = movie.alternativeName,
            enName = movie.enName,
            userRate = null,
            type = movie.type,
            typeNumber = movie.typeNumber,
            year = movie.year,
            description = movie.description,
            shortDescription = movie.shortDescription,
            slogan = movie.slogan,
            status = movie.status,
            facts = movie.facts.map { fact -> FactEntity(
                id = null,
                fact = fact.value,
                type = fact.type,
                spoiler = fact.spoiler,
                movieId = movie.id
            ) },
            rating = RatingEntity(
                kp = movie.rating?.kp,
                imdb = movie.rating?.imdb,
                tmdb = movie.rating?.tmdb,
                filmCritics = movie.rating?.filmCritics,
                russianFilmCritics = movie.rating?.russianFilmCritics,
                await = movie.rating?.await
            ),
            movieLength = movie.movieLength,
            ageRating = movie.ageRating,
            logo = LogoEntity(
                logoUrl = movie.logo?.url
            ),
            poster = PosterEntity(
                posterUrl = movie.poster?.url,
                posterPreviewUrl = movie.poster?.previewUrl
            ),
            backdrop = BackdropEntity(
                backdropUrl = movie.backdrop?.url,
                backdropPreviewUrl = movie.backdrop?.previewUrl
            ),
            genres = movie.genres.map { genre ->
                GenreEntity(
                    id = null,
                    name = genre.name,
                    movieId = movie.id
                )
            },
            countries = movie.countries.map { country ->
                CountryEntity(
                    id = null,
                    name = country.name,
                    movieId = movie.id
                )
            },
            persons = movie.persons.map { person ->
                PersonSimpleEntity(
                    id = null,
                    photo = person.photo,
                    name = person.name,
                    enName = person.enName,
                    description = person.description,
                    profession = person.profession,
                    enProfession = person.enProfession,
                    movieId = movie.id
                )
            },
            budget = BudgetEntity(
                budgetId = null,
                budgetValue = movie.budget?.value,
                budgetCurrency = movie.budget?.currency
            ),
            similarMovies = movie.similarMovies.map { sim ->
                SimilarMovieEntity(
                    id = sim.id,
                    name = sim.name,
                    enName = sim.enName,
                    alternativeName = sim.alternativeName,
                    type = sim.type,
                    poster = PosterEntity(
                        posterUrl = sim.poster?.url,
                        posterPreviewUrl = sim.poster?.previewUrl
                    ),
                    rating = RatingEntity(
                        kp = sim.rating?.kp,
                        imdb = sim.rating?.imdb,
                        tmdb = sim.rating?.tmdb,
                        filmCritics = sim.rating?.filmCritics,
                        russianFilmCritics = sim.rating?.russianFilmCritics,
                        await = sim.rating?.await,
                    ),
                    year = sim.year,
                    movieId = movie.id
                )
            },
            sequelsAndPrequels = movie.sequelsAndPrequels.map { seq ->
                SeqAndPreqEntity(
                    id = seq.id,
                    name = seq.name,
                    enName = seq.enName,
                    alternativeName = seq.alternativeName,
                    type = seq.type,
                    poster = PosterEntity(
                        posterUrl = seq.poster?.url,
                        posterPreviewUrl = seq.poster?.previewUrl
                    ),
                    rating = RatingEntity(
                        kp = seq.rating?.kp,
                        imdb = seq.rating?.imdb,
                        tmdb = seq.rating?.tmdb,
                        filmCritics = seq.rating?.filmCritics,
                        russianFilmCritics = seq.rating?.russianFilmCritics,
                        await = seq.rating?.await,
                    ),
                    year = seq.year,
                    movieId = movie.id
                )
            },
            top10 = movie.top10,
            top250 = movie.top250,
            totalSeriesLength = movie.totalSeriesLength,
            seriesLength = movie.seriesLength,
            isSeries = movie.isSeries
        )
}