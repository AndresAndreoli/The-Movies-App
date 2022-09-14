package com.example.themoviesapp.data.repositories

import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.data.services.PopularMoviesService
import com.example.themoviesapp.data.services.TopRatedMovieService
import com.example.themoviesapp.data.services.UpcomingMovieService
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.utils.ValuesProvider
import com.example.themoviesapp.utils.toDomain
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val popularMoviesService: PopularMoviesService,
    private val topRatedMovieService: TopRatedMovieService,
    private val upcomingMovieService: UpcomingMovieService,
    private val moviesCache: Cache
) {
    suspend fun getAllPopularMovies(apiKey: String, page: Int): GenericResponse<List<MovieItem>> {
        return if (popularMovieCacheIsEmpty() || page > 1) {
            var movies = popularMoviesService.getPopularMoviesResponse(apiKey, page)
            movies.data.movieModels.forEach {
                moviesCache.popularMovie.put(it.id!!, it)
            }
            GenericResponse(
                movies.success,
                getMoviesFromCache(ValuesProvider.typeRequest.POPULAR)
            )
        } else {
            GenericResponse(
                true,
                getMoviesFromCache(ValuesProvider.typeRequest.POPULAR)
            )
        }
    }

    suspend fun getAllTopRatedMovies(apiKey: String, page: Int): GenericResponse<List<MovieItem>> {
        return if (topRatedMovieCacheIsEmpty() || page > 1) {
            var movies = topRatedMovieService.getTopRatedMoviesResponse(apiKey, page)
            movies.data.movieModels.forEach {
                moviesCache.topRatedMovie.put(it.id!!, it)
            }
            GenericResponse(
                movies.success,
                getMoviesFromCache(ValuesProvider.typeRequest.TOP_RATED)
            )
        } else {
            GenericResponse(
                true,
                getMoviesFromCache(ValuesProvider.typeRequest.TOP_RATED)
            )
        }
    }

    suspend fun getAllUpcomingMovies(apiKey: String, page: Int): GenericResponse<List<MovieItem>> {
        return if (upcomingMovieCacheIsEmpty() || page > 1) {
            var movies = upcomingMovieService.getUpcomingMoviesResponse(apiKey, page)
            movies.data.movieModels.forEach {
                moviesCache.upcomingMovie.put(it.id!!, it)
            }
            GenericResponse(
                movies.success,
                getMoviesFromCache(ValuesProvider.typeRequest.UPCOMING)
            )
        } else {
            GenericResponse(
                true,
                getMoviesFromCache(ValuesProvider.typeRequest.UPCOMING)
            )
        }
    }

    fun getMoviesFromCache(type: ValuesProvider.typeRequest): List<MovieItem> {
        var response: List<MovieModel>
        when (type){
            ValuesProvider.typeRequest.POPULAR -> response = moviesCache.popularMovie.values.toList()
            ValuesProvider.typeRequest.TOP_RATED -> response = moviesCache.topRatedMovie.values.toList()
            ValuesProvider.typeRequest.UPCOMING -> response = moviesCache.upcomingMovie.values.toList()
        }
        // mapeo el modelo de datos de movieModel a modelItem (domain layer)
        return response.map { it.toDomain() }
    }

    fun getAllMoviesFromCache(): List<MovieItem>{
        var allMovies = mutableListOf<MovieModel>()
        allMovies.clear()
        allMovies.addAll(moviesCache.popularMovie.values)
        allMovies.addAll(moviesCache.upcomingMovie.values)
        allMovies.addAll(moviesCache.topRatedMovie.values)

        return allMovies.map { it.toDomain() }
    }

    fun chacheIsEmpty(): Boolean {
        return popularMovieCacheIsEmpty()
                && topRatedMovieCacheIsEmpty()
                && upcomingMovieCacheIsEmpty()
    }

    private fun popularMovieCacheIsEmpty() = moviesCache.popularMovie.isEmpty()
    private fun topRatedMovieCacheIsEmpty() = moviesCache.topRatedMovie.isEmpty()
    private fun upcomingMovieCacheIsEmpty() = moviesCache.upcomingMovie.isEmpty()
}