package com.example.themoviesapp.data

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.movieResponse.MovieModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cache @Inject constructor(){
    // This is a volatile cache
    //var movie: MutableList<MovieModel> = mutableListOf()
    var movie = HashMap<Int,MovieModel>()
    var movieDetailsList: MutableList<MovieDetailsResponse> = mutableListOf()
    var favoriteMovies = HashMap<Int, String>() // This will be load with firebase
}