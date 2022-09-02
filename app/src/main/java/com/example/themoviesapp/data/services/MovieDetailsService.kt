package com.example.themoviesapp.data.services

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.data.services.APIService
import com.example.themoviesapp.model.GenericResponse
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
                     request.body()!!
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