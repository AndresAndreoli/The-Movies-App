package com.example.themoviesapp.model.movieDetailsResponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionMovie(
    var id: Int?,
    var name: String,
    var poster_path: String,
    var backdrop_path: String
) : Parcelable {
    constructor():this(null, "", "", "")
}
