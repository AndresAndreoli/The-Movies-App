package com.example.themoviesapp.data.services

import com.example.themoviesapp.GuestSessionIdResponse
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.MovieRateResponse
import com.example.themoviesapp.RateObject
import com.example.themoviesapp.model.movieResponse.MoviesResponse
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    companion object{
        //Credentials
        const val urlImage = "https://image.tmdb.org/t/p/w500"
        const val urlEndPoint = "https://api.themoviedb.org/3/movie/"
        const val urlAuthentication = "https://api.themoviedb.org/3/authentication/"
        const val APIkey = "208e554046f1cf82cd9a3dd3e315fe5f"
        lateinit var guest_session_id : String
    }
    // https://developers.themoviedb.org/3/movies/get-popular-movies

    @Headers("Accept: application/json")
    @GET("popular?language=en-US")
    suspend fun getPopularMovies(@Query("api_key") ApiKey: String, @Query("page") pageNum: Int): Response<MoviesResponse>

    @Headers("Accept: application/json")
    @GET("top_rated?language=en-US")
    suspend fun getTopRatedMovies(@Query("api_key") ApiKey: String, @Query("page") pageNum: Int): Response<MoviesResponse>

    @Headers("Accept: application/json")
    @GET("upcoming?language=en-US")
    suspend fun getUpcomingMovies(@Query("api_key") ApiKey: String, @Query("page") pageNum: Int): Response<MoviesResponse>

    @Headers("Accept: application/json")
    @GET ("{idMovie}?api_key=208e554046f1cf82cd9a3dd3e315fe5f&language=en-US")
    suspend fun getDetailsMovie(@Path("idMovie") idMovie: Int): Response<MovieDetailsResponse>

    @GET("guest_session/new")
    suspend fun getGuestSessionID(@Query("api_key") apiKey: String): Response <GuestSessionIdResponse>

    @POST ("{movie_id}/rating")
    suspend fun postMovie(@Path("movie_id") movie_id: Int,@Query ("api_key") APIKey: String,@Query ("guest_session_id") guest_session_id: String, @Body example: RateObject): Response <MovieRateResponse>
}
