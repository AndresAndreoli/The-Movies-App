package com.example.themoviesapp.model.movieDetailsResponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpokenlanguageMovie(
    var english_name: String,
    var iso_639_1: String,
    var name: String
): Parcelable{
    constructor(): this("", "", "")
}
