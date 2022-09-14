package com.example.themoviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviesapp.data.services.APIService
import com.example.themoviesapp.domain.homeUseCase.*
import com.example.themoviesapp.domain.model.MovieItem
import com.example.themoviesapp.utils.ValuesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// main activity and home fragment's ViewModel
@HiltViewModel
class ViewModelMovies @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val findMovieUseCase: FindMovieUseCase,
    private val retrieveMoviesFromCacheUseCase: RetrieveMoviesFromCacheUseCase,
    private val clearingCacheUseCase: ClearingCacheUseCase,
    private val retrieveFavoriteMoviesFromFirestore: RetrieveFavoriteMoviesFromFirestore,
    private val deleteMoviesBDUseCase: DeleteMoviesBDUseCase
): ViewModel() {

    private val _popularMoviesList = MutableLiveData<List<MovieItem>>()
    val popularMoviesList: LiveData<List<MovieItem>> = _popularMoviesList

    private val _topRatedMoviesList = MutableLiveData<List<MovieItem>>()
    val topRatedMoviesList: LiveData<List<MovieItem>> = _topRatedMoviesList

    private val _upcomingMoviesList = MutableLiveData<List<MovieItem>>()
    val upcomingMoviesList: LiveData<List<MovieItem>> = _upcomingMoviesList

    private val _moviesStatus = MutableLiveData<ValuesProvider.loadingContent>()
    val moviesStatus: LiveData<ValuesProvider.loadingContent> = _moviesStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isConnected = MutableLiveData<Boolean>(true)

    fun onCreateMovies(page: Int){
        if (_isConnected.value!!){
            _isLoading.postValue(true)
            viewModelScope.launch{
                loadPopularContent(page)
                loadTopRatedContent(page)
                loadUpcomingContent(page)
                _isLoading.postValue(false)
            }
        }
    }

    private suspend fun loadPopularContent(page: Int){
        _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.POPULAR, ValuesProvider.Status.LOADING))

        var resultPopular = getPopularMoviesUseCase(APIService.APIkey, page)

        if (resultPopular.success){
            _popularMoviesList.postValue(resultPopular.data)
            _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.POPULAR, ValuesProvider.Status.SUCCESS))
        } else {
            _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.POPULAR, ValuesProvider.Status.ERROR))
        }
    }

    private suspend fun loadTopRatedContent(page: Int){
        _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.TOP_RATED, ValuesProvider.Status.LOADING))

        var resultTopRated = getTopRatedMoviesUseCase(APIService.APIkey, page)

        if (resultTopRated.success){
            _topRatedMoviesList.postValue(resultTopRated.data)
            _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.TOP_RATED, ValuesProvider.Status.SUCCESS))
        } else {
            _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.TOP_RATED, ValuesProvider.Status.ERROR))
        }
    }

    private suspend fun loadUpcomingContent(page: Int){
        _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.UPCOMING, ValuesProvider.Status.LOADING))

        var resultUpcoming = getUpcomingMoviesUseCase(APIService.APIkey, page)

        if (resultUpcoming.success){
            _upcomingMoviesList.postValue(resultUpcoming.data)
            _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.UPCOMING, ValuesProvider.Status.SUCCESS))
        } else {
            _moviesStatus.postValue( ValuesProvider.loadingContent(ValuesProvider.typeRequest.UPCOMING, ValuesProvider.Status.ERROR))
        }
    }


    /*fun loadMoreMovies(page: Int){
        if (_isConnected.value!!){
            _isLoading.postValue(true)
            viewModelScope.launch {
                val result = getPopularMoviesUseCase(APIService.APIkey, page)
                _moviesList.postValue(result.data)
                //_moviesStatus.postValue(ValuesProvider.Status.SUCCESS)
                _isLoading.postValue(false)
            }
        }
    }*/

    // Search movies by Title
    /*fun searchMovie(query: String): Boolean{
        _isLoading.postValue(true)
        var result = findMovieUseCase(query)
        _moviesList.postValue(result)
        return result.isNotEmpty()
    }*/

    /*fun retrieveMoviesFromCache(){
        var result = retrieveMoviesFromCacheUseCase()
        _moviesList.postValue(result)
        _isLoading.postValue(false)
    }*/

    fun clearCache(): Boolean {
        return clearingCacheUseCase()
    }

    fun connectionInternet(isConnected: Boolean){
        _isConnected.postValue(isConnected)
    }

    // Fetch favorite movies ID from firestore and load them on cache volatile
    fun dataFirebase() {
        viewModelScope.launch {
            retrieveFavoriteMoviesFromFirestore()
        }
    }

    // Cleaning the daba base up
    fun clearDB(){
        viewModelScope.launch {
            deleteMoviesBDUseCase()
        }
    }


}