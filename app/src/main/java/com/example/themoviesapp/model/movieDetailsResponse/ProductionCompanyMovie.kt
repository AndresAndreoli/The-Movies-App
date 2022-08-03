package com.example.themoviesapp.model.movieDetailsResponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCompanyMovie(
    var id: Int?,
    var logo_path: String,
    var name: String,
    var origin_country: String
): Parcelable{
    constructor(): this(null, "", "", "")
}