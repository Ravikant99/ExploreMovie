package com.ravi.exploremovie.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.exploremovie.common.movie.model.GenreResult
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.movieDetails.data.model.MovieDetails
import com.ravi.exploremovie.screens.home.HomeRepository
import com.ravi.exploremovie.video.data.model.TrailerResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.ravi.exploremovie.webServices.Resource


class MovieDetailsViewModel : ViewModel() {
    private val repository = HomeRepository()
    private val TAG = "MovieDetailsViewModel"

    // Movie details state
    private val _currentMovieDetails = MutableStateFlow<MovieDetails?>(null)
    val currentMovieDetails: StateFlow<MovieDetails?> = _currentMovieDetails.asStateFlow()

    // Popular movies
    private val _popularMovies = MutableStateFlow<List<MovieResult>>(emptyList())
    val popularMovies: StateFlow<List<MovieResult>> = _popularMovies.asStateFlow()

    // Genres
    private val _genres = MutableStateFlow<List<GenreResult>>(emptyList())
    val genres: StateFlow<List<GenreResult>> = _genres.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isTrailerLoading = MutableStateFlow(false)
    val isTrailerLoading: StateFlow<Boolean> = _isTrailerLoading.asStateFlow()

    // Error states
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _trailerError = MutableStateFlow<String?>(null)
    val trailerError: StateFlow<String?> = _trailerError.asStateFlow()

    // Trailer state
    private val _trailerState = MutableStateFlow<Resource<TrailerResponse>>(Resource.Loading())
    val trailerState: StateFlow<Resource<TrailerResponse>> = _trailerState.asStateFlow()

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
                    Log.e(exception.toString(), "Error fetching movie details")
                    _error.value = "Failed to fetch movie details: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(e.toString(), "Exception in fetchMovieDetails")
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMovieTrailer(movieId: Int) {
        _isTrailerLoading.value = true
        _trailerError.value = null
        _trailerState.value = Resource.Loading()

        viewModelScope.launch {
            try {
                val result = repository.getMovieTrailer(movieId)
                result.onSuccess { trailerResponse ->
                    _trailerState.value = Resource.Success(trailerResponse)
                    if (trailerResponse.results.isNullOrEmpty()) {
                        _trailerError.value = "No trailers available"
                    }
                }.onFailure { exception ->
                    Log.e(exception.toString(), "Error fetching trailer")
                    _trailerState.value = Resource.Error(exception.message ?: "Unknown error")
                    _trailerError.value = "Failed to fetch trailer: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(e.toString(), "Exception in fetchMovieTrailer")
                _trailerState.value = Resource.Error(e.message ?: "Unknown error")
                _trailerError.value = "An error occurred: ${e.message}"
            } finally {
                _isTrailerLoading.value = false
            }
        }
    }

    // Helper function to get the first YouTube trailer key
    fun getFirstTrailerKey(): String? {
        return (_trailerState.value as? Resource.Success)?.data?.results
            ?.filterNotNull()
            ?.firstOrNull {
                it.site?.equals("YouTube", ignoreCase = true) == true &&
                        it.type?.equals("Trailer", ignoreCase = true) == true
            }
            ?.key
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val result = repository.getPopularMovies()
                result.onSuccess { response ->
                    _popularMovies.value = response.results ?: emptyList()
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

    fun getGenreNamesFromIds(genreIds: List<Int>?): String {
        if (genreIds.isNullOrEmpty()) return "Unknown"

        return _genres.value
            .filter { genre -> genre.id != null && genreIds.contains(genre.id) }
            .mapNotNull { it.name }
            .takeIf { it.isNotEmpty() }
            ?.joinToString(", ")
            ?: "Unknown"
    }
}