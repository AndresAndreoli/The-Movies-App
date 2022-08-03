package com.example.themoviesapp.model.movieDetailsResponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCountryMovie(
    var iso_3166_1: String,
    var name: String
): Parcelable{
    constructor(): this("", "")
}
