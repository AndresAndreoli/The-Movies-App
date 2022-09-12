package com.example.themoviesapp.data.repositories

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.data.services.MovieDetailsService
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(
    private val movieDetailsService: MovieDetailsService,
    private val movieDetailsCache: Cache
) {
    suspend fun getMovieDetails(idMovie: Int): GenericResponse<MovieDetailsResponse> {
        // Checkeo si dicha pelicula se encuentra ya en cache
        val movieFound = (movieDetailsCache.movieDetailsList.find { it.id == idMovie })

        return if (movieFound == null){
            val requestDetailMovie = movieDetailsService.getMovieDetailsResponse(idMovie)

            if (requestDetailMovie.success){ // Almaceno en cache
                movieDetailsCache.movieDetailsList.add(requestDetailMovie.data)
            }

            requestDetailMovie
        } else {
            GenericResponse(
                true,
                movieFound
            )
        }
    }
}