package com.example.themoviesapp.domain

import com.example.themoviesapp.model.movieDetailsResponse.MovieDetailsRepository
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class FindMovieUseCase @Inject constructor(
    val moviesRepository: MoviesRepository
){
    operator fun invoke(query: String): List<Movie>{
        return moviesRepository.getMoviesFromCache()
            .filter {
                it.title.lowercase().contains(query.lowercase())
            }
    }
}