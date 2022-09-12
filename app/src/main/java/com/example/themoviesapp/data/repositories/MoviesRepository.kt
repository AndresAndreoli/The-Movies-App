package com.example.themoviesapp.data.repositories

import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.data.database.dao.MovieDao
import com.example.themoviesapp.model.entities.MovieEntity
import com.example.themoviesapp.data.services.MovieDetailsService
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.data.services.MoviesService
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.utils.toDataBase
import com.example.themoviesapp.utils.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesService: MoviesService,
    private val moviesCache: Cache
) {
    suspend fun getAllMovies(apiKey: String, page: Int): GenericResponse<List<MovieItem>> {
        return if (chacheIsEmpty() || page > 1) {
            var movies = moviesService.getMoviesResponse(apiKey, page)
            movies.data.movieModels.forEach {
                moviesCache.movie.put(it.id!!, it)
            }
            GenericResponse(
                movies.success,
                getMoviesFromCache()
            )
        } else {
            GenericResponse(
                true,
                getMoviesFromCache()
            )
        }
    }

    fun getMoviesFromCache(): List<MovieItem> {
        // mapeo el modelo de datos de ModelEntity a Model
        val response: List<MovieModel> = moviesCache.movie.values.toList()
        return response.map { it.toDomain() }
    }

    fun chacheIsEmpty(): Boolean {
        return moviesCache.movie.isEmpty()
    }
}