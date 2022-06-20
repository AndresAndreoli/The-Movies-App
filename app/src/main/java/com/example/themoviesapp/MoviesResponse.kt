package com.example.themoviesapp

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    var page: Int,
    var total_page: Int,
    var total_results: Int,
    @SerializedName("results") var movies: List<Movie>
)

