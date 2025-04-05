package com.example.data.mapper

import com.example.data.local.entity.Backdrop
import com.example.data.local.entity.Budget
import com.example.data.local.entity.Logo
import com.example.data.local.entity.MovieEntity
import com.example.data.local.entity.Poster
import com.example.data.local.entity.Rating
import com.example.data.remote.dto.common.MovieDto


object DtoToEntity : MovieMapper<MovieDto, MovieEntity> {
    override fun map(movie: MovieDto): MovieEntity =
        MovieEntity(
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
                filmCritics = movie.rating?.filmCritics,
                russianFilmCritics = movie.rating?.russianFilmCritics,
                await = movie.rating?.await
            ),
            movieLength = movie.movieLength,
            ageRating = movie.ageRating,
            logo = Logo(
                logoUrl = movie.logo?.url
            ),
            poster = Poster(
                posterUrl = movie.poster?.url,
                posterPreviewUrl = movie.poster?.previewUrl
            ),
            backdrop = Backdrop(
                backdropUrl = movie.backdrop?.url,
                backdropPreviewUrl = movie.backdrop?.previewUrl
            ),
            budget = Budget(
                budgetValue = movie.budget?.value,
                budgetCurrency = movie.budget?.currency
            ),
            top10 = movie.top10,
            top250 = movie.top250,
            totalSeriesLength = movie.totalSeriesLength,
            seriesLength = movie.seriesLength,
            isSeries = movie.isSeries
        )
}