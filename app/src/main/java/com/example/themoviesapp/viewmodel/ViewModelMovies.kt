package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.domain.GetMoviesUseCase
import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.services.APIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class typeRequest {CREATE, RESET}
enum class status {LOADING, SUCCESS, ERROR}

@HiltViewModel
class ViewModelMovies @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val cacheMovie: Cache
): ViewModel() {

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList: LiveData<List<Movie>> = _moviesList

    private val _moviesStatus = MutableLiveData<status>()
    val moviesStatus: LiveData<status> = _moviesStatus

    fun onCreateMovies(type: typeRequest, page: Int){
        viewModelScope.launch{
            _moviesStatus.postValue(status.LOADING)

            // Cleaning movies cache
            if (typeRequest.RESET == type){
                cacheMovie.movies = mutableListOf()
            }

            val result = getMoviesUseCase(APIService.APIkey, page)

            if (result.isNotEmpty()){
                _moviesList.postValue(result)
                _moviesStatus.postValue(status.SUCCESS)
            } else {
                //TODO
                _moviesStatus.postValue(status.ERROR)
            }
        }
    }
}