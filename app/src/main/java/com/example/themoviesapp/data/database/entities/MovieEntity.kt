package com.example.themoviesapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Este es el nombre de la tabla de la base de datos
@Entity(tableName = "movies_favorite_table")
data class MovieEntity(
    // ColumnInfo -> name: es el nombre de la columna de la base de datos
    @ColumnInfo(name = "adult") var adult: Boolean,
    @ColumnInfo(name = "backdrop_path") var backdrop_path: String,
    @ColumnInfo(name = "genre_ids") var genre_ids: List<Int>,
    @PrimaryKey @ColumnInfo(name = "id") var id: Int?, //                    ---> Primary key
    @ColumnInfo(name = "original_language") var original_language: String,
    @ColumnInfo(name = "original_title") var original_title: String,
    @ColumnInfo(name = "overview") var overview: String,
    @ColumnInfo(name = "popularity") var popularity: Float?,
    @ColumnInfo(name = "poster_path") var poster_path: String,
    @ColumnInfo(name = "release_date") var release_date: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "video") var video: Boolean,
    @ColumnInfo(name = "vote_average") var vote_average: Float?,
    @ColumnInfo(name = "vote_count") var vote_count: Int?
)