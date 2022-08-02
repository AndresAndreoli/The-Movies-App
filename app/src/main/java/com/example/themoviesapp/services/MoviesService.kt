package com.example.themoviesapp.services

import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.model.movieResponse.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class MoviesService @Inject constructor (
    private val apiService: APIService
    ) {
    suspend fun getMoviesResponse (page: Int): MoviesResponse{
        return withContext(Dispatchers.IO){
              if (apiService.getMovies(page).isSuccessful){
                  apiService.getMovies(page).body() ?: MoviesResponse(null, null, null, listOf(Movie(false, "",listOf(), null, "", "", "", null, "", "", "", false, null, null)))
             } else {
                 MoviesResponse(null, null, null, listOf(Movie(false, "",listOf(), null, "", "", "", null, "", "", "", false, null, null)))
             }
        }
    }
}


