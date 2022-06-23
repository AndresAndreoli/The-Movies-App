package com.example.themoviesapp

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

// Anotacion: decidi implementar 2 metodos para obtener datos de la API para poder experimentar
interface APIService {

    companion object{
        const val urlImage = "https://image.tmdb.org/t/p/w500"
        const val urlEndPoint = "https://api.themoviedb.org/3/movie/"
        const val APIkey = "208e554046f1cf82cd9a3dd3e315fe5f"
    }

    @GET
    suspend fun getMovies(@Url url: String): Response<MoviesResponse>

    @GET ("{idMovie}?api_key=208e554046f1cf82cd9a3dd3e315fe5f&language=en-US")
    suspend fun getDetailsMovie(@Path("idMovie") idMovie: String): Response<MovieDetailsResponse>
}
