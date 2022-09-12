package com.example.themoviesapp.utils

import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.model.entities.MovieEntity
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.model.movieResponse.MovieModel

/* Esta funcion de extension la voy a usar para mapear el modelo de datos por capas, de esta forma,
 no importa como me llegue la informacion del backend, la capa de dominio y ui trabajaran con
 este modelo de datos siempre*/
fun MovieModel.toDomain() = MovieItem(
    adult,
    backdrop_path,
    genre_ids,
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)

fun MovieEntity.toDomain() = MovieItem(
    adult,
    backdrop_path,
    genre_ids,
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)

fun MovieItem.toDataBase() = MovieEntity(
    adult,
    backdrop_path,
    genre_ids,
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)

fun MovieModel.toDataBase() = MovieEntity(
    adult,
    backdrop_path,
    genre_ids,
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)

fun MovieDetailsResponse.toDataBase() = MovieEntity(
    adult,
    backdrop_path,
    genres.map { it.id!! },
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)

fun Int.converterTime(): String{
    var horas: Int = 0
    var min: Int = this

    while (min>60){
        horas++
        min -= 60
    }
    return "${horas}:${min}"
}