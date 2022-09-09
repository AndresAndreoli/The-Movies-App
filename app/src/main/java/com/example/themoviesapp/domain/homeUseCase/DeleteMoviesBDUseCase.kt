package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.data.repositories.FavoriteMoviesRepository
import javax.inject.Inject

class DeleteMoviesBDUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    suspend operator fun invoke(){
        favoriteMoviesRepository.clearDB()
    }
}