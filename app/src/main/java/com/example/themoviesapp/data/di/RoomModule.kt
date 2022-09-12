package com.example.themoviesapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.themoviesapp.data.database.MovieDatabase
import com.example.themoviesapp.data.database.dao.MovieDao
import com.example.themoviesapp.model.entities.MovieEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    // nombre que tendra la base de datos
    private const val MOVIE_DATABASE_NAME = "movie_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MovieDatabase::class.java, MOVIE_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideMovieDao(db: MovieDatabase): MovieDao {
        return db.getMovieDao()
    }
}