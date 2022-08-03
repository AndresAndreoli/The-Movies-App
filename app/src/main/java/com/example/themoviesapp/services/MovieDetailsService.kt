package com.example.themoviesapp.services

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.movieResponse.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailsService @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getMovieDetailsResponse(idMovie: Int): MovieDetailsResponse{
        return withContext(Dispatchers.IO){
             if (apiService.getDetailsMovie(idMovie).isSuccessful){
                apiService.getDetailsMovie(idMovie).body() ?: MovieDetailsResponse()
            } else {
                // TODO: catch error
                MovieDetailsResponse()
            }
        }
    }
}