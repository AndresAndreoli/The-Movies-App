package com.example.themoviesapp

import com.example.themoviesapp.model.movieDetailsResponse.*

data class MovieDetailsResponse(
    var adult: Boolean,
    var backdrop_path: String,
    var belongs_to_collection: CollectionMovie,
    var budget: Int,
    var genres: List<GenreMovie>,
    var homepage: String,
    var id: Int,
    var imdb_id: String,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Float,
    var poster_path: String,
    var production_companies: List<ProductionCompanyMovie>,
    var production_countries: List<ProductionCountryMovie>,
    var release_date: String,
    var revenue: Int,
    var runtime: Int,
    var spoken_language: List<SpokenlanguageMovie>,
    var status: String,
    var tagline: String,
    var title: String,
    var video: Boolean,
    var vote_average: Float,
    var vote_count: Int
)

