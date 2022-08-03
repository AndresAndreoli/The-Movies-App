package com.example.themoviesapp.model

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.movieResponse.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache @Inject constructor(){
    var movies: MutableList<Movie> = mutableListOf()
    var movieDetailsList: MutableList<MovieDetailsResponse> = mutableListOf()
}