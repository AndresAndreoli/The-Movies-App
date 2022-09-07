package com.example.themoviesapp.domain

import com.example.themoviesapp.data.repositories.FavoriteMoviesRepository
import javax.inject.Inject

class RetrieveIDFavMoviesFirebaseUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    suspend operator fun invoke(){
        favoriteMoviesRepository.retrieveIDFavoriteMoviesFromFirebase()
    }
}