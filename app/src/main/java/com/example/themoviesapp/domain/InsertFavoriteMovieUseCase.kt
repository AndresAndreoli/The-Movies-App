package com.example.themoviesapp.domain

import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class InsertFavoriteMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movie: MovieItem){
        repository.insertFavoriteMovieToDB(movie)
    }
}