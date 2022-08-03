package com.example.themoviesapp.model

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.movieResponse.Movie
import javax.inject.Inject

class Cache @Inject constructor(){
    var movies: List<Movie> = emptyList()
    var movieDetailsList: MutableList<MovieDetailsResponse> = mutableListOf()
}