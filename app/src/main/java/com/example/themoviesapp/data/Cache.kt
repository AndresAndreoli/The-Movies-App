package com.example.themoviesapp.data

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.movieResponse.MovieModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache @Inject constructor(){
    var movie: MutableList<MovieModel> = mutableListOf()
    var movieDetailsList: MutableList<MovieDetailsResponse> = mutableListOf()
    var favoriteMovies = HashSet<Int>() // This will be load with firebase
}