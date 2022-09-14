package com.example.themoviesapp.domain.homeUseCase

import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.data.repositories.MoviesRepository
import javax.inject.Inject

class ClearingCacheUseCase @Inject constructor(
    private val moviesCache: Cache,
    private val moviesRepository: MoviesRepository
){
    operator fun invoke(): Boolean{
        moviesCache.popularMovie.clear()
        moviesCache.topRatedMovie.clear()
        moviesCache.upcomingMovie.clear()

        return moviesRepository.chacheIsEmpty()
    }
}