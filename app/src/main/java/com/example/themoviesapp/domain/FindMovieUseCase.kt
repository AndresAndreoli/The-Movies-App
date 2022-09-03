package com.example.themoviesapp.domain

import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class FindMovieUseCase @Inject constructor(
    val moviesRepository: MoviesRepository
){
    operator fun invoke(query: String): List<MovieModel>{
        return moviesRepository.getMoviesFromCache()
            .filter {
                it.title.lowercase().contains(query.lowercase())
            }
    }
}