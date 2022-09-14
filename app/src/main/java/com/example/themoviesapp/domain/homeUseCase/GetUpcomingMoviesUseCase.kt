package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.data.repositories.MoviesRepository
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(apiKey: String, page: Int) = moviesRepository.getAllUpcomingMovies(apiKey, page) // GenericResponse<List<MovieItem>>
}