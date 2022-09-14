package com.example.themoviesapp.data.services

import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.model.movieResponse.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopRatedMovieService @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getTopRatedMoviesResponse(apiKey: String, page: Int): GenericResponse<MoviesResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getTopRatedMovies(apiKey, page)
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
