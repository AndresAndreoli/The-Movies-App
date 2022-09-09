package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class FindMovieUseCase @Inject constructor(
    val moviesRepository: MoviesRepository
){
    operator fun invoke(query: String): List<MovieItem>{
        return moviesRepository.getMoviesFromCache()
            .filter {
                it.title.lowercase().contains(query.lowercase())
            }
    }
}