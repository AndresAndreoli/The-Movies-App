package com.example.themoviesapp.domain

import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(page: Int): List<Movie> = moviesRepository.getAllMovies(page)
}