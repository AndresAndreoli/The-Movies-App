package com.example.themoviesapp.data.database.dao

import android.graphics.Movie
import androidx.room.*
import com.example.themoviesapp.data.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies_favorite_table")
    suspend fun getAllFavoriteMovies(): List<MovieEntity>

    // Con este OnClonflic replace le estoy dificiendo que si inserto una entidad
    // con la misma primary key que otra que ya se encuentra en la DB, lo reemplazara
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM movies_favorite_table WHERE ID = :idMovie")
    suspend fun removeMovieFromDB(idMovie: Int)

    @Query("DELETE FROM movies_favorite_table")
    suspend fun clearDB()

    @Query("SELECT * FROM movies_favorite_table WHERE ID = :idMovie")
    suspend fun searchMovie(idMovie: Int): MovieEntity

    @Query("SELECT EXISTS(SELECT * FROM movies_favorite_table WHERE id = :idMovie)")
    fun isRowIsExist(idMovie : Int) : Boolean

    @Query("SELECT id FROM movies_favorite_table")
    suspend fun recoverIDs(): List<Int>
}