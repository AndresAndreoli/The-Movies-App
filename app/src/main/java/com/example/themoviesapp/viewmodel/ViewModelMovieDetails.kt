package com.example.themoviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.data.database.entities.MovieEntity
import com.example.themoviesapp.domain.GetMovieDetailsUseCase
import com.example.themoviesapp.domain.InsertFavoriteMovieUseCase
import com.example.themoviesapp.domain.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMovieDetails @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase
) : ViewModel() {
    private val _movieDetails = MutableLiveData<MovieDetailsResponse>()
    val movieDetails: LiveData<MovieDetailsResponse> = _movieDetails

    private val _movieDetailStatus = MutableLiveData<ValuesProvider.Status>()
    val movieDetailStatus: LiveData<ValuesProvider.Status> = _movieDetailStatus

    fun onCreateMovieDetails(idMovie: Int) {
        viewModelScope.launch {
            _movieDetailStatus.postValue(ValuesProvider.Status.LOADING)

            val getMovieDetails = getMovieDetailsUseCase(idMovie)

            if (getMovieDetails.success){
                _movieDetails.postValue(getMovieDetails.data)
                _movieDetailStatus.postValue(ValuesProvider.Status.SUCCESS)
            } else {
                _movieDetailStatus.postValue(ValuesProvider.Status.ERROR)
            }
        }
    }

    fun insertFavoriteMovieToDB(movie: MovieItem){
        viewModelScope.launch {
            insertFavoriteMovieUseCase(movie)
        }
    }
}