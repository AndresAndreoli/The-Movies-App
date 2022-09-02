package com.example.themoviesapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviesapp.data.database.entities.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table")
    suspend fun getAllFavoriteMovies(): List<MovieEntity>

    // Con este OnClonflic replace le estoy dificiendo que si inserto una entidad
    // con la misma primary key que otra que ya se encuentra en la DB, lo reemplazara
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)
}