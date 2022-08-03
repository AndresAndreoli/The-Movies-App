package com.example.themoviesapp

import android.os.Parcelable
import com.example.themoviesapp.model.movieDetailsResponse.*
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class MovieDetailsResponse(
    var adult: Boolean,
    var backdrop_path: String,
    var belongs_to_collection: @RawValue CollectionMovie,
    var budget: Int?,
    var genres: @RawValue List<GenreMovie>,
    var homepage: String,
    var id: Int?,
    var imdb_id: String,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Float?,
    var poster_path: String,
    var production_companies: @RawValue List<ProductionCompanyMovie>,
    var production_countries: @RawValue List<ProductionCountryMovie>,
    var release_date: String,
    var revenue: Int?,
    var runtime: Int?,
    var spoken_language: @RawValue List<SpokenlanguageMovie>,
    var status: String,
    var tagline: String,
    var title: String,
    var video: Boolean,
    var vote_average: Float?,
    var vote_count: Int?
): Parcelable {
    constructor():this(false, "", CollectionMovie(), null, emptyList(), "", null, "", "", "", "", null, "", emptyList(), emptyList(), "", null, null, emptyList(), "", "", "", false, null, null)
}

