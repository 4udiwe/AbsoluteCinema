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
            type = movie.type,
            typeNumber = movie.typeNumber,
            year = movie.year,
            description = movie.description,
            shortDescription = movie.shortDescription,
            slogan = movie.slogan,
            status = movie.status,
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
            budget = Budget(
                budgetValue = movie.budget?.budgetValue,
                budgetCurrency = movie.budget?.budgetCurrency
            ),
            top10 = movie.top10,
            top250 = movie.top250,
            totalSeriesLength = movie.totalSeriesLength,
            seriesLength = movie.seriesLength,
            isSeries = movie.isSeries
        )
}