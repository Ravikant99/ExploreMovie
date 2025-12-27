package com.ravi.exploremovie.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.common.movie.model.PersonResult
import com.ravi.exploremovie.screens.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeeMoreViewModel : ViewModel() {
    private val repository = HomeRepository()
    private val TAG = "SeeMoreViewModel"

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Initial loading state (for first page)
    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading.asStateFlow()
    
    // Pagination loading state (for subsequent pages)
    private val _isPaginationLoading = MutableStateFlow(false)
    val isPaginationLoading: StateFlow<Boolean> = _isPaginationLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Movies state
    private val _movies = MutableStateFlow<List<MovieResult>>(emptyList())
    val movies: StateFlow<List<MovieResult>> = _movies.asStateFlow()

    // Persons state
    private val _persons = MutableStateFlow<List<PersonResult>>(emptyList())
    val persons: StateFlow<List<PersonResult>> = _persons.asStateFlow()
    
    // Current page
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    // Total pages
    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()
    
    // Current content type
    private var _currentContentType = ""

    fun fetchPopularMovies(page: Int = 1, isLoadMore: Boolean = false) {
        if (isLoadMore) {
            _isPaginationLoading.value = true
        } else {
            _isInitialLoading.value = true
        }
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getPopularMovies(page)
                result.onSuccess { response ->
                    if (isLoadMore) {
                        _movies.value = _movies.value + response.results
                    } else {
                        _movies.value = response.results
                    }
                    _totalPages.value = response.totalPages
                    _currentPage.value = page
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching popular movies: ${exception.message}", exception)
                    _error.value = "Failed to fetch popular movies: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchPopularMovies", e)
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
                _isInitialLoading.value = false
                _isPaginationLoading.value = false
            }
        }
    }

    fun fetchUpcomingMovies(page: Int = 1, isLoadMore: Boolean = false) {
        if (isLoadMore) {
            _isPaginationLoading.value = true
        } else {
            _isInitialLoading.value = true
        }
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getUpcomingMovies(page)
                result.onSuccess { response ->
                    if (isLoadMore) {
                        _movies.value = _movies.value + response.results
                    } else {
                        _movies.value = response.results
                    }
                    _totalPages.value = response.totalPages
                    _currentPage.value = page
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching upcoming movies: ${exception.message}", exception)
                    _error.value = "Failed to fetch upcoming movies: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchUpcomingMovies", e)
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
                _isInitialLoading.value = false
                _isPaginationLoading.value = false
            }
        }
    }

    fun fetchTopRatedMovies(page: Int = 1, isLoadMore: Boolean = false) {
        if (isLoadMore) {
            _isPaginationLoading.value = true
        } else {
            _isInitialLoading.value = true
        }
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getTopRatedMovies(page)
                result.onSuccess { response ->
                    if (isLoadMore) {
                        _movies.value = _movies.value + response.results
                    } else {
                        _movies.value = response.results
                    }
                    _totalPages.value = response.totalPages
                    _currentPage.value = page
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching top rated movies: ${exception.message}", exception)
                    _error.value = "Failed to fetch top rated movies: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchTopRatedMovies", e)
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
                _isInitialLoading.value = false
                _isPaginationLoading.value = false
            }
        }
    }

    fun fetchTrendingPersons(page: Int = 1, isLoadMore: Boolean = false) {
        if (isLoadMore) {
            _isPaginationLoading.value = true
        } else {
            _isInitialLoading.value = true
        }
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getTrendingPersons(page)
                result.onSuccess { response ->
                    if (isLoadMore) {
                        _persons.value = _persons.value + response.results
                    } else {
                        _persons.value = response.results
                    }
                    _totalPages.value = response.totalPages
                    _currentPage.value = page
                }.onFailure { exception ->
                    Log.e(TAG, "Error fetching trending persons: ${exception.message}", exception)
                    _error.value = "Failed to fetch trending persons: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchTrendingPersons", e)
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
                _isInitialLoading.value = false
                _isPaginationLoading.value = false
            }
        }
    }

    fun fetchContentByType(contentType: String, page: Int = 1, isLoadMore: Boolean = false) {
        if (_currentContentType != contentType || isLoadMore || (_movies.value.isEmpty() && _persons.value.isEmpty())) {
            _currentContentType = contentType
            
            when (contentType) {
                "popular" -> fetchPopularMovies(page, isLoadMore)
                "upcoming" -> fetchUpcomingMovies(page, isLoadMore)
                "top_rated" -> fetchTopRatedMovies(page, isLoadMore)
                "trending_persons" -> fetchTrendingPersons(page, isLoadMore)
                else -> {
                    Log.e(TAG, "Unknown content type: $contentType")
                    _error.value = "Unknown content type: $contentType"
                }
            }
        } else {
            _isInitialLoading.value = false
        }
    }
    
    fun loadNextPage() {
        if (_currentPage.value < _totalPages.value && !_isPaginationLoading.value) {
            fetchContentByType(_currentContentType, _currentPage.value + 1, true)
        }
    }
} 