package com.ravi.exploremovie.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.exploremovie.common.movie.model.GenreResult
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.movieDetails.data.model.MovieDetails
import com.ravi.exploremovie.screens.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel() {
    private val repository = HomeRepository()
    private val TAG = "MovieDetailsViewModel"

    // Movie details state
    private val _currentMovieDetails = MutableStateFlow<MovieDetails?>(null)
    val currentMovieDetails: StateFlow<MovieDetails?> = _currentMovieDetails.asStateFlow()

    // Popular movies for reference if needed
    private val _popularMovies = MutableStateFlow<List<MovieResult>>(emptyList())
    val popularMovies: StateFlow<List<MovieResult>> = _popularMovies.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Genre mapping
    private val _genres = MutableStateFlow<List<GenreResult>>(emptyList())
    val genres: StateFlow<List<GenreResult>> = _genres.asStateFlow()

    init {
        fetchPopularMovies()
        fetchGenres()
    }

    fun fetchMovieDetails(movieId: Int) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getMovieDetails(movieId)
                result.onSuccess { movieDetails ->
                    _currentMovieDetails.value = movieDetails
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching movie details: ${exception.message}", exception)
                    _error.value = "Failed to fetch movie details: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchMovieDetails", e)
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val result = repository.getPopularMovies()
                result.onSuccess { response ->
                    _popularMovies.value = response.results
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching popular movies: ${exception.message}", exception)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchPopularMovies", e)
            }
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            try {
                val result = repository.getMovieGenres()
                result.onSuccess { response ->
                    _genres.value = response.genres?.filterNotNull() ?: emptyList()
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching genres: ${exception.message}", exception)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchGenres", e)
            }
        }
    }

    fun getGenreFromIds(genreIds: List<Int>?): String {
        if (genreIds.isNullOrEmpty()) return "Unknown"
        
        val matchedGenres = _genres.value.filter { genre -> 
            genre.id != null && genreIds.contains(genre.id) 
        }
        
        return if (matchedGenres.isNotEmpty()) {
            matchedGenres.mapNotNull { it.name }.joinToString(", ")
        } else {
            "Unknown"
        }
    }
} 