package com.example.themoviesapp.domain.favoriteUseCase

import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class LoadDBWithFavMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(){
        moviesRepository.loadDBWithFavoriteMovies()
    }
}