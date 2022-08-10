package com.example.themoviesapp.domain

import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class ClearingCacheUseCase @Inject constructor(
    private val repository: MoviesRepository
){
    operator fun invoke(): Boolean{
        return repository.clearCache()
    }
}