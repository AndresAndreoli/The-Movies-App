package com.example.themoviesapp.domain

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.movieDetailsResponse.MovieDetailsRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(idMovie: Int): MovieDetailsResponse{
        return movieDetailsRepository.getMovieDetails(idMovie)
    }
}