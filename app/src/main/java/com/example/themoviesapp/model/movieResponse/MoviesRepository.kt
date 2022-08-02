package com.example.themoviesapp.model.movieResponse

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.services.MoviesService
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesService: MoviesService
    ) {
    suspend fun getAllMovies(page: Int): List<Movie>{
        return if (moviesService.getMoviesResponse(page).movies.size==1 && moviesService.getMoviesResponse(page).movies[0].id == null){
            emptyList()
        } else {
            moviesService.getMoviesResponse(page).movies
        }
    }
}