package com.example.themoviesapp.model.movieResponse

import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.services.MoviesService
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesService: MoviesService,
    private val moviesCache: Cache
    ) {
    suspend fun getAllMovies(apiKey:String, page: Int): GenericResponse<List<Movie>>{

        return if (chacheIsEmpty() || page>1){
            var movies = moviesService.getMoviesResponse(apiKey, page)

            moviesCache.movies.addAll(movies.data.movies)
            GenericResponse(
                movies.success,
                movies.data.movies
            )
        } else {
            GenericResponse(
                true,
                getMoviesFromCache()
            )
        }
    }

    fun getMoviesFromCache(): List<Movie>{
        return moviesCache.movies
    }

    fun chacheIsEmpty(): Boolean{
        return moviesCache.movies.isEmpty()
    }

    fun clearCache(): Boolean{
        moviesCache.movies.clear()
        return moviesCache.movies.isEmpty()
    }
}