package com.example.themoviesapp.model.movieResponse

import com.example.themoviesapp.data.Cache
import com.example.themoviesapp.data.database.dao.MovieDao
import com.example.themoviesapp.data.database.entities.MovieEntity
import com.example.themoviesapp.model.GenericResponse
import com.example.themoviesapp.data.services.MoviesService
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.utils.toDataBase
import com.example.themoviesapp.utils.toDomain
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesService: MoviesService,
    private val moviesCache: Cache,
    private val moviesDao: MovieDao
    ) {
    suspend fun getAllMovies(apiKey:String, page: Int): GenericResponse<List<MovieItem>>{
        return if (chacheIsEmpty() || page>1){
            var movies = moviesService.getMoviesResponse(apiKey, page)
            moviesCache.movie.addAll(movies.data.movieModels)
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

    suspend fun getFavoriteMoviesFromDB(): List<MovieItem>{
        // mapeo el modelo de datos de ModelEntity a Model
        val response: List<MovieEntity> = moviesDao.getAllFavoriteMovies()
        return response.map { it.toDomain() }
    }

    suspend fun insertFavoriteMovieToDB(movie: MovieItem){
        val convertMovie = movie.toDataBase()
        moviesDao.insertMovie(convertMovie)
    }

    fun getMoviesFromCache(): List<MovieItem>{
        // mapeo el modelo de datos de ModelEntity a Model
        val response : List<MovieModel> = moviesCache.movie.toList()
        return response.map { it.toDomain() }
    }

    fun chacheIsEmpty(): Boolean{
        // TODO: cambiar solo a caso de uso
        return moviesCache.movie.isEmpty()
    }

    fun clearCache(): Boolean{
        // TODO: cambiar solo a caso de uso
        moviesCache.movie.clear()
        return moviesCache.movie.isEmpty()
    }

    suspend fun insertMovieToDB(movie: MovieEntity) {
        moviesDao.insertMovie(movie)
    }
}