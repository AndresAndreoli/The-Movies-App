package com.example.themoviesapp.services

import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.model.movieResponse.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesService @Inject constructor (
    private val apiService: APIService
    ) {
    suspend fun getMoviesResponse (apiKey: String, page: Int): GenericResponse<MoviesResponse>{
        return withContext(Dispatchers.IO){
            val request = apiService.getMovies(apiKey, page)
              if (request.isSuccessful){
                  GenericResponse<MoviesResponse>(
                      true,
                      request.body()!!
                  )
             } else {
                  // TODO: catch error
                  GenericResponse<MoviesResponse>(
                      false,
                      MoviesResponse()
                  )
             }
        }
    }
}


