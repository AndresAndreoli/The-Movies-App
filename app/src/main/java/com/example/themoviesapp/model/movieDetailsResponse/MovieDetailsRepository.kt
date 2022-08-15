package com.example.themoviesapp.model.movieDetailsResponse

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.services.MovieDetailsService
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(
    private val movieDetailsService: MovieDetailsService,
    private val movieDetailsCache: Cache
) {
    var movieDetails: MovieDetailsResponse = MovieDetailsResponse()
    suspend fun getMovieDetails(idMovie: Int): MovieDetailsResponse{

        // Checkeao si dicha pelicula se encuentra ya en cache
        val movieFound = (movieDetailsCache.movieDetailsList.find { it.id==idMovie })

        if (movieFound == null){
            movieDetails = movieDetailsService.getMovieDetailsResponse(idMovie)
            movieDetailsCache.movieDetailsList.add(movieDetails)
        } else {
            movieDetails = movieFound
        }
        return movieDetails
    }
}