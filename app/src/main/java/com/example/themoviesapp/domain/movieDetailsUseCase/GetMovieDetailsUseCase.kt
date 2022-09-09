package com.example.themoviesapp.domain.movieDetailsUseCase

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.model.movieDetailsResponse.MovieDetailsRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(idMovie: Int): GenericResponse<MovieDetailsResponse>{
        return movieDetailsRepository.getMovieDetails(idMovie)
    }
}