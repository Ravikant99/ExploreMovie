package com.ravi.exploremovie.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.exploremovie.common.movie.model.MovieResult
import com.ravi.exploremovie.screens.home.HomeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    // Search query with debounce
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Selected category filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Search results
    private val _searchResults = MutableStateFlow<List<MovieResult>>(emptyList())
    val searchResults: StateFlow<List<MovieResult>> = _searchResults.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isPaginationLoading = MutableStateFlow(false)
    val isPaginationLoading: StateFlow<Boolean> = _isPaginationLoading.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Pagination
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    // Track search job to cancel previous searches
    private var searchJob: Job? = null

    init {
        // Setup debounce for search query (300ms delay)
        _query
            .debounce(300)
            .onEach { query ->
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    _searchResults.value = emptyList()
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun loadMoreResults() {
        if (_currentPage.value < _totalPages.value && !_isPaginationLoading.value) {
            viewModelScope.launch {
                _isPaginationLoading.value = true
                performSearch(
                    query = _query.value,
                    page = _currentPage.value + 1,
                    isLoadMore = true
                )
            }
        }
    }

    fun performSearch(
        query: String,
        page: Int = 1,
        isLoadMore: Boolean = false
    ) {
        // Cancel previous search if it's still running
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                if (!isLoadMore) {
                    _isLoading.value = true
                    _currentPage.value = 1
                }

                val result = repository.getSearchData(query, page)

                result.onSuccess { response ->
                    if (isLoadMore) {
                        _searchResults.value = _searchResults.value + response.results
                    } else {
                        _searchResults.value = response.results
                    }
                    _totalPages.value = response.totalPages
                    _currentPage.value = page
                    _error.value = null
                }.onFailure { exception ->
                    _error.value = "Failed to search: ${exception.message}"
                    if (!isLoadMore) {
                        _searchResults.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to search: ${e.localizedMessage}"
                if (!isLoadMore) {
                    _searchResults.value = emptyList()
                }
            } finally {
                _isLoading.value = false
                _isPaginationLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}