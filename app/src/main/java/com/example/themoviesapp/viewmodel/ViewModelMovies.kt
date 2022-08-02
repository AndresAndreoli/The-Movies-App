package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.domain.GetMoviesUseCase
import com.example.themoviesapp.model.movieResponse.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMovies @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
): ViewModel() {

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList: LiveData<List<Movie>> = _moviesList

    fun onCreateMovies(page: Int){
        viewModelScope.launch{
            val result = getMoviesUseCase(page)
            if (result.isNotEmpty()){
                _moviesList.postValue(result)
            } else {
                //TODO
                println("error")
            }
        }
    }
}