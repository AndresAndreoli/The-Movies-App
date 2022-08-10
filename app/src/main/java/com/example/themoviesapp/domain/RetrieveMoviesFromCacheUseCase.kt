package com.example.themoviesapp.domain

import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class RetrieveMoviesFromCacheUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): List<Movie>{
        return repository.getMoviesFromCache()
    }
}