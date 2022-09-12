package com.example.themoviesapp.domain.favoriteUseCase

import com.example.themoviesapp.data.repositories.FavoriteMoviesRepository
import com.example.themoviesapp.data.repositories.MoviesRepository
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    suspend operator fun invoke() = favoriteMoviesRepository.getFavoriteMoviesFromDB()
}