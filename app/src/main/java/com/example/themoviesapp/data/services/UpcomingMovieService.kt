package com.example.themoviesapp.data.services

import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.model.movieResponse.MoviesResponse
import com.google.android.gms.common.api.internal.ApiKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpcomingMovieService @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getUpcomingMoviesResponse(apiKey: String, page: Int): GenericResponse<MoviesResponse>{
        return withContext(Dispatchers.IO){
            val response = apiService.getUpcomingMovies(apiKey, page)
            if (response.isSuccessful){
                GenericResponse(
                    true,
                    response.body()!!
                )
            } else {
                // TODO: catch error
                GenericResponse(
                    false,
                    MoviesResponse()
                )
            }
        }
    }
}