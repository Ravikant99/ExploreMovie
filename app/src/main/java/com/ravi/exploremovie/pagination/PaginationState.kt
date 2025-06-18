package com.ravi.exploremovie.pagination

data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false
)
