package com.ravi.exploremovie.screens.home

import android.util.Log
import androidx.lifecycle.*
import com.ravi.exploremovie.common.movie.model.MovieListResponse
import com.ravi.exploremovie.common.movie.model.MovieGenreListResponse
import com.ravi.exploremovie.common.movie.model.PersonListResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = HomeRepository()
    private val TAG = "HomeViewModel"

    private val _trendingPersons = MutableLiveData<Result<PersonListResponse>>()
    val trendingPersons: LiveData<Result<PersonListResponse>> get() = _trendingPersons

    private val _discoverMovies = MutableLiveData<Result<MovieListResponse>>()
    val discoverMovies: LiveData<Result<MovieListResponse>> get() = _discoverMovies

    private val _popularMovies = MutableLiveData<Result<MovieListResponse>>()
    val popularMovies: LiveData<Result<MovieListResponse>> get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Result<MovieListResponse>>()
    val topRatedMovies: LiveData<Result<MovieListResponse>> get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Result<MovieListResponse>>()
    val upcomingMovies: LiveData<Result<MovieListResponse>> get() = _upcomingMovies

    private val _movieGenres = MutableLiveData<Result<MovieGenreListResponse>>()
    val movieGenres: LiveData<Result<MovieGenreListResponse>> get() = _movieGenres

    fun fetchTrendingPersons() {
        viewModelScope.launch {
            try {
                val result = repository.getTrendingPersons()
                _trendingPersons.value = result
                if (result.isFailure) {
                    Log.e(TAG, "Failed to fetch trending persons: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching trending persons", e)
                _trendingPersons.value = Result.failure(e)
            }
        }
    }

    fun fetchDiscoverMovies() {
        viewModelScope.launch {
            try {
                val result = repository.getDiscoverMovies()
                _discoverMovies.value = result
                if (result.isFailure) {
                    Log.e(TAG, "Failed to fetch discover movies: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching discover movies", e)
                _discoverMovies.value = Result.failure(e)
            }
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val result = repository.getPopularMovies()
                _popularMovies.value = result
                if (result.isFailure) {
                    Log.e(TAG, "Failed to fetch popular movies: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching popular movies", e)
                _popularMovies.value = Result.failure(e)
            }
        }
    }

    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            try {
                val result = repository.getTopRatedMovies()
                _topRatedMovies.value = result
                if (result.isFailure) {
                    Log.e(TAG, "Failed to fetch top rated movies: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching top rated movies", e)
                _topRatedMovies.value = Result.failure(e)
            }
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            try {
                val result = repository.getUpcomingMovies()
                _upcomingMovies.value = result
                if (result.isFailure) {
                    Log.e(TAG, "Failed to fetch upcoming movies: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching upcoming movies", e)
                _upcomingMovies.value = Result.failure(e)
            }
        }
    }

    fun fetchMovieGenres() {
        viewModelScope.launch {
            try {
                val result = repository.getMovieGenres()
                _movieGenres.value = result
                if (result.isFailure) {
                    Log.e(TAG, "Failed to fetch movie genres: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching movie genres", e)
                _movieGenres.value = Result.failure(e)
            }
        }
    }
}
