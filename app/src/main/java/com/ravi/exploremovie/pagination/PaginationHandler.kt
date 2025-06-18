import com.ravi.exploremovie.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaginationHandler<T : Any, R : Any>(
    private val coroutineScope: CoroutineScope,
    private val fetch: suspend (Int) -> Result<R>,
    private val extractItems: (R) -> List<T>,
    private val extractTotalPages: (R) -> Int,
    private val initialPage: Int = 1
) {
    private val _state = MutableStateFlow(PaginationState<T>())
    val state: StateFlow<PaginationState<T>> = _state

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        if (_state.value.isLoading) return

        coroutineScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    currentPage = initialPage - 1
                )
            }

            when (val result = fetch(initialPage)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            items = extractItems(result.data),
                            currentPage = initialPage,
                            totalPages = extractTotalPages(result.data),
                            isLoading = false,
                            endReached = false
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isLoading = false,
                            endReached = true
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun loadNextPage() {
        if (shouldLoadNextPage().not()) return

        coroutineScope.launch {
            _state.update { it.copy(isLoadingMore = true, error = null) }
            val nextPage = _state.value.currentPage + 1

            when (val result = fetch(nextPage)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            items = it.items + extractItems(result.data),
                            currentPage = nextPage,
                            totalPages = extractTotalPages(result.data),
                            isLoadingMore = false,
                            endReached = nextPage >= extractTotalPages(result.data)
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isLoadingMore = false
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun retry() {
        if (_state.value.items.isEmpty()) {
            loadInitialData()
        } else {
            loadNextPage()
        }
    }

    private fun shouldLoadNextPage(): Boolean {
        val currentState = _state.value
        return currentState.currentPage < currentState.totalPages &&
                !currentState.isLoadingMore &&
                !currentState.isLoading &&
                !currentState.endReached
    }
}
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
