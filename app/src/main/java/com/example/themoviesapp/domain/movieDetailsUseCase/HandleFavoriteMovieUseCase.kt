package com.example.themoviesapp.domain.movieDetailsUseCase

import com.example.themoviesapp.data.repositories.FavoriteMoviesRepository
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.data.repositories.MoviesRepository
import javax.inject.Inject

class HandleFavoriteMovieUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    suspend fun addNewFavoriteMovie (movie: MovieItem){
        favoriteMoviesRepository.insertFavoriteMovieToDB(movie)
    }

    suspend fun deleteFavoriteMovie(idMovie: Int){
        favoriteMoviesRepository.deleteFavoriteMovieFromDB(idMovie)
    }
}