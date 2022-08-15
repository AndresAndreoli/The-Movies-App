package com.example.themoviesapp.services

import com.example.themoviesapp.model.movieResponse.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesService @Inject constructor (
    private val apiService: APIService
    ) {
    suspend fun getMoviesResponse (apiKey: String, page: Int): MoviesResponse{
        return withContext(Dispatchers.IO){
              if (apiService.getMovies(apiKey, page).isSuccessful){
                  apiService.getMovies(apiKey, page).body() ?: MoviesResponse()
             } else {
                  // TODO: catch error
                 MoviesResponse()
             }
        }
    }
}


