package com.example.themoviesapp.domain.model

// Este es el modelo de datos con el que la capa de UI y la capa de domio van a trabajar, ya que si
// se cambiar la base de adtos o retrofit, este no se vera afectado porque la informacion que llegue al
// dominio va a ser de este tipo de modelo de dato.
data class MovieItem(
    var adult: Boolean,
    var backdrop_path: String,
    var genre_ids: List<Int>,
    var id: Int?,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Float?,
    var poster_path: String,
    var release_date: String,
    var title: String,
    var video: Boolean,
    var vote_average: Float?,
    var vote_count: Int?
)
