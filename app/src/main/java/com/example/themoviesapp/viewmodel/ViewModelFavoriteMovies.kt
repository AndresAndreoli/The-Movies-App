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
import com.example.themoviesapp.utils.ValuesProvider
import kotlinx.coroutines.launch

// favorite fragment's ViewModel
@HiltViewModel
class ViewModelFavoriteMovies @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val loadDBWithFavMoviesUseCase: LoadDBWithFavMoviesUseCase
): ViewModel() {

    private val _favoriteMovies = MutableLiveData<List<MovieItem>>()
    val favoriteMovies: LiveData<List<MovieItem>> = _favoriteMovies

    private val _moviesStatus = MutableLiveData<ValuesProvider.Status>()
    val moviesStatus: LiveData<ValuesProvider.Status> = _moviesStatus

    // Recover favories movies from DB
    fun getFavoriteMoviesFromDB(){
        viewModelScope.launch{
            _moviesStatus.postValue(ValuesProvider.Status.LOADING)
            val result = getFavoriteMoviesUseCase()
            _favoriteMovies.postValue(result)
            _moviesStatus.postValue(ValuesProvider.Status.SUCCESS)
        }
    }

    private val _dataBaseStatus = MutableLiveData<ValuesProvider.Status>()
    val dataBaseStatus: LiveData<ValuesProvider.Status> = _dataBaseStatus

    // Load DB with favorite movies
    fun loadDBWithFavMovies(){
        viewModelScope.launch {
            _dataBaseStatus.postValue(ValuesProvider.Status.LOADING)
            loadDBWithFavMoviesUseCase()
            _dataBaseStatus.postValue(ValuesProvider.Status.SUCCESS)
        }
    }

}