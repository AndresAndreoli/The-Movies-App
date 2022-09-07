package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.MovieDetailsResponse
import com.example.themoviesapp.domain.GetMovieDetailsUseCase
import com.example.themoviesapp.domain.HandleFavoriteMovieUseCase
import com.example.themoviesapp.domain.MovieIsFavoriteUseCase
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.utils.ValuesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMovieDetails @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val HandleFavoriteMovieUseCase: HandleFavoriteMovieUseCase,
    private val movieIsFavoriteUseCase: MovieIsFavoriteUseCase
) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetailsResponse>()
    val movieDetails: LiveData<MovieDetailsResponse> = _movieDetails

    private val _movieDetailStatus = MutableLiveData<ValuesProvider.Status>()
    val movieDetailStatus: LiveData<ValuesProvider.Status> = _movieDetailStatus

    private val _favoriteMovie = MutableLiveData<Boolean>(false)
    val favoriteMovie: LiveData<Boolean> = _favoriteMovie

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

            // Check if the movie is favorite
            _favoriteMovie.postValue(movieIsFavoriteUseCase(idMovie))
        }
    }

    fun handleFavoriteMovie(movie: MovieItem, type: ValuesProvider.ActionFavMovie){
        viewModelScope.launch {
            when (type){
                ValuesProvider.ActionFavMovie.ADD -> HandleFavoriteMovieUseCase.addNewFavoriteMovie(movie)
                ValuesProvider.ActionFavMovie.DELETE -> HandleFavoriteMovieUseCase.deleteFavoriteMovie(movie.id!!)
            }
        }
    }
}