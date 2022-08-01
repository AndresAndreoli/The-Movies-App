package com.example.themoviesapp

data class MovieRateResponse(
    var success: String,
    var status_code: Int,
    var status_message: String
)

data class RateObject(
    var value: Float
)
