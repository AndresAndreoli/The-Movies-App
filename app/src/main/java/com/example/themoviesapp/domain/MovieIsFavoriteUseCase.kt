package com.example.themoviesapp.domain

import com.example.themoviesapp.data.Cache
import javax.inject.Inject

class MovieIsFavoriteUseCase @Inject constructor(
    private val movieCache: Cache
) {
    operator fun invoke(idMovie: Int): Boolean{
        return movieCache.favoriteMovies.containsKey(idMovie)
    }
}