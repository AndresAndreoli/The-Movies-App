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
        var movies: List<Movie>
        if (moviesCache.movies.isEmpty() && page==1) {
            if (moviesService.getMoviesResponse(apiKey, page).movies.size == 1 && moviesService.getMoviesResponse(apiKey, page).movies[0].id == null) {
                movies = emptyList()
            } else {
                movies = moviesService.getMoviesResponse(apiKey, page).movies
                moviesCache.movies.addAll(movies)
            }
        } else if (page>1){
            movies = moviesService.getMoviesResponse(apiKey, page).movies
            moviesCache.movies.addAll(movies)
        } else {
            movies = moviesCache.movies
        }
        return movies
    }
}