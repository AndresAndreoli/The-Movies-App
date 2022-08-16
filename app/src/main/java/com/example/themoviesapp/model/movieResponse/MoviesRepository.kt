package com.example.themoviesapp.model.movieResponse

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.services.MoviesService
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesService: MoviesService,
    private val moviesCache: Cache
    ) {
    suspend fun getAllMovies(apiKey:String, page: Int): List<Movie>{
            if (moviesCache.movies.isEmpty()) {
                var movies = moviesService.getMoviesResponse(apiKey, page)
                if (movies.success) {
                    moviesCache.movies.addAll(movies.data.movies)
                } else {
                    return emptyList()
                }
            } else if (page>1) {
                moviesCache.movies.addAll(moviesService.getMoviesResponse(apiKey, page).data.movies)
            }
        return moviesCache.movies
    }

    fun getMoviesFromCache(): List<Movie>{
        return moviesCache.movies
    }

    fun clearCache(): Boolean{
        moviesCache.movies.clear()
        return moviesCache.movies.isEmpty()
    }
}