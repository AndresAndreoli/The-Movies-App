package com.example.themoviesapp

data class GuestSessionIdResponse(
    var success: String,
    var guest_session_id: String,
    var expires_at: String
)
