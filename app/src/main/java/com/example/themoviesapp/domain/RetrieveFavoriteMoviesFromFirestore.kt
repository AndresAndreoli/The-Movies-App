package com.example.themoviesapp.domain

import com.example.themoviesapp.data.repositories.FavoriteMoviesRepository
import javax.inject.Inject

class RetrieveFavoriteMoviesFromFirestore @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    // This function retrieve favorite movies ids from firestore when the app is opened
    suspend operator fun invoke(){
        favoriteMoviesRepository.retrieveIDFavoriteMoviesFromFirebase()
    }
}