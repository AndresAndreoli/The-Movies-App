package com.example.themoviesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.themoviesapp.data.database.dao.MovieDao
import com.example.themoviesapp.data.database.entities.MovieEntity

// Primer parametro es para indicar las entidades de la BD
// Segundo parametro es para indicar las versiones, por si en algun futuro queremos hacer actualizaciones, insertar mas tablas, etc
@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun getMovieDao():MovieDao
}