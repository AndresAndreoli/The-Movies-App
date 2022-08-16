package com.example.themoviesapp.model

data class GenericResponse<T: Any>(
    val success: Boolean,
    val data: T
)
