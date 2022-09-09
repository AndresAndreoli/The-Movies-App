package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class RetrieveMoviesFromCacheUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): List<MovieItem>{
        return repository.getMoviesFromCache()
    }
}