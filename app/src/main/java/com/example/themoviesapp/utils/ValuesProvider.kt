package com.example.themoviesapp.utils

class ValuesProvider {
    enum class Status { LOADING, SUCCESS, ERROR }
    enum class ActionFavMovie { ADD, DELETE }
    enum class typeRequest { POPULAR, TOP_RATED, UPCOMING }
    data class loadingContent(
        val type: typeRequest,
        val load: Status)
}

enum class KindOfFragment { HOME_FRAGMENT, FAVORITE_FRAGMENT }