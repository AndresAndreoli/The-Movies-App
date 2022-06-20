package com.example.themoviesapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

// Esta interface me permite crear el metodo por el cual vamos a acceder al API
interface APIService {
    @GET
    suspend fun getMovies(@Url url: String): Response<MoviesResponse>

    @GET
    suspend fun getDetailsMovie(@Url url: String): Response<MovieDetailsResponse>
}