package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.data.repositories.MoviesRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(apiKey: String, page: Int) = moviesRepository.getAllPopularMovies(apiKey, page) // GenericResponse<List<MovieItem>>
}