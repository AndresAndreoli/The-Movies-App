package com.example.themoviesapp.services

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.model.movieResponse.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailsService @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getMovieDetailsResponse(idMovie: Int): GenericResponse<MovieDetailsResponse> {
        return withContext(Dispatchers.IO){
            val request = apiService.getDetailsMovie(idMovie)
             if (request.isSuccessful){
                 GenericResponse(
                     true,
                     MovieDetailsResponse()
                 )
             } else {
                 GenericResponse(
                     false,
                     MovieDetailsResponse()
                 )
             }
        }
    }
}