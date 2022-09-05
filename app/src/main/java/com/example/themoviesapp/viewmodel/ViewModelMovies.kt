package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.domain.ClearingCacheUseCase
import com.example.themoviesapp.domain.FindMovieUseCase
import com.example.themoviesapp.domain.GetMoviesUseCase
import com.example.themoviesapp.domain.RetrieveMoviesFromCacheUseCase
import com.example.themoviesapp.model.movieResponse.MovieModel
import com.example.themoviesapp.data.services.APIService
import com.example.themoviesapp.domain.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMovies @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val findMovieUseCase: FindMovieUseCase,
    private val retrieveMoviesFromCacheUseCase: RetrieveMoviesFromCacheUseCase,
    private val clearingCacheUseCase: ClearingCacheUseCase
): ViewModel() {

    private val _moviesList = MutableLiveData<List<MovieItem>>()
    val moviesList: LiveData<List<MovieItem>> = _moviesList

    private val _moviesStatus = MutableLiveData<ValuesProvider.Status>()
    val moviesStatus: LiveData<ValuesProvider.Status> = _moviesStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isConnected = MutableLiveData<Boolean>(true)

    fun onCreateMovies(page: Int){
        if (_isConnected.value!!){
            _isLoading.postValue(true)
            viewModelScope.launch{
                _moviesStatus.postValue(ValuesProvider.Status.LOADING)

                    var result = getMoviesUseCase(APIService.APIkey, page)

                    if (result.success){
                        _moviesList.postValue(result.data)
                        _moviesStatus.postValue(ValuesProvider.Status.SUCCESS)
                    } else {
                        _moviesStatus.postValue(ValuesProvider.Status.ERROR)
                }
                _isLoading.postValue(false)
            }
        }
    }

    fun loadMoreMovies(page: Int){
        if (_isConnected.value!!){
            _isLoading.postValue(true)
            viewModelScope.launch {
                val result = getMoviesUseCase(APIService.APIkey, page)
                _moviesList.postValue(result.data)
                _moviesStatus.postValue(ValuesProvider.Status.SUCCESS)
                _isLoading.postValue(false)
            }
        }
    }

    // Search movies by Title
    fun searchMovie(query: String): Boolean{
        _isLoading.postValue(true)
        var result = findMovieUseCase(query)
        _moviesList.postValue(result)
        return result.isNotEmpty()
    }

    fun retrieveMoviesFromCache(){
        var result = retrieveMoviesFromCacheUseCase()
        _moviesList.postValue(result)
        _isLoading.postValue(false)
    }

    fun clearCache(): Boolean {
        return clearingCacheUseCase()
    }

    fun connectionInternet(isConnected: Boolean){
        _isConnected.postValue(isConnected)
    }
}