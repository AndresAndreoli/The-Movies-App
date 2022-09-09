package com.example.themoviesapp.domain.movieDetailsUseCase

import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MoviesRepository
import javax.inject.Inject

class HandleFavoriteMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend fun addNewFavoriteMovie (movie: MovieItem){
        repository.insertFavoriteMovieToDB(movie)
    }

    suspend fun deleteFavoriteMovie(idMovie: Int){
        repository.deleteFavoriteMovieFromDB(idMovie)
    }
}