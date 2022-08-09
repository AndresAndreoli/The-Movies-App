package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.domain.FindMovieUseCase
import com.example.themoviesapp.domain.GetMoviesUseCase
import com.example.themoviesapp.model.Cache
import com.example.themoviesapp.model.movieResponse.Movie
import com.example.themoviesapp.services.APIService
import com.example.themoviesapp.view.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TypeRequest {CREATE, RESET}
enum class Status {LOADING, SUCCESS, ERROR}

@HiltViewModel
class ViewModelMovies @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val findMovieUseCase: FindMovieUseCase
): ViewModel() {

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList: LiveData<List<Movie>> = _moviesList

    private val _moviesStatus = MutableLiveData<Status>()
    val moviesStatus: LiveData<Status> = _moviesStatus

    fun onCreateMovies(type: TypeRequest, page: Int){
        viewModelScope.launch{
            _moviesStatus.postValue(Status.LOADING)

            // Cleaning movies cache
            if (TypeRequest.RESET == type){
                //cacheMovie.movies = mutableListOf()
            }

            val result = getMoviesUseCase(APIService.APIkey, page)

            if (result.isNotEmpty()){
                _moviesList.postValue(result)
                _moviesStatus.postValue(Status.SUCCESS)
            } else {
                //TODO
                _moviesStatus.postValue(Status.ERROR)
            }
        }
    }
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadMoreMovies(page: Int){
        _isLoading.postValue(true)
        viewModelScope.launch {
            val result = getMoviesUseCase(APIService.APIkey, page)
            _moviesList.postValue(result)
            _isLoading.postValue(false)
        }
    }

    fun searchMovie(query: String){
        _isLoading.postValue(true)
        var result = findMovieUseCase(query)
        _moviesList.postValue(result)
    }
}