package com.example.themoviesapp

data class MovieDetailsResponse(
    var adult: Boolean,
    var backdrop_path: String,
    var belongs_to_collection: CollectionType,
    var budget: Int,
    var genres: List<GenresType>,
    var homepage: String,
    var id: Int,
    var imdb_id: String,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Float,
    var poster_path: String,
    var production_companies: List<ProductionCompaniesType>,
    var production_countries: List<ProductionCountriesType>,
    var release_date: String,
    var revenue: Int,
    var runtime: Int,
    var spoken_language: List<SpokenLanguageType>,
    var status: String,
    var tagline: String,
    var title: String,
    var video: Boolean,
    var vote_average: Float,
    var vote_count: Int
)

// belongs_to_collection
data class CollectionType(
    var id: Int,
    var name: String,
    var poster_path: String,
    var backdrop_path: String
)

//genres
data class GenresType(
    var id: Int,
    var name: String
)

//production_companies
data class ProductionCompaniesType(
    var id: Int,
    var logo_path: String,
    var name: String,
    var origin_country: String
)

//production_countries
data class ProductionCountriesType(
    var iso_3166_1: String,
    var name: String
)

//spoken_language
data class SpokenLanguageType(
    var english_name: String,
    var iso_639_1: String,
    var name: String
)
