package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.data.Cache
import javax.inject.Inject

class ClearingCacheUseCase @Inject constructor(
    private val moviesCache: Cache
){
    operator fun invoke(): Boolean{
        return moviesCache.movie.isEmpty()
    }
}