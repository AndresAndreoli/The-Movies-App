package com.example.themoviesapp.model.movieResponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesResponse(
    var page: Int?,
    var total_page: Int?,
    var total_results: Int?,
    @SerializedName("results") var movieModels: List<MovieModel>
): Parcelable {
    constructor(): this (null, null, null, emptyList())
}

