package com.example.themoviesapp.model.movieDetailsResponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreMovie(
    var id: Int?,
    var name: String
): Parcelable{
    constructor(): this(null, "")
}