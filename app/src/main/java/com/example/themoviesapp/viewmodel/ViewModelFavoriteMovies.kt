package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviesapp.domain.GetFavoriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.domain.LoadDBWithFavMoviesUseCase
import com.example.themoviesapp.domain.model.MovieItem
import kotlinx.coroutines.launch

// favorite fragment's ViewModel
@HiltViewModel
class ViewModelFavoriteMovies @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val loadDBWithFavMoviesUseCase: LoadDBWithFavMoviesUseCase
): ViewModel() {

    private val _favoriteMovies = MutableLiveData<List<MovieItem>>()
    val favoriteMovies: LiveData<List<MovieItem>> = _favoriteMovies

    fun getFavoriteMoviesFromDB(){
        viewModelScope.launch{
            val result = getFavoriteMoviesUseCase()
            _favoriteMovies.postValue(result)
        }
    }

    fun loadDBWithFavMovies(){
        viewModelScope.launch {
            loadDBWithFavMoviesUseCase()
        }
    }
}